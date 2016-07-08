/*
 * Model della ristampa
 * Ogni pubblicazione ha ALMENO una ristampa
 * Contiene metadati di una pubblicazione 
 */
package it.univaq.model;

/**
 *
 * @author Luca
 */
public class Ristampa {
    
    private long isbn;
    private int num_pagine;
    private String editore;
    private String lingua;
    private String data_pubblicazione;
    private int pubb;
    private String data_insert;
    private int download;

    public Ristampa(long isbn, int num_pagine, String lingua, String editore, String data_pubblicazione, int pubb, String data_insert, int download) {
        this.isbn = isbn;
        this.num_pagine = num_pagine;
        this.lingua = lingua;
        this.editore = editore;
        this.data_pubblicazione = data_pubblicazione;
        this.pubb = pubb;
        this.data_insert = data_insert;
        this.download = download;
    }

    public Ristampa(long isbn, int num_pagine, String lingua, String data_pubblicazione, int pubb) {
        this.isbn = isbn;
        this.num_pagine = num_pagine;
        this.lingua = lingua;
        this.data_pubblicazione = data_pubblicazione;
        this.pubb = pubb;
    }
     public Ristampa(long isbn, int num_pagine, String lingua, String editore, String data_pubblicazione) {
        this.isbn = isbn;
        this.num_pagine = num_pagine;
        this.lingua = lingua;
        this.editore = editore;
        this.data_pubblicazione = data_pubblicazione;
       
    }

    public Ristampa(String editore, String data_pubblicazione) {
        this.editore = editore;
        this.data_pubblicazione = data_pubblicazione;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public int getNum_pagine() {
        return num_pagine;
    }

    public void setNum_pagine(int num_pagine) {
        this.num_pagine = num_pagine;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public String getData_pubblicazione() {
        return data_pubblicazione;
    }

    public void setData_pubblicazione(String data_pubblicazione) {
        this.data_pubblicazione = data_pubblicazione;
    }

    public int getPubb() {
        return pubb;
    }

    public void setPubb(int pubb) {
        this.pubb = pubb;
    }

    public String getData_insert() {
        return data_insert;
    }

    public void setData_insert(String data_insert) {
        this.data_insert = data_insert;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public String getEditore() {
        return editore;
    }

    public void setEditore(String editore) {
        this.editore = editore;
    }
    
    
    
    
}
