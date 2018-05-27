package com.cristianodevpro.contab;

public class TipoDespesa {
    String id_despesa, categoria;

    //Construtores
    public TipoDespesa() {}

    public TipoDespesa(String id_despesa, String categoria){
        this.id_despesa = id_despesa;
        this.categoria = categoria;
    }

    //Setters and Getters
    public String getId_despesa() {
        return id_despesa;
    }

    public void setId_despesa(String id_despesa) {
        this.id_despesa = id_despesa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
