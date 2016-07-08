/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.model.Recensione;
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
public class User_admin extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
                    
        HttpSession s = SecurityLayer.checkSession(request);
        Map data = new HashMap(); 
        //controllo permessi e sessione
        if(s != null && (DataUtil.getTipologiaByEmail((String) s.getAttribute("username"))) == 1){
            //utente attivo ok modifica permessi utente
             //creo lista utenti
        List<Utente> lista_utenti = new ArrayList();
         List<Recensione> listarec = new ArrayList();
        //seleziono tutti gli utenti che non sono attivi
        ResultSet rs = Database.selectRecord("utenti", "tipologia=0");
        while(rs.next()){
            String username = rs.getString("username");
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            int tipologia = rs.getInt("tipologia");
            //creo oggetto utente 
          Utente user = new Utente(username, nome, cognome, tipologia);
          //inserisco nella lista
          lista_utenti.add(user);
        }
        //mi porto dietro la tipologia e username per freemarker
          data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
          data.put("sessione", s.getAttribute("username"));
          data.put("lista_utenti",lista_utenti);
        //se utente attivo vuole promuovere
        if(!isNull(request.getParameter("user"))){
            //mappa per update record
            Map map = new HashMap();
            int tipo = DataUtil.getTipologiaByEmail(request.getParameter("user"));
            if(tipo != 1){
                map.put("tipologia", 1);
                Database.updateRecord("utenti", map, "username='" + request.getParameter("user") + "'");
                response.sendRedirect("user_admin");
            }
        }
        //seleziono tutte le recensioni da approvare
        ResultSet rs2 = Database.selectRecord("recensione", "pubblicata=0");
        while(rs2.next()){
            int idr = rs2.getInt("id");
                  String titolor = rs2.getString("titolo");
                  String testor = rs2.getString("testo");
                  String utente =  DataUtil.getUserbyId(rs2.getInt("userid"));
                  int votor = rs2.getInt("voto");
                  int idpubr = rs2.getInt("idpub");
                  int pubblicata = rs2.getInt("pubblicata");
                  //creo oggetto temporaneo recensione
                  Recensione rec = new Recensione(idr, titolor, testor, utente, votor, idpubr, pubblicata);
                  //lo aggiungo alla lista delle recensioni
                  listarec.add(rec);
        }
        
                  data.put("lista_recensioni",listarec);

        
        
        
        //se l utente approva
            if (!isNull(request.getParameter("approva"))){
                 //approvazione
         int id = Integer.parseInt(request.getParameter("recensione"));
          Map<String, Object> temp= new HashMap();
          temp.put("pubblicata",'1');
          Database.updateRecord("recensione", temp, "id='"+id+"'");
          response.sendRedirect("user_admin");
          
        }
            //se utente elimina
        if (!isNull(request.getParameter("elimina"))){
          int id = Integer.parseInt(request.getParameter("recensione"));
          Database.deleteRecord("recensione", "id="+id);
          data.put("lista_recensioni",listarec );
          
        }
        
        
        
        
        
        
        
        FreeMarker.process("backoffice.html", data, response, getServletContext());
        
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
        } catch (SQLException ex) {
            Logger.getLogger(User_admin.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(User_admin.class.getName()).log(Level.SEVERE, null, ex);
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
