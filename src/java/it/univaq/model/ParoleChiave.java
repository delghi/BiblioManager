/*
 * Model che rappresenta le parole chiave associate a una pubblicazione
 * Assumiamo che una pubblicazione possa avere massimo quattro parole chiave associate
 * 
 */
package it.univaq.model;

/**
 *
 * @author Luca
 */
public class ParoleChiave {
    
    private int id;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String keyword4;

    public ParoleChiave(int id, String keyword1, String keyword2, String keyword3, String keyword4) {
        this.id = id;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.keyword4 = keyword4;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }

    public String getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }

    public String getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }

    public String getKeyword4() {
        return keyword4;
    }

    public void setKeyword4(String keyword4) {
        this.keyword4 = keyword4;
    }

    
    
    
    
}
