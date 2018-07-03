package com.cristianodevpro.contab;

public class TipoDespesa {
    int id_despesa;
    String categoria;

    //Construtores
    public TipoDespesa() {}


    public TipoDespesa(int id_despesa, String categoria){
        this.id_despesa = id_despesa;
        this.categoria = categoria;
    }

    //Setters and Getters
    public int getId_despesa() {
        return id_despesa;
    }

    public void setId_despesa(int id_despesa) {
        this.id_despesa = id_despesa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
