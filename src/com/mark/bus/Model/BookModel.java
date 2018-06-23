package com.mark.bus.Model;

public class BookModel {

    private Integer aantal;
    private String titel,auteur,uitgeverij;

    private static Long isbn;

    private String boek;

    private int id;

    public BookModel(int id, Long isbn, String titel, String auteur, String uitgeverij, Integer boek, Integer aantal) {
        this.id = id;
        this.titel = titel;
        this.auteur = auteur;
        this.uitgeverij = uitgeverij;
        this.isbn = isbn;
        if (boek == 1){
            this.boek = "E-boek";
        }else{
            this.boek = "Boek";
        }
        this.aantal = aantal;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getUitgeverij() {
        return uitgeverij;
    }

    public void setUitgeverij(String uitgeverij) {
        this.uitgeverij = uitgeverij;
    }

    public static Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getBoek() {

        if (boek.equals("E-boek")){
            return "E-boek";
        }else{
            return "Boek";
        }
    }

    public void setId(Integer auteur) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Integer getAantal() {
        return aantal;
    }

    public void setAantal(Integer aantal) {
        this.aantal = aantal;
    }

}
