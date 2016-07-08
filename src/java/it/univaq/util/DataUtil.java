/**
 * 
 */
package it.univaq.util;



import it.univaq.model.Pubblicazione;
import it.univaq.model.Utente;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.apache.commons.validator.EmailValidator;
/**
 * @author Luca
 *
 */
public class DataUtil {
	
	
    
    public static String getUserbyId(int id){
        String username = null;
        try {
           ResultSet rs= Database.selectRecord("utenti", "id="+id);
            while(rs.next()){
                username = rs.getString("username");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            return username;
        }
        
        
    public static boolean checkEmail(String email){
        
        //controllo validità email 
        
        if((EmailValidator.getInstance().isValid(email))) return true;
        else return false;
        
        
    }
    
  
        
        
	public static String getUsername(String email) throws Exception {
		
		String nome = null;
		
		try{

		     
		         String condition="email='"+email+"'";
		         
		         ResultSet rs =Database.selectRecord("users",condition);
		       
		         while(rs.next()){ 
		          nome= rs.getString("nome");
		        	 
		       }
		                   
		      } catch (SQLException e) {
		      }
		        return nome;  
	}
        
        
        //Metodo che data email(username) restituisce la tipologia ---->0 o 1
        public static int getTipologiaByEmail (String email) {
             
            int tipologia = 0;
            try {
                
                String condition = "username='"+email+"'";
                
                ResultSet rs = Database.selectRecord("utenti", condition);
                
                while (rs.next()){
                    tipologia = rs.getInt("tipologia");
                }
                
                
            
            } catch (Exception ex) {
                Logger.getLogger(DataUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return tipologia;
        }
        
        
        
        
        
    public static int verificaUser(String email,String password) throws Exception 
     {
      int id = 0;
      try{

        if(!isNull(password)){
            password=crypt(password);
        }
       
         String condition = "username='"+email+"' AND password='"+password+"'";
         
         ResultSet rs = Database.selectRecord("utenti",condition);
       while(rs.next()){ 
           id=rs.getInt("id");
       }
        
      } catch (SQLException e) {
      }
        return id;    
    }   
  
    
   
    
   //metodo per criptare una stringa (password)
    public static String crypt(String string){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = string.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }

    }
    
    
    public static boolean decrypt(String string_crypted, String to_check){
        if(to_check == null || string_crypted == null) return false;
        return string_crypted.equals(crypt(to_check));
    }
     public static String dateToString(Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
     public static Date stringToDate(String date, String format) throws ParseException{
        DateFormat formatter = new SimpleDateFormat(format);
        java.util.Date myDate;

        myDate = formatter.parse(date);
        Date sqlDate = new Date(myDate.getTime());
        return sqlDate;
        
    }
}
