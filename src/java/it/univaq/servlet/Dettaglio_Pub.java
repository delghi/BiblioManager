/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.model.Recensione;
import it.univaq.model.Ristampa;
import it.univaq.model.StoriaMod;
import it.univaq.util.DataUtil;
import it.univaq.util.Database;
import it.univaq.util.FreeMarker;
import it.univaq.util.SecurityLayer;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Luca
 */
public class Dettaglio_Pub extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //controllo sessione
        HttpSession s = SecurityLayer.checkSession(request);
        
        //mappa per contere dati
        Map data = new HashMap();
        //se l'utente è in sessione e ho l'id della pubblicazione
        if(s != null && !isNull(request.getParameter("id"))){
            data.put("sessione", s.getAttribute("username"));
                        data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));

            //controllo dati inseriti nella form recensione
            if(!isNull(request.getParameter("titolo")) && !isNull(request.getParameter("voto")) && !isNull(request.getParameter("testo"))){
                //eseguo metodo per inserimento recensione
                action_insert_recensione(request);
            }
        //se non ha inserito una recensione vado avanti e mostro dettaglio pubblicazione
        //recupero id della recensione
        int idpubb = Integer.parseInt(request.getParameter("id"));
        //creo lista ristampe, lista recensioni e lista modifiche
        List<Ristampa> ristampe = new ArrayList();
        List<Recensione> recensioni = new ArrayList();
        List<StoriaMod> modifiche = new ArrayList();
        data.put("id",idpubb);
        try {
            
            
            ResultSet rs = Database.selectJoinDet("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", "files", "ristampa.copertina=files.id", "pubblicazione.id="+idpubb );
            
            
           
            while(rs.next()){
                String titolo = rs.getString("titolo");
                String autore = rs.getString("autore");
                String descrizione = rs.getString("descrizione");
                String copertina = rs.getString("name");
                int idu = rs.getInt("idutente");
                ResultSet user = Database.selectRecord("utenti", "id="+idu);
                while(user.next()){
                    data.put("nome", user.getString("nome"));
                    data.put("cognome", user.getString("cognome"));
                }
                data.put("titolo", titolo);
                data.put("autore", autore);
                data.put("descrizione", descrizione);
                data.put("copertina", copertina);
            }
//             
             //recupero info ristampa
             SimpleDateFormat datapubbl = new SimpleDateFormat("dd/MM/yyyy");
             SimpleDateFormat datainsert = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
             
             ResultSet rs2 = Database.selectRecord("ristampa", "idpub="+idpubb, "datainsert");
             while(rs2.next()){
                 long isbn = rs2.getLong("isbn");
                 int numpagine = rs2.getInt("numpagine");
                 String editore = rs2.getString("editore");
                 String lingua = rs2.getString("lingua");
                 int download = rs2.getInt("download");
                 String data_inserimento = datainsert.format(rs2.getDate("datainsert"));
//                    String data_inserimento  = DataUtil.dateToString(rs2.getDate("datainsert"));
                 int idpub = rs2.getInt("idpub");
                 String data_pubblicazione = datapubbl.format(rs2.getDate("datapub"));
                 
//                    String data_pubblicazione  = DataUtil.dateToString(rs2.getDate("datapub"));    
                   //creo oggetto temporaneo ristampa
                 Ristampa rist = new Ristampa(isbn, numpagine, lingua, editore, data_pubblicazione, idpub, data_inserimento, download );
                //lo aggiungo alla lista delle ristampe
                 ristampe.add(rist);
             }
//              //recupero info recensioni 
              
              ResultSet rs3 = Database.selectRecord("recensione", "idpub=" +idpubb+ " && pubblicata=1 ");
              while(rs3.next()){
                  int idr = rs3.getInt("id");
                  String titolor = rs3.getString("titolo");
                  String testor = rs3.getString("testo");
                  String utente =  DataUtil.getUserbyId(rs3.getInt("userid"));
                  int votor = rs3.getInt("voto");
                  int idpubr = rs3.getInt("idpub");
                  int pubblicata = rs3.getInt("pubblicata");
                  //creo oggetto temporaneo recensione
                  Recensione rec = new Recensione(idr, titolor, testor, utente, votor, idpubr, pubblicata);
                  //lo aggiungo alla lista delle recensioni
                  recensioni.add(rec);
                  
              }
                           SimpleDateFormat datamodifica = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                 //recupero lista modifiche fai dopo
                 ResultSet rs4 = Database.selectJoinDet("pubblicazione", "storia", "pubblicazione.id=storia.idpubb", "utenti", "storia.userid=utenti.id", "pubblicazione.id="+idpubb);
                 while(rs4.next()){
                     String username = rs4.getString("username");
                  String datamod = datamodifica.format(rs4.getDate("storia.datamod"));
                  String descrmod = rs4.getString("storia.descrizionemod");
                  StoriaMod storia = new StoriaMod( datamod, username, descrmod );
                  modifiche.add(storia);
                 }
             //dopo aver preso tutti i dati creo il data-model per freemarker
             
             data.put("lista_recensioni", recensioni);
             data.put("lista_ristampe", ristampe);
             data.put("lista_modifiche", modifiche);
             data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
             data.put("session", s.getAttribute("username"));
             FreeMarker.process("dettaglio_pubb.html", data, response, getServletContext());
             
                
            
        } catch (SQLException ex) {
            Logger.getLogger(Dettaglio_Pub.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Dettaglio_Pub.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        }else response.sendRedirect("login");
    }
    
    
    //metodo eseguito quando un utente inserisce una recensione
    protected void action_insert_recensione (HttpServletRequest request){
        //controllo sessione
        HttpSession s = SecurityLayer.checkSession(request);
       //mappa per contenere i dati
       Map data = new HashMap();
       if(s != null){
        //prendo titolo, testo, voto della recensione + l'id pubblicazione e id utente
            String titolo = request.getParameter("titolo");
            int voto = Integer.parseInt(request.getParameter("voto"));
            String testo = request.getParameter("testo");
            int id_pubb = Integer.parseInt(request.getParameter("id"));
            //e li inserisco
            data.put("titolo", titolo);
             data.put("testo", testo);
             data.put("userid", s.getAttribute("userid"));
            data.put("voto", voto);
           
            data.put("idpub", id_pubb);
            try {
                Database.insertRecord("recensione", data);
            } catch (SQLException ex) {
                Logger.getLogger(Dettaglio_Pub.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
        
        
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
