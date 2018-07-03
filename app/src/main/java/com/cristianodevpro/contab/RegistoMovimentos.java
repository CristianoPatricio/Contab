package com.cristianodevpro.contab;

import java.util.Date;

public class RegistoMovimentos {

    String id_movimento, receitadespesa, designacao;
    int tiporeceita, tipodespesa;
    int dia, mes, ano;
    Date data;
    double valor;

    //Construtores
    public RegistoMovimentos() {}

    public void RegistoMovimentos(String id_movimento, int dia, int mes, int ano, String receitadespesa, String designacao, double valor, int tiporeceita, int tipodespesa) {
        this.id_movimento = id_movimento;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.receitadespesa = receitadespesa;
        this.designacao = designacao;
        this.valor = valor;
        this.tiporeceita = tiporeceita;
        this.tipodespesa = tipodespesa;
    }


    //Setters and Getters
    public String getId_movimento() {
        return id_movimento;
    }

    public void setId_movimento(String id_movimento) {
        this.id_movimento = id_movimento;
    }

    public String getReceitadespesa() {
        return receitadespesa;
    }

    public void setReceitadespesa(String receitadespesa) {
        this.receitadespesa = receitadespesa;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public int getTiporeceita() {
        return tiporeceita;
    }

    public void setTiporeceita(int tiporeceita) {
        this.tiporeceita = tiporeceita;
    }

    public int getTipodespesa() {
        return tipodespesa;
    }

    public void setTipodespesa(int tipodespesa) {
        this.tipodespesa = tipodespesa;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
