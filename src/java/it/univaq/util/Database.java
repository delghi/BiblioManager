package it.univaq.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Database {
	

	private static Connection db;
    /**
     * Connessione al database
     * @throws Exception 
     */
 
    
    	public static void connect() throws NamingException, SQLException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/biblioteca");
        Database.db = ds.getConnection();
    }
    	
    	
    /**
     * Chiusura connessione al database
     * @throws java.sql.SQLException
     */
    public static void close() throws SQLException{
        Database.db.close();
    }
    
    
    
    public static ResultSet selectAllRecord(String table) throws SQLException {
        // Generazione query
        String query = "SELECT * FROM " + table ;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
    
    
    
    
    public static ResultSet selectRecord(String table, String condition) throws SQLException {
        // Generazione query
        String query = "SELECT * FROM " + table + " WHERE " + condition;
        // Esecuzione query
        return Database.executeQuery(query);
    }
 
    public static ResultSet selectRecord(String table, String condition, String order) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table + " WHERE " + condition + " ORDER BY " + order;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
    
    
    public static ResultSet selectRecordU(String table, String order, int limit) throws SQLException{
        
        String query = "SELECT * FROM " + table + " ORDER BY " + order + " DESC LIMIT " + limit;
        return Database.executeQuery(query);
    }
    
    
    
    

    public static ResultSet selectJoin(String table_1, String table_2, String join_condition, String where_condition) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " WHERE " + where_condition;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
//    SELECT * FROM pubblicazione JOIN ristampa ON pubblicazione.id=ristampa.idpub JOIN files ON ristampa.copertina=files.id WHERE pubblicazione.id=47
     public static ResultSet selectJoinDet(String table_1, String table_2, String join_condition, String table_3, String join_condition2,  String where_condition) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " JOIN " + table_3 + " ON " + join_condition2 + " WHERE " + where_condition;
        // Esecuzione query
        return Database.executeQuery(query);
    }
  
     
        public static ResultSet selectJoinMod(String table_1, String table_2, String join_condition, String table_3, String join_condition2, String table_4, String join_condition3, String order , int limit) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " JOIN " + table_3 + " ON " + join_condition2 + " JOIN " + table_4 + " ON " + join_condition3 + " ORDER BY " + order + " LIMIT " + limit; 
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
        
   
        
        
     public static ResultSet selectJoin(String table_1, String table_2, String where_condition) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " WHERE " + where_condition;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
     
     
     
     
    /**
     * Select record con join tra due tabelle e ordinamento
     * @param table_1           nome della prima tabella
     * @param table_2           nome della seconda tabella
     * @param join_condition    condizione del join tra la tabelle
     * @param group
     * @param where_condition   condizione per il filtro dei dati
     * @param order             ordinamento dei dati
     * @return                  dati prelevati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectJoin(String table_1, String table_2, String join_condition, String group, String order, int limit) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " GROUP BY " + group + " ORDER BY " + order + " LIMIT " + limit;
        // Esecuzione query
        return Database.executeQuery(query);
    }
//     SELECT * FROM pubblicazione JOIN ristampa ON pubblicazione.id=ristampa.idpub JOIN files ON ristampa.download=files.id
    public static ResultSet selectJoin3(String table_1, String table_2, String join_condition, String table_3, String join_condition2,  String group, String order, int limit) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " JOIN " + table_3 + " ON " + join_condition2 + " GROUP BY " + group + " ORDER BY " + order  + " LIMIT " + limit;
        // Esecuzione query
        return Database.executeQuery(query);
    }
  
   
      public static ResultSet selectJoinPag(String table_1, String table_2, String join_condition, String order, int limit1, int limit2) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " ORDER BY " + order + " LIMIT " + limit1 + " , " + limit2;
        // Esecuzione query
        return Database.executeQuery(query);
    }
 
      
      public static ResultSet selectJoinCat(String table_1, String table_2, String join_condition, String table_3, String join_condition2,  String group, String order, int limit1, int limit2) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " JOIN " + table_3 + " ON " + join_condition2 + " GROUP BY " + group + " ORDER BY " + order  + " LIMIT " + limit1 + " , " + limit2;
        // Esecuzione query
        return Database.executeQuery(query);
    }
  
      
      
      
      
      
      
      
     
 
    
   public static ResultSet SearchPub(String table_1, String table_2, String join_condition1, String table_3, String join_condition2, String ricerca, String order, int limit1, int limit2) throws SQLException{
  
       String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition1 + " JOIN " + table_3 + " ON " + join_condition2 + " WHERE titolo LIKE '% " + ricerca + "%' || isbn LIKE '%" + ricerca + "%' || autore LIKE '%" + ricerca + "%' "
                + "|| editore LIKE '%" + ricerca + "%' || key1 LIKE '%" + ricerca + "%' || key2 LIKE '%" + ricerca + "%' || key3 LIKE '%" + ricerca + "%' || key4 LIKE '%" + ricerca + "%' " + " ORDER BY " + order + " LIMIT " + limit1 + "," + limit2;
       
       return Database.executeQuery(query);
       
   }
   
   
   public static ResultSet SearchPub2(String table_1, String table_2, String join_condition1, String table_3, String join_condition2, String table_4, String join_condition3, String ricerca, String order, int limit1, int limit2) throws SQLException{
  
       String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition1 + " JOIN " + table_3 + " ON " + join_condition2 + " JOIN " + table_4 + " ON " + join_condition3 +  " WHERE titolo LIKE '% " + ricerca + "%' || isbn LIKE '%" + ricerca + "%' || autore LIKE '%" + ricerca + "%' "
                + "|| editore LIKE '%" + ricerca + "%' || key1 LIKE '%" + ricerca + "%' || key2 LIKE '%" + ricerca + "%' || key3 LIKE '%" + ricerca + "%' || key4 LIKE '%" + ricerca + "%' " + " ORDER BY " + order + " LIMIT " + limit1 + "," + limit2;
       
       return Database.executeQuery(query);
       
   }
   
   
   
   
   
   
    
    
    public static ResultSet CountSearchPub(String table_1, String table_2, String join_condition1, String table_3, String join_condition2, String ricerca) throws SQLException{
  
       String query = "SELECT COUNT(*) FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition1 + " JOIN " + table_3 + " ON " + join_condition2 + " WHERE titolo LIKE '% " + ricerca + "%' || isbn LIKE '%" + ricerca + "%' || autore LIKE '%" + ricerca + "%' "
                + "|| editore LIKE '%" + ricerca + "%' || key1 LIKE '%" + ricerca + "%' || key2 LIKE '%" + ricerca + "%' || key3 LIKE '%" + ricerca + "%' || key4 LIKE '%" + ricerca + "%' ";
       
       return Database.executeQuery(query);
       
   }
    
    
    
    // fine provaaaa
    /**
     * Update record
     * @param table         tabella in cui aggiornare i dati
     * @param data          dati da inserire
     * @param condition     condizione per il filtro dei dati
     * @return              true se l'inserimento è andato a buon fine, false altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean updateRecord(String table, Map<String,Object> data, String condition) throws SQLException{
        // Generazione query
        String query = "UPDATE " + table + " SET ";
        Object value;
        String attr;
        
        for(Map.Entry<String,Object> e:data.entrySet()){
            attr = e.getKey();
            value = e.getValue();
            if(value instanceof String){
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            }else{
                query = query + attr + " = " + value + ", ";
            }
            
            
        }
        query = query.substring(0, query.length()-2) + " WHERE " + condition;
        
        // Esecuzione query
        return Database.updateQuery(query);
    }
    
    
    public static boolean simpleupdateRecord(String table, String set, String condition) throws SQLException{
        
        String query = "UPDATE " + table + " SET " + set + " WHERE " + condition;
        
        return Database.updateQuery(query);
        
    }
    
    
    
    
    
    
    /**
     * Delete record
     * @param table         tabella in cui eliminare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              true se l'eliminazione è andata a buon fine, false altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean deleteRecord(String table, String condition) throws SQLException{
        // Generazione query
        String query = "DELETE FROM " + table + " WHERE " + condition;
        // Esecuzione query
        return Database.updateQuery(query);
    }
    
    /**
     * Count record
     * @param table         tabella in cui contare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              numero dei record se la query è stata eseguita on successo, -1 altrimenti
     * @throws java.sql.SQLException
     */
    public static int countRecord(String table, String condition) throws SQLException{

        // Generazione query
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + condition;
        // Esecuzione query
        ResultSet record = Database.executeQuery(query);
        record.next();
        // Restituzione del risultato
        return record.getInt(1);

    }
    
    
    
    
    
    /**
     * Imposta a NULL un attributo di una tabella  
     * @param table         tabella in cui è presente l'attributo
     * @param attribute     attributo da impostare a NULL
     * @param condition     condizione
     * @return
     * @throws java.sql.SQLException
     */
    public static boolean resetAttribute(String table, String attribute, String condition) throws SQLException{
        String query = "UPDATE " + table + " SET " + attribute + " = NULL WHERE " + condition;
        return Database.updateQuery(query);
    }

    // <editor-fold defaultstate="collapsed" desc="Metodi ausiliari.">
    
    /**
     * executeQuery personalizzata
     * @param query query da eseguire
     */
    private static ResultSet executeQuery(String query) throws SQLException{
        Statement s1 = Database.db.createStatement();
        ResultSet records = s1.executeQuery(query);

        return records; 
            
    }
    
    /**
     * updateQuery personalizzata
     * @param query query da eseguire
     */
    private static boolean updateQuery(String query) throws SQLException{
        
        Statement s1;
        
        s1 = Database.db.createStatement();
        s1.executeUpdate(query); 
        s1.close();
        return true; 

    }
   // </editor-fold>
     public static boolean insertRecord(String table, Map<String, Object> data) throws SQLException {
        // Generazione query
        String query = "INSERT INTO " + table + " SET ";
        Object value;
        String attr;

        for (Map.Entry<String, Object> e : data.entrySet()) {
            attr = e.getKey();
            value = e.getValue();
            if (value instanceof Integer) {
                query = query + attr + " = " + value + ", ";
            } else {
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            }
        }
        query = query.substring(0, query.length() - 2);
        // Esecuzione query
        return Database.updateQuery(query);
    }
}