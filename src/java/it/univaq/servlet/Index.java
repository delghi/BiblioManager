/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.model.Pubblicazione;
import it.univaq.model.Utente;
import it.univaq.util.DataUtil;
import it.univaq.util.Database;
import it.univaq.util.FreeMarker;
import it.univaq.util.SecurityLayer;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Luca
 */
public class Index extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NamingException {
        
            try {
                Map data = null;
                try {
                    
                    //serie di controlli di sicurezzasulla sessione corrente
                    HttpSession s = SecurityLayer.checkSession(request);
                    // dichiaro mappa da passara successivamente a freemarker
                    data = new HashMap();
                    //controllo se l'utente è in sessione
                    int tipologia = 0;
                    if(s != null){
                        try {
//                            Database.connect();
                            //metto username dell'utente nella mappa
                            data.put("sessione", s.getAttribute("username"));
                            //prendo dal db la tipologia di utente---> 0 utente normale || 1 utente attivo
                            tipologia = DataUtil.getTipologiaByEmail((String) s.getAttribute("username"));
                            //e la inserisco nella mappa
//                            Database.close();
                            data.put("tipologia", tipologia);
                        } catch (Exception ex) {
                            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    
                    // mostro ULTIME PUBBLICAZIONI INSERITE come da specifica
                    //dichiaro collezione che conterrà lista delle ultimi pubblicazioni
                    ArrayList<Pubblicazione> lista_ultime = new ArrayList<Pubblicazione>();
//                    Database.connect();
                    //eseguo query per selezionare  le ultime  pubblicazioni inserite (5)
                     
//                    ResultSet rs = Database.selectJoin("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", "pubblicazione.titolo", " datainsert " , 5);
                    ResultSet rs = Database.selectJoin3("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", "files", "ristampa.copertina=files.id", "pubblicazione.titolo", " datainsert ", 5);
                   String descrizione = null;
                     //scorro risultati e creo nuova pubblicazione
                    while(rs.next()){
                        int id = rs.getInt("pubblicazione.id");
                        String titolo = rs.getString("titolo");
                        String autore = rs.getString("autore");
                        descrizione = rs.getString("files.name");
                        
                      
                        Pubblicazione p = new Pubblicazione(id, titolo, autore, descrizione);
                        //inserisco ogni pubbl nella lista
                        lista_ultime.add(p);
                        
                    }
                    
//                    ResultSet dw = Database.selectRecord("files", "id="+download);
//                    while(dw.next()){
//                      data.put("copertina", dw.getString("name"));
//                    }
//                    Database.close();
                    //passo la lista alla mappa per freemarker
                    data.put("lista_ultime", lista_ultime);
                } catch (SQLException ex) {
                    Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                //mostro ULTIME PUBBLICAZIONI MODIFICATE come da specifica
                
                //dichiaro collezione che conterrà lista delle ultimi pubblicazioni modificate
                ArrayList<Pubblicazione> lista_modificate = new ArrayList<Pubblicazione>();
                 ResultSet rs2 = Database.selectJoinMod("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", "storia", "pubblicazione.id=storia.idpubb", "files" , "ristampa.copertina=files.id", "datamod", 5);
    //scorro risultati, creo pubblicazione e li inserisco nella lista
                while(rs2.next()){
                    int id = rs2.getInt("idpubb");
                    String titolo = rs2.getString("titolo");
                    String copertina = rs2.getString("name");
                    Pubblicazione p = new Pubblicazione (id, titolo, copertina);
                    lista_modificate.add(p);
                    
                }
                //passo la lista alla mappa per freemarker
                data.put("lista_modificate", lista_modificate);
                  //chiudo connessione al db
//                        Database.close();
            
//utenti più collaborativi

ArrayList<Utente> lista_attivi = new ArrayList<>();


ResultSet rs3 = Database.selectRecordU("utenti", "inserite", 4);
while(rs3.next()){
    int id = rs3.getInt("id");
    String nome = rs3.getString("nome");
    String cognome = rs3.getString("cognome");
    String email = rs3.getString("username");
    int num_inserite = rs3.getInt("inserite");
    
    Utente utente = new Utente(id, nome, cognome, email, num_inserite);
    lista_attivi.add(utente);
}

data.put("lista_attivi", lista_attivi);




            //passo tutti i dati a freemarker che si occuperà di mostrarli a video
            FreeMarker.process("home.html", data, response, getServletContext());
            
             } catch (SQLException ex) {
                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            processRequest(request, response);
        } catch (NamingException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (NamingException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
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
