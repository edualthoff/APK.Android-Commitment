package com.eduardo.appagenda.object;

import java.util.Date;

public class Compromisso {

    private int id;
    private String titulo;
    private String descr;
    private Date date = new Date();
    private int status;
    private Pessoa pessoa = new Pessoa();

    public Compromisso(String titulo, String descr, Date date, int status, Pessoa pessoa) {
        this.titulo = titulo;
        this.descr = descr;
        this.date = date;
        this.status = status;
        this.pessoa = pessoa;
    }

    public Compromisso (int id, String titulo, String descr, Date date, int status, Pessoa pessoa) {
        this.id = id;
        this.titulo = titulo;
        this.descr = descr;
        this.date = date;
        this.status = status;
        this.pessoa = pessoa;
    }

    public Compromisso() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

}
