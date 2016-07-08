

//MODEL DELL'OGGETTO PUBBLICAZIONE
package it.univaq.model;

/**
 *
 * @author Luca
 */
public class Pubblicazione {
    
    private int id;
    private String titolo;
    private String autore;
    
    private String descrizione;
    private int id_keyword;
    private String editore;
    private String data_pubblicazione;
    private String download;
    private String utente;

    public Pubblicazione(int id, String titolo, String autore, String descrizione, int id_keyword) {
        this.id = id;
        this.titolo = titolo;
        this.autore = autore;
     
        this.descrizione = descrizione;
        this.id_keyword = id_keyword;
    }

    public Pubblicazione(int id, String titolo, String autore, String descrizione) {
        this.id = id;
        this.titolo = titolo;
        this.autore = autore;
        this.descrizione = descrizione;
    }

    public Pubblicazione() {
    }
    //costruttore per ultime modificate
    public Pubblicazione(int id, String titolo) {
       this.id = id;
       this.titolo = titolo;
    }

    public Pubblicazione(int id, String titolo, String autore) {
        this.id = id;
        this.titolo = titolo;
        this.autore = autore;
    }

    public Pubblicazione(int id, String titolo, String autore, String editore, String data_pubblicazione, String descrizione) {
       this.id = id;
       this.titolo=titolo;
       this.autore=autore;
       this.editore = editore;
       this.data_pubblicazione = data_pubblicazione;
       this.descrizione = descrizione;
    }

    public Pubblicazione(int id, String titolo, String autore, String editore, String data_pubblicazione, String descrizione, String utente) {
       
       this.id = id;
       this.titolo=titolo;
       this.autore=autore;
       this.data_pubblicazione = data_pubblicazione;
       this.descrizione = descrizione;
       this.utente = utente;
    
    }

//    public Pubblicazione(int id, String titolo, String autore, String download) {
//        this.id = id;
//        this.titolo = titolo;
//        this.autore = autore;
//        this.download = download;
//    }

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

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

  
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getId_keyword() {
        return id_keyword;
    }

    public void setId_keyword(int id_keyword) {
        this.id_keyword = id_keyword;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }

    public String getData_pubblicazione() {
        return data_pubblicazione;
    }

    public void setData_pubblicazione(String data_pubblicazione) {
        this.data_pubblicazione = data_pubblicazione;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }
    
    
    

}
