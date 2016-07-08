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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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
public class Upload_rist extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
         HttpSession s = SecurityLayer.checkSession(request);
        //controllo permessi utente
        if(s != null && DataUtil.getTipologiaByEmail((String) s.getAttribute("username") ) != 0){
              //utente attivo ok modifica
            Map data = new HashMap();
            data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));
            int idpubb = Integer.parseInt(request.getParameter("id"));

            data.put("idpubb", idpubb);
            try {
                if(action_upload(request)){
                data.put("sessione", s.getAttribute("username"));
                response.sendRedirect("catalogo");
                }else{
//                     data.put("sessione", s.getAttribute("username"));
//                     FreeMarker.process("insert_rist.html", data, response, getServletContext());
                action_error(request, response);
                }
                    
              
            } catch (Exception ex) {
                Logger.getLogger(Upload_pub.class.getName()).log(Level.SEVERE, null, ex);
            }}
    }

    
    protected boolean action_upload(HttpServletRequest request)throws FileUploadException, Exception{
         
          HttpSession s = SecurityLayer.checkSession(request);
        //dichiaro mappe 
Map rist = new HashMap();
//prendo id pubblicazione dalla request

if (ServletFileUpload.isMultipartContent(request)) {
Map files = new HashMap();
int idpubb = Integer.parseInt(request.getParameter("id"));
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

 if(name.equals("isbn")||name.equals("editore") || name.equals("lingua")||name.equals("numpagine")||name.equals("datapub")){
    rist.put(name, value);
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
//prendo id del file se è stato inserito
ResultSet rs1 = Database.selectRecord("files", "name='" + files.get("name") + "'");
    if(!isNull(rs1)){
    while(rs1.next()){
        rist.put("download", rs1.getInt("id"));
    }}



}
     
    }

    
    rist.put("idpub", idpubb);
    //inserisco dati in tab ristampa
    Database.insertRecord("ristampa", rist);

         
         return true;
     }else return false;
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
