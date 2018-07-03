package com.cristianodevpro.contab;

public class TipoReceita {
    int id_receita;
    String categoria;

    //Construtores
    public TipoReceita() {}

    public TipoReceita(int id_receita, String categoria) {
        this.id_receita = id_receita;
        this.categoria = categoria;
    }

    //Setters and Getters
    public int getId_receita() {
        return id_receita;
    }

    public void setId_receita(int id_receita) {
        this.id_receita = id_receita;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }



}
