package com.cristianodevpro.contab;

public class Orcamento {
    int id_orcamento;
    double valor;

    //Construtores
    public Orcamento() {}

    public Orcamento(int id_orcamento, double valor){
        this.id_orcamento = id_orcamento;
        this.valor = valor;
    }

    //Setters and Getters
    public int getId_orcamento() {
        return id_orcamento;
    }

    public void setId_orcamento(int id_orcamento) {
        this.id_orcamento = id_orcamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }



}
