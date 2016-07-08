/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.model;

/**
 *
 * @author Luca
 */
public class Recensione {
    
    private int id;
    private String titolo;
    private String testo; 
    private String utente;
    private int voto;
    private int id_pub;
    private int pubblicata;

    public Recensione(int id, String titolo, String testo, String utente, int voto, int id_pub, int pubblicata) {
        this.id = id;
        this.titolo = titolo;
        this.testo = testo;
        this.utente = utente;
        this.voto = voto;
        this.id_pub = id_pub;
        this.pubblicata = pubblicata;
    }

    public Recensione(int userid, String titolo, String testo, int voto, int idpub) {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public int getId_pub() {
        return id_pub;
    }

    public void setId_pub(int id_pub) {
        this.id_pub = id_pub;
    }

    public int getPubblicata() {
        return pubblicata;
    }

    public void setPubblicata(int pubblicata) {
        this.pubblicata = pubblicata;
    }
    
    
    
}
