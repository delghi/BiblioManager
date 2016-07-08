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
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Luca
 */
public class Upload_pub extends HttpServlet {

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
       
       
       
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
        
        HttpSession s = SecurityLayer.checkSession(request);
        Map data = new HashMap();
        //controllo permessi utente
        data.put("tipologia", DataUtil.getTipologiaByEmail((String) s.getAttribute("username")));

        if(s != null && DataUtil.getTipologiaByEmail((String) s.getAttribute("username") ) != 0){
            try {
//                //utente attivo ok inserimento
//                data.put("sessione", s.getAttribute("username"));
//                FreeMarker.process("inser_pubb.html", data, response, getServletContext());
////                action_upload(request, response);
//            } catch (Exception ex) {
//                Logger.getLogger(Upload_pub.class.getName()).log(Level.SEVERE, null, ex);
//            }}
           if (action_upload(request)) {
                data.put("sessione", s.getAttribute("username"));
                response.sendRedirect("index");

            } else {
                data.put("sessione", s.getAttribute("username"));
                FreeMarker.process("inser_pubb.html", data, response, getServletContext());
            
                
            }
           
    }       catch (Exception ex) {
                Logger.getLogger(Upload_pub.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else action_error(request, response);
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
    
     protected boolean action_upload(HttpServletRequest request)throws FileUploadException, Exception{
         
          HttpSession s = SecurityLayer.checkSession(request);
          if (ServletFileUpload.isMultipartContent(request)) {
     
             
        //dichiaro mappe 
Map pubb = new HashMap();
Map rist = new HashMap();
Map key = new HashMap();
Map files = new HashMap();
int idristampa= 0;
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

if(name.equals("titolo")&&!name.isEmpty()||name.equals("autore")&&!name.isEmpty() || name.equals("descrizione")&&!name.isEmpty()){
pubb.put(name, value);
}else if(name.equals("isbn")&&!name.isEmpty()||name.equals("editore")&&!name.isEmpty() || name.equals("lingua")&&!name.isEmpty()|| name.equals("numpagine")&&!name.isEmpty()){
    rist.put(name, value);
}else if(name.equals("datapub")&&!name.isEmpty()){

    rist.put(name, value);
} else if(name.equals("key1") || name.equals("key2")||name.equals("key3")||name.equals("key4")){
        key.put(name, value);
} else return false;
    

} // Se si stratta invece di un file
else{
 
    // Dopo aver ripreso tutti i dati disponibili name,type,size
//String fieldName = item.getFieldName();
String fileName = item.getName();
String contentType = item.getContentType();
long sizeInBytes = item.getSize(); 
if(contentType.equals("image/jpeg")&& !fileName.isEmpty()){
//li salvo nella mappa
files.put("name", fileName);
files.put("type", contentType);
files.put("size", sizeInBytes);



//li scrivo nel db
//Database.connect();
Database.insertRecord("files", files);
//Database.close();
ResultSet rs1 = Database.selectRecord("files", "name='" + files.get("name") + "'");
    if(!isNull(rs1)){
    while(rs1.next()){
        rist.put("copertina", rs1.getInt("id"));

    }}






// Posso scriverlo direttamente su filesystem
if (true) {
File uploadedFile = new File(getServletContext().getInitParameter("uploads.directory") + fileName);
// Solo se veramente ho inviato qualcosa
if (item.getSize() > 0) {
item.write(uploadedFile);
}
}
}else if(!fileName.isEmpty()){
    files.put("name", fileName);
files.put("type", contentType);
files.put("size", sizeInBytes);

//li scrivo nel db
//Database.connect();
Database.insertRecord("files", files);
//Database.close();
  ResultSet rs4 = Database.selectRecord("files", "name='" + files.get("name") + "'");
    if(!isNull(rs4)){
    while(rs4.next()){
        
        rist.put("download", rs4.getInt("id"));
    }}
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
    }

        //inserisco dati  nel db
pubb.put("idutente", s.getAttribute("userid"));
//    Database.connect();
    //prova incremento campo inserite per utenti piu attivi
   pubb.put("idutente", s.getAttribute("userid"));
//    Database.connect();
    int userid =(int) s.getAttribute("userid");
    //prova incremento campo inserite per utenti piu attivi
    
//    inserisco parole chiave
       Database.simpleupdateRecord("utenti", "inserite=inserite+1", "id="+userid);
    
//    inserisco parole chiave
       Database.insertRecord("keyword", key);
    //seleziono id parole chiave appena inserite
     ResultSet rs2 = Database.selectRecord("keyword", "key1='" + key.get("key1")+"'&&" + "key2='"+ key.get("key2")+"'&&" + "key3='"+ key.get("key3")+"'&&" + "key4='"+ key.get("key4")+"'");
   if(!isNull(rs2)){
     while(rs2.next()){ //e inserisco id keyword nella tab pubblicazione
     pubb.put("keyword", rs2.getInt("id"));
   }}  
     //inserisco ora la pubblicazione con tutti i dati
    Database.insertRecord("pubblicazione", pubb);
    //seleziono id pubblicazione appena inserita
    ResultSet rs = Database.selectRecord("pubblicazione", "titolo='" + pubb.get("titolo") + "'");
    //e inserisco l'id nella tab ristampa e  tab files
    while(rs.next()){
        rist.put("idpub", rs.getInt("id"));
    }
    
    
    ResultSet rs3 = Database.selectRecord("ristampa", "isbn="+rist.get("isbn"));
    while(rs3.next()){
       idristampa = rs3.getInt("id");
          }
    //inserisco dati in tab ristampa
//    Database.updateRecord("ristampa", rist, "id " +idristampa);
   Database.insertRecord("ristampa", rist);
//    Database.close();
    //vado alla pagina di corretto inserimento
//    FreeMarker.process("home.html", pubb, response, getServletContext());
return true;
          }else return false;
     }
     
    
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Upload_pub.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(Upload_pub.class.getName()).log(Level.SEVERE, null, ex);
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
    }
}


