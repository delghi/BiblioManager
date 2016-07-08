/*
MODEL OGGETTO UTENTE
 */
package it.univaq.model;

import java.sql.Date;

/**
 *
 * @author Luca
 */
public class Utente {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private Date nascita;
    private int id_tipo; //tipologia utente

    public Utente(int id, String nome, String cognome, String email, Date nascita, int id_tipo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.nascita = nascita;
        this.id_tipo = id_tipo;
    }

    public Utente(int id, String nome, String cognome, String email, int id_tipo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        
        this.id_tipo = id_tipo;
    }
    
    
    
    public Utente() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Utente(String email, String nome, String cognome, int id_tipo) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.id_tipo = id_tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getNascita() {
        return nascita;
    }

    public void setNascita(Date nascita) {
        this.nascita = nascita;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }
    
    
}
