package com.cristianodevpro.contab;

public class TipoReceita {
    String id_receita, categoria;

    //Construtores
    public TipoReceita() {}
    public TipoReceita(String id_receita, String categoria) {
        this.id_receita = id_receita;
        this.categoria = categoria;
    }

    //Setters and Getters
    public String getId_receita() {
        return id_receita;
    }

    public void setId_receita(String id_receita) {
        this.id_receita = id_receita;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }



}
