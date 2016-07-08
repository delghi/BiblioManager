/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.model.Pubblicazione;
import it.univaq.util.DataUtil;
import it.univaq.util.Database;
import it.univaq.util.FreeMarker;
import it.univaq.util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class Profilo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
        
        HttpSession s = SecurityLayer.checkSession(request);
        Map data = new HashMap();
        if(s != null){
         //utente in sessione quindi loggato, recupero dati utente
         String username = (String) s.getAttribute("username");
         data.put("sessione", s.getAttribute("username"));
         data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
         
         ResultSet rs = Database.selectRecord("utenti", "username='" + username + "'");
         SimpleDateFormat datanasc = new SimpleDateFormat("dd/MM/yyyy");
         while(rs.next()){
             String email = rs.getString("username");
             String nome = rs.getString("nome");
             String cognome = rs.getString("cognome");
             String datanascita = datanasc.format(rs.getDate("datanascita"));
             data.put("username", email);
             data.put("nome", nome);
             data.put("cognome", cognome);
             data.put("datanascita", datanascita);
               
         }
         int userid = (int) s.getAttribute("userid");
       List<Pubblicazione> lista_pubblicazioni = new ArrayList<Pubblicazione>();
       

SimpleDateFormat datapubbl = new SimpleDateFormat("dd/MM/yyyy");
       ResultSet rs2 = Database.selectJoinDet("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", "files", "ristampa.copertina=files.id", "idutente="+userid);
       while(rs2.next()){
           String titolo = rs2.getString("titolo");
           String autore = rs2.getString("autore");
           int id = rs2.getInt("id");
           String editore = rs2.getString("editore");
           String data_pubblicazione = datapubbl.format(rs2.getDate("ristampa.datapub"));
           String copertina = rs2.getString("files.name");
Pubblicazione pub = new Pubblicazione(id, titolo, autore, editore, data_pubblicazione, copertina);           
           lista_pubblicazioni.add(pub);
       }
       data.put("lista_pubblicazioni", lista_pubblicazioni);
         
         
            FreeMarker.process("profilo_pers.html", data, response, getServletContext());
            
            
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
            Logger.getLogger(Profilo.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Profilo.class.getName()).log(Level.SEVERE, null, ex);
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
