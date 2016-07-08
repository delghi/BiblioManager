/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.util.Database;
import it.univaq.util.SecurityLayer;
import it.univaq.util.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luca
 */
public class Download extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response){
        int res = SecurityLayer.checkNumeric(request.getParameter("file"));
            request.setAttribute("idf", res);
        try {
            action_download(request, response);
        } catch (IOException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }

    
    
    private void action_download(HttpServletRequest request, HttpServletResponse response) throws IOException, NamingException, SQLException {
        Integer res = (Integer) request.getAttribute("idf");
        if (res != null) {
            StreamResult result = new StreamResult(getServletContext());
//            try (Connection c = ds.getConnection(); PreparedStatement s = c.prepareStatement(GET_FILE_QUERY)) {
//                s.setInt(1, res);
//                try (ResultSet rs = s.executeQuery()) {
                ResultSet rs = Database.selectRecord("files", "id="+res);
                    if (rs.next()) {
                        try (InputStream is = new FileInputStream(
                                getServletContext().getInitParameter("uploads.directory") + File.separatorChar + rs.getString("name"))) {
                            request.setAttribute("contentType", rs.getString("type"));
                            result.activate(is, rs.getLong("size"), rs.getString("name"), request, response);
                        }
                    } else {
                        request.setAttribute("exception", new Exception("Resurce not found in file database"));
                        
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
