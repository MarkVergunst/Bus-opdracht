package com.mark.bus.Model;

import java.util.Date;

public class ReserveerModel {

    private String titel,auteur,uitgever;
    private Long isbn;
    private Date van,tot;
    private String boek;
    private static int id;
    private Long dagen;
    private double bedrag;

    public ReserveerModel(String titel, String auteur, String uitgever, Long isbn, Date van, Date tot, Integer boek,Integer boek_id) {
        this.titel = titel;
        this.auteur = auteur;
        this.uitgever = uitgever;
        this.isbn = isbn;
        this.van = van;
        this.tot = tot;
        if (boek == 1){
            this.boek = "E-boek";
        }else{
            this.boek = "Boek";
        }
        this.id = boek_id;
    }

    public ReserveerModel(Integer id,String titel, String auteur, String uitgever, Long isbn, Integer boek, Integer boek_id, long aantalDagen, double bedrag) {
        ReserveerModel.id = id;
        this.titel = titel;
        this.auteur = auteur;
        this.uitgever = uitgever;
        this.isbn = isbn;
        if (boek == 1){
            this.boek = "E-boek";
        }else{
            this.boek = "Boek";
        }
        this.id = boek_id;
        this.dagen = aantalDagen;
        this.bedrag = bedrag;
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

    public String getUitgever() {
        return uitgever;
    }

    public void setUitgever(String uitgever) {
        this.uitgever = uitgever;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public Date getVan() {
        return van;
    }

    public void setVan(Date van) {
        this.van = van;
    }

    public Date getTot() {
        return tot;
    }

    public void setTot(Date tot) {
        this.tot = tot;
    }

    public String getBoek() {
        return boek;
    }

    public void setBoek(String boek) {
        this.boek = boek;
    }

    public void setId(int id) {
        ReserveerModel.id = id;
    }

    public Long getDagen() {
        return dagen;
    }

    public void setDagen(Long dagen) {
        this.dagen = dagen;
    }

    public double getBedrag() {
        return bedrag;
    }

    public void setBedrag(double bedrag) {
        this.bedrag = bedrag;
    }

    public static Integer getId() {
        return id;
    }

    public void setId(Integer boek_id){
        id = boek_id;
    }
}

