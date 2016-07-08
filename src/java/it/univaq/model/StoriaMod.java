/*
 * Model che rappresenta l'oggetto che ha il compito di tenere traccia delle modifiche apportate ad 
 * una pubblicazione. 
 */
package it.univaq.model;

import java.sql.Date;

/**
 *
 * @author Luca
 */
public class StoriaMod {
    
        private int id;
        private String timestamp;
        private int id_utente;
        private String descrizione;
        private int id_pubb;
        private String nome;

    public StoriaMod(int id, String timestamp, int id_utente, String descrizione, int id_pubb) {
        this.id = id;
        this.timestamp = timestamp;
        this.id_utente = id_utente;
        this.descrizione = descrizione;
        this.id_pubb = id_pubb;
    }
    
    public StoriaMod( String timestamp, String nome, String descrizione) {
        this.timestamp = timestamp;
        this.nome = nome;
        this.descrizione = descrizione;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getId_utente() {
        return id_utente;
    }

    public void setId_utente(int id_utente) {
        this.id_utente = id_utente;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getId_pubb() {
        return id_pubb;
    }

    public void setId_pubb(int id_pubb) {
        this.id_pubb = id_pubb;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
        
        
        
}
