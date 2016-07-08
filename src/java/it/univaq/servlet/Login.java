/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.util.DataUtil;
import it.univaq.util.Database;
import it.univaq.util.FreeMarker;
import it.univaq.util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
public class Login extends HttpServlet {

    
    private void action_error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
           Map data = new HashMap();
           HttpSession s = SecurityLayer.checkSession(request);
           if(s != null) {
              data.put("sessione", s.getAttribute("username"));
              data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
             }
        FreeMarker.process("errorpage.html", data, response, getServletContext()); 
    
}
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession s = SecurityLayer.checkSession(request);
       //se l'utente non è in sessione 
        if (s == null){
            //prelevo user e password inseriti dall'utente
            String username = request.getParameter("email");
            String password = request.getParameter("password");
            //mappa da passare al motore di template in caso di errore 
                Map data = new HashMap();
            //verifico se username e password non sono vuoti
            if(isNull(username)|| isNull(password)) {
                //lo rimando alla pagina di login
                FreeMarker.process("login_signup.html", data, response, getServletContext());
            }
            
            try {
                //validazione identità
               //se la validazione ha successo
               //carichiamo lo userid dal database utenti
             int id_user = DataUtil.verificaUser(username, password);
             data.put("userid", id_user);
             data.put("tipologia", DataUtil.getTipologiaByEmail(username));
             

             if(id_user != 0){
             SecurityLayer.createSession(request, username, id_user);
             response.sendRedirect("index");
             } else{
                action_error(request,response);
             }
//            FreeMarker.process("home.html", data, response, getServletContext());
            } catch (Exception ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
             
            } else {
                //se l'utente  è in sessione lo mando alla home
                response.sendRedirect("home");
                
            }
    }

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
              {
        try {
            Map data= new HashMap ();
            FreeMarker.process("login_signup.html", data, response, getServletContext());
            processRequest(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
          {
        try {
            processRequest(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet per login";
    }// </editor-fold>

}
