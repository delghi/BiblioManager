/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.model.Pubblicazione;
import it.univaq.model.Ristampa;
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
public class Ricerca extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{

    HttpSession s = SecurityLayer.checkSession(request);
    if(s != null){
        //utente in sessione ok ricerca
        //creo mappa data per contenere i dati da passare a freemarker
        Map data = new HashMap();
        data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
        String order;
        //ordinamento 
             if(!isNull(request.getParameter("data"))){
                order = "datapub DESC";
            }else if(!isNull(request.getParameter("titolo"))){
                order = "titolo";
            }else order= "titolo"; //default
           data.put("ordinamento", order);
           
            data.put("sessione", s.getAttribute("username"));
            List<Pubblicazione> lista_pubblicazioni = new ArrayList<Pubblicazione>();
            int[] pagine;
            int numpagina = 0;
            int numpagine;
            int totpagine = 0;
            //prendo stringa di ricerca inserita dall'utente
            String ricerca = request.getParameter("ricerca");
            SimpleDateFormat datapubbl = new SimpleDateFormat("dd/MM/yyyy");
            
if (isNull(request.getParameter("numpagina")) && isNull(request.getParameter("totpagine")) && isNull(request.getParameter("numpagine"))) {

       
            //eseguo query per ricercare le pubblicazioni e le conto
            
            ResultSet rs2 = Database.CountSearchPub("ristampa", "pubblicazione", "ristampa.idpub=pubblicazione.id", "keyword", "pubblicazione.keyword=keyword.id", ricerca);
            while(rs2.next()){
                totpagine = rs2.getInt(1);
            }
         //divido il num totale di pubblicazioni presenti per il num di elementi che voglio visualizzare in una pagina
            numpagine = (int) Math.ceil(totpagine / 7);
            pagine = new int[numpagine];
            try {
                
                  String editore = null;
                    String data_pubblicazione = null;
                    
            
ResultSet rs = Database.SearchPub2("ristampa", "pubblicazione", "ristampa.idpub=pubblicazione.id", "keyword", "pubblicazione.keyword=keyword.id", "files", "files.id=ristampa.copertina", ricerca, order, numpagina, 6);
            while(rs.next()){
                int id = rs.getInt("idpub");
                    String titolo  = rs.getString("titolo");
                    String autore = rs.getString("autore");
                    
                     editore = rs.getString("editore");
                     data_pubblicazione = datapubbl.format(rs.getDate("datapub"));
                     String img = rs.getString("files.name");
//                    Pubblicazione pub = new Pubblicazione(id, titolo, autore);
//                    Ristampa rist = new Ristampa(editore, data_pubblicazione);
Pubblicazione pub = new Pubblicazione(id, titolo, autore, editore, data_pubblicazione, img);
                    lista_pubblicazioni.add(pub);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ricerca.class.getName()).log(Level.SEVERE, null, ex);
        }
          

}else{
    numpagina = Integer.parseInt(request.getParameter("numpagina"));
                numpagine = Integer.parseInt(request.getParameter("numpagine"));
                pagine = new int [numpagine];
                
//ResultSet rs3 = Database.SearchPub("ristampa", "pubblicazione", "ristampa.idpub=pubblicazione.id", "keyword", "pubblicazione.keyword=keyword.id", ricerca , order, numpagina * 6, 6);
ResultSet rs3 = Database.SearchPub2("ristampa", "pubblicazione", "ristampa.idpub=pubblicazione.id", "keyword", "pubblicazione.keyword=keyword.id", "files", "files.id=ristampa.copertina", ricerca, order, numpagina * 6, 6);

                while(rs3.next()){
                    int id = rs3.getInt("idpub");
                    String titolo  = rs3.getString("titolo");
                    String autore = rs3.getString("autore");
                    String editore = rs3.getString("editore");
                    String data_pubblicazione = datapubbl.format(rs3.getDate("datapub"));
                    String img = rs3.getString("files.name");
                Pubblicazione pub = new Pubblicazione(id, titolo, autore, editore, data_pubblicazione, img);
                    lista_pubblicazioni.add(pub);
                
} 
}         
     
  
           data.put("lista_pubblicazioni", lista_pubblicazioni);
           data.put("numpagine", numpagine);
           data.put("pagine", pagine);
           data.put("numpagina", numpagina);
          FreeMarker.process("catalogo.html", data, response, getServletContext());
    }
    else response.sendRedirect("login");
        
        
        
        
        
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
            Logger.getLogger(Ricerca.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Ricerca.class.getName()).log(Level.SEVERE, null, ex);
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
