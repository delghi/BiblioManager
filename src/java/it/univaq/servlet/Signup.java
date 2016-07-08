/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.util.DataUtil;
import static it.univaq.util.DataUtil.crypt;
import it.univaq.util.Database;
import it.univaq.util.FreeMarker;
import it.univaq.util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class Signup extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws NamingException {
        
        //verifico sessione
        HttpSession s = SecurityLayer.checkSession(request);
        //dichiaro mappa per freemarker
        Map data = new HashMap();
         // se l'utente non è in sessione procedo con la signup
         if(s == null){
            try {
                //recupero i dati dalla form
                String nome = request.getParameter("nome_reg");
                String cognome = request.getParameter("cognome_reg");
                String email = request.getParameter("email_reg");
                String password = request.getParameter("password_reg");
                String data_nascita = request.getParameter("data");
               
                //verifico se l'email è valida 
                if(DataUtil.checkEmail(email)){
                
                //dichiaro mappa per contenere dati registrazione
                Map dati = new HashMap();
                dati.put("nome", nome);
                dati.put("cognome", cognome);
                dati.put("username", email);
                dati.put("password", crypt(password));
                dati.put("datanascita", data_nascita);
                
                //eseguo query di inserimento
                Database.insertRecord("utenti", dati);
                
                //recupero userid utente appena inserito
                ResultSet rs = Database.selectRecord("utenti", "username='"+ email + "'");
              
                int id = 0;
                while(rs.next()){
                    id = rs.getInt("id");
                }
                //metto il nuovo utente in sessione 
                SecurityLayer.createSession(request, email, id);
//                Database.close();
//                FreeMarker.process("home.html", data, response, getServletContext());
response.sendRedirect("index");
                //e lo mando alla homepage
                } else { //email non valida 
                    action_error(request, response);
                    
                }      
            } catch (SQLException ex) {
                Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
            }  catch (IOException ex) {
                Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
            }
            
         } 
           else{          
         try {
             // se l' utente è in sessione e lo mando alla homepage 
             response.sendRedirect("index");
        } catch (IOException ex) {
            Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
        }
         }//fine else
        
        
          }
     
    
private void action_error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
           Map data = new HashMap();
           HttpSession s = SecurityLayer.checkSession(request);
           if(s != null) {
              data.put("sessione", s.getAttribute("username"));
              data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
             }
        FreeMarker.process("errorpage.html", data, response, getServletContext()); 
    
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
            Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
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
