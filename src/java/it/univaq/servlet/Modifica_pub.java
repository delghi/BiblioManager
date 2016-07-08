/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.servlet;

import it.univaq.model.ParoleChiave;
import it.univaq.model.Pubblicazione;
import it.univaq.model.Ristampa;
import it.univaq.util.DataUtil;
import it.univaq.util.Database;
import it.univaq.util.FreeMarker;
import it.univaq.util.SecurityLayer;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Luca
 */
public class Modifica_pub extends HttpServlet {

    
       private void action_error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
           Map data = new HashMap();
           HttpSession s = SecurityLayer.checkSession(request);
           if(s != null) {
              data.put("sessione", s.getAttribute("username"));
              data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
             }
        FreeMarker.process("errorpage.html", data, response, getServletContext()); 
    
}
       
       
 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            Map data = new HashMap();
            int id=0;
            
            data.put("tipologia",DataUtil.getTipologiaByEmail((String) s.getAttribute("email")));
           if(s != null && DataUtil.getTipologiaByEmail((String) s.getAttribute("username") ) != 0){
            if (action_upload(request)) {
                data.put("sessione", s.getAttribute("username"));
                response.sendRedirect("catalogo");

            } else {
                data.put("sessione", s.getAttribute("username"));
                //prendo tutti i dati di una pubblicazione per permettere la  modifica
                id= Integer.parseInt(request.getParameter("id"));
                request.setAttribute("id", id);
                ResultSet rs = Database.selectRecord("pubblicazione", "id="+id);
                String titolo = null, autore = null, descrizione = null;
                int keyword = 0;
                while(rs.next()){
                    titolo = rs.getString("titolo");
                    autore = rs.getString("autore");
                    descrizione = rs.getString("descrizione");
                    keyword = rs.getInt("keyword");
                    
                }
                Pubblicazione pub = new Pubblicazione(id, titolo, autore, descrizione, keyword);
                data.put("pubblicazione", pub);
                
                //prendo tutti dati ristampa
                
                ResultSet rs2 = Database.selectRecord("ristampa", "idpub="+id);
                long isbn = 0;
                int numpagine = 0;
                String editore = null, lingua = null;
                String datapub = null;
                while(rs2.next()){
                    isbn = rs2.getLong("isbn");
                    numpagine = rs2.getInt("numpagine");
                    editore = rs2.getString("editore");
                    lingua = rs2.getString("lingua");
                    datapub = DataUtil.dateToString(rs2.getDate("datapub"));
                }
                Ristampa rist = new Ristampa(isbn, numpagine, lingua, editore , datapub);
                data.put("ristampa", rist);
                
                //prendo keywords
                
                ResultSet rs3 = Database.selectRecord("keyword", "id="+keyword);
                String key1 = null, key2 = null, key3 = null, key4 = null;
                while(rs3.next()){
                    key1 = rs3.getString("key1");
                    key2 = rs3.getString("key2");
                    key3 = rs3.getString("key3");
                    key4 = rs3.getString("key4");
                }
                
                ParoleChiave keywords = new ParoleChiave(keyword, key1, key2, key3, key4);
                data.put("keyword", keywords);
                
                
                FreeMarker.process("modifica_pub.html", data, response, getServletContext());
                
            
            }
        }else action_error(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Modifica_pub.class.getName()).log(Level.SEVERE, null, ex);
        
    }}

    
    
     protected boolean action_upload(HttpServletRequest request)throws FileUploadException, Exception{
                HttpSession s = SecurityLayer.checkSession(request);
                        //dichiaro mappe 
Map pubb = new HashMap();
Map rist = new HashMap();
Map key = new HashMap();
Map files = new HashMap();
Map modifica = new HashMap();
                
                

int id = Integer.parseInt(request.getParameter("id"));

if (ServletFileUpload.isMultipartContent(request)) {
      

//La dimensione massima di ogni singolo file su system
int dimensioneMassimaDelFileScrivibieSulFileSystemInByte = 10 * 1024 * 1024; // 10 MB
//Dimensione massima della request
int dimensioneMassimaDellaRequestInByte = 20 * 1024 * 1024; // 20 MB

// Creo un factory per l'accesso al filesystem
DiskFileItemFactory factory = new DiskFileItemFactory(); 
 
//Setto la dimensione massima di ogni file, opzionale
factory.setSizeThreshold(dimensioneMassimaDelFileScrivibieSulFileSystemInByte);
 
 
// Istanzio la classe per l'upload
ServletFileUpload upload = new ServletFileUpload(factory); 
 
// Setto la dimensione massima della request
upload.setSizeMax(dimensioneMassimaDellaRequestInByte); 
 
// Parso la riquest della servlet, mi viene ritornata una lista di FileItem con
// tutti i field sia di tipo file che gli altri
List <FileItem> items = upload.parseRequest(request); 
 
/*
* La classe usata non permette di riprendere i singoli campi per
* nome quindi dovremmo scorrere la lista che ci viene ritornata con
* il metodo parserequest
*/
//scorro per tutti i campi inviati
for (int i = 0; i < items.size(); i++) {
FileItem item = items.get(i); 
// Controllo se si tratta di un campo di input normale
if (item.isFormField()) {
// Prendo solo il nome e il valore
String name = item.getFieldName();
String value = item.getString(); 

if(name.equals("titolo")||name.equals("autore") || name.equals("descrizione")){
pubb.put(name, value);
}else if(name.equals("isbn")||name.equals("editore") || name.equals("lingua")|| name.equals("numpagine")|| name.equals("datapubbl")){
    rist.put(name, value);
} else if(name.equals("key1") || name.equals("key2")||name.equals("key3")||name.equals("key4")){
        key.put(name, value);
} else if(name.equals("descrizionemod")){
    modifica.put(name, value);
}
    

} // Se si stratta invece di un file
else{
    // Dopo aver ripreso tutti i dati disponibili name,type,size
//String fieldName = item.getFieldName();
String fileName = item.getName();
String contentType = item.getContentType();
long sizeInBytes = item.getSize(); 
//li salvo nella mappa
files.put("name", fileName);
files.put("type", contentType);
files.put("size", sizeInBytes);
//li scrivo nel db
//Database.connect();
Database.insertRecord("files", files);
//Database.close();

// Posso scriverlo direttamente su filesystem
if (true) {
File uploadedFile = new File(getServletContext().getInitParameter("uploads.directory") + fileName);
// Solo se veramente ho inviato qualcosa
if (item.getSize() > 0) {
item.write(uploadedFile);
}
}
}
     
    }
     
pubb.put("idutente", s.getAttribute("userid"));
 modifica.put("userid", s.getAttribute("userid"));
 modifica.put("idpubb", id);
 

    
   
   
    try{
//    if(Database.updateRecord("keyword", key, "id="+id)){
    
     //aggiorno ora la pubblicazione con tutti i dati
    Database.updateRecord("keyword", key, "id="+id);
    Database.updateRecord("pubblicazione", pubb, "id="+id);
    Database.updateRecord("ristampa", rist, "idpub="+id);
    Database.insertRecord("storia", modifica);
    
    
//    //vado alla pagina di corretto inserimento
 
         
         return true;
                } catch (SQLException ex) {
                Logger.getLogger(Modifica_pub.class.getName()).log(Level.SEVERE, null, ex);}

                
                
                }else return false;
    return false;
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
