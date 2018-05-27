package com.cristianodevpro.contab;

public class RegistoMovimentos {

    String id_movimento, receitadespesa, designacao, tiporeceita, tipodespesa;
    int dia, mes, ano;
    double valor;

    //Construtores
    public RegistoMovimentos() {}

    public RegistoMovimentos(String id_movimento, int dia, int mes, int ano, String receitadespesa, String designacao, double valor, String tiporeceita, String tipodespesa) {
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

    public String getTiporeceita() {
        return tiporeceita;
    }

    public void setTiporeceita(String tiporeceita) {
        this.tiporeceita = tiporeceita;
    }

    public String getTipodespesa() {
        return tipodespesa;
    }

    public void setTipodespesa(String tipodespesa) {
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
