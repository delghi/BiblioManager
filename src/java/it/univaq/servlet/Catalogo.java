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
public class Catalogo extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        //controllo sessione
        HttpSession s = SecurityLayer.checkSession(request);
        //creo mappa data per contenere i dati da passare a freemarker
        Map data = new HashMap();
        data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
        String order;
        //se l'utente è in sessione
        if(s != null){
            //ordinamento 
           if(!isNull(request.getParameter("data"))){
                order = "datapub";
            }else if(!isNull(request.getParameter("titolo"))){
                order = "titolo";
            }else order= "titolo"; //default
           data.put("ordinamento", order);
           
            data.put("sessione", s.getAttribute("username"));
            List<Pubblicazione> lista_pubblicazioni = new ArrayList<Pubblicazione>();
            int[] pagine = null;
            int numpagina = 0;
            int numpagine = 0;
            int totpagine = 0;
            
                //se mi trovo nella prima pagina
            if (isNull(request.getParameter("numpagina")) && isNull(request.getParameter("totpagine")) && isNull(request.getParameter("numpagine"))) {
            try {
                 totpagine = Database.countRecord("pubblicazione", "id");
            } catch (SQLException ex) {
                Logger.getLogger(Catalogo.class.getName()).log(Level.SEVERE, null, ex);
            }
            // numero di pagine per paginazione = pagine_totali/elementi_da_visualizzare_in_ogni_pagina 
            numpagine = (int) Math.ceil(totpagine / 7);
            pagine = new int[numpagine];
            try {
                SimpleDateFormat datapubbl = new SimpleDateFormat("dd/MM/yyyy");
                  String editore = null;
                    String data_pubblicazione = null;
                    
                //
                 ResultSet rs2 = Database.selectJoinCat("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", "files", "files.id=ristampa.copertina", "pubblicazione.id", order, numpagina, 6);
//                ResultSet rs2 = Database.selectJoinPag("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", order, numpagina , 6);
                while(rs2.next()){
                    int id = rs2.getInt("idpub");
                    String titolo  = rs2.getString("titolo");
                    String autore = rs2.getString("autore");
                    
                     editore = rs2.getString("editore");
                     data_pubblicazione = datapubbl.format(rs2.getDate("datapub"));
                     String descrizione = rs2.getString("files.name");
//                    Pubblicazione pub = new Pubblicazione(id, titolo, autore);
//                    Ristampa rist = new Ristampa(editore, data_pubblicazione);
Pubblicazione pub = new Pubblicazione(id, titolo, autore, editore, data_pubblicazione, descrizione);
                    lista_pubblicazioni.add(pub);
//                    lista_ristampe.add(rist);
                   
                } 
            }catch (SQLException ex) {
                Logger.getLogger(Catalogo.class.getName()).log(Level.SEVERE, null, ex);
            }
            //se mi trovo ad una pagina diversa dalla prima
            }else {
                numpagina = Integer.parseInt(request.getParameter("numpagina"));
                numpagine = Integer.parseInt(request.getParameter("numpagine"));
                pagine = new int [numpagine];
//                ResultSet rs3 = Database.selectJoinPag("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", order, numpagina * 6 , 6 );
                ResultSet rs3 = Database.selectJoinCat("pubblicazione", "ristampa", "pubblicazione.id=ristampa.idpub", "files", "files.id=ristampa.copertina", "pubblicazione.id", order, numpagina * 6, 6);
                SimpleDateFormat datapubbl = new SimpleDateFormat("dd/MM/yyyy");
                while(rs3.next()){
                    int id = rs3.getInt("idpub");
                    String titolo  = rs3.getString("titolo");
                    String autore = rs3.getString("autore");
                    String editore = rs3.getString("editore");
                    String data_pubblicazione = datapubbl.format(rs3.getDate("datapub"));
                    String descrizione = rs3.getString("files.name");
                Pubblicazione pub = new Pubblicazione(id, titolo, autore, editore, data_pubblicazione, descrizione);
                    lista_pubblicazioni.add(pub);
                
                }
                
                
            }
            
                     data.put("lista_pubblicazioni", lista_pubblicazioni);
                      data.put("numpagine", numpagine);
                      data.put("pagine", pagine);
                      data.put("numpagina", numpagina);
                    FreeMarker.process("catalogo.html", data, response, getServletContext());
                        
            
           
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
            Logger.getLogger(Catalogo.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Catalogo.class.getName()).log(Level.SEVERE, null, ex);
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
