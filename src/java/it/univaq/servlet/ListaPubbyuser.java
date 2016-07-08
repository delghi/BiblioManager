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
public class ListaPubbyuser extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
    //controllo sessione
        HttpSession s = SecurityLayer.checkSession(request);
        
        //mappa per contere dati
        Map data = new HashMap();
        //se l'utente è in sessione e ho l'id dell'utente
        if(s != null && !isNull(request.getParameter("id"))){
        data.put("sessione", s.getAttribute("username"));
       data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
       int userid = Integer.parseInt(request.getParameter("id"));
       List<Pubblicazione> lista_pubblicazioni = new ArrayList<Pubblicazione>();
       

SimpleDateFormat datapubbl = new SimpleDateFormat("dd/MM/yyyy");
       ResultSet rs = Database.selectJoinDet("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", "files", "ristampa.copertina=files.id", "idutente="+userid);
       while(rs.next()){
           String titolo = rs.getString("titolo");
           String autore = rs.getString("autore");
           int id = rs.getInt("id");
           String editore = rs.getString("editore");
           String data_pubblicazione = datapubbl.format(rs.getDate("datapub"));
           String copertina = rs.getString("files.name");
Pubblicazione pub = new Pubblicazione(id, titolo, autore, editore, data_pubblicazione, copertina);           
           lista_pubblicazioni.add(pub);
       }
       data.put("lista_pubblicazioni", lista_pubblicazioni);
       
      FreeMarker.process("pubblicazioni_utente.html", data, response, getServletContext());
        } else response.sendRedirect("login");
       
       
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
            Logger.getLogger(ListaPubbyuser.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ListaPubbyuser.class.getName()).log(Level.SEVERE, null, ex);
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
