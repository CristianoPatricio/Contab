package com.cristianodevpro.contab;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DbContabOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contab.db";

    public DbContabOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Criação da tabela Registo Movimentos
        DbTableRegistoMovimentos dbTableRegistoMovimentos = new DbTableRegistoMovimentos(db);
        dbTableRegistoMovimentos.create();

        //Criação da tabela TipoDespesa
        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);
        dbTableTipoDespesa.create();
        initialDespesasCategories(db); //Categorias Predefinidas

        //criação da tabela TipoReceita
        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);
        dbTableTipoReceita.create();
        initialReceitasCategories(db); //Categorias Predefinidas

        //criação da tabela Orcamento
        DbTableOrcamento dbTableOrcamento = new DbTableOrcamento(db);
        dbTableOrcamento.create();
    }

    private void initialReceitasCategories(SQLiteDatabase db){

        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);

        //Inserir categorias predefinidas para as receitas
        TipoReceita tipoReceita = new TipoReceita();
        tipoReceita.setCategoria("Vencimento");

        tipoReceita = new TipoReceita();
        tipoReceita.setCategoria("Depósito");

        tipoReceita = new TipoReceita();
        tipoReceita.setCategoria("Prémios");
        dbTableTipoReceita.insert(DbTableTipoReceita.getContentValues(tipoReceita));
    }

    private void initialDespesasCategories(SQLiteDatabase db){

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);

        TipoDespesa tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Alimentação");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Café");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Carro");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Casa");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Comida");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Telecomunicações");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Contas");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Entretenimento");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Presentes");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Saúde");

        tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Roupas");

        dbTableTipoDespesa.insert(DbTableTipoDespesa.getContentValues(tipoDespesa));
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove a BD antiga e cria uma nova
        db.execSQL("DROP TABLE IF EXISTS " + DbTableOrcamento.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbTableTipoReceita.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbTableTipoDespesa.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbTableRegistoMovimentos.TABLE_NAME);
        onCreate(db);
    }


    //Funções
    public void deleteAllCategoriaReceita(){
        String query = "DELETE FROM "+DbTableTipoReceita.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                db.delete(DbTableTipoReceita.TABLE_NAME, null, null);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    public void deleteAllCategoriaDespesa(){
        String query = "DELETE FROM "+DbTableTipoDespesa.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
             do{
                db.delete(DbTableTipoDespesa.TABLE_NAME, null, null);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    public void deleteAllOrcamento(){
        String query = "DELETE FROM "+DbTableOrcamento.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                db.delete(DbTableOrcamento.TABLE_NAME, null, null);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    public void deleteAllRegistosMovimentos(){
        String query = "DELETE FROM "+DbTableRegistoMovimentos.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                db.delete(DbTableRegistoMovimentos.TABLE_NAME, null, null);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    //Teste
    public double getOrcamentoFromDb(){
        String query = "SELECT "+DbTableOrcamento.VALOR_COLUMN+
                        " FROM "+DbTableOrcamento.TABLE_NAME+
                        " WHERE "+DbTableOrcamento._ID+
                        " = (SELECT MAX("+DbTableOrcamento._ID+") FROM "+DbTableOrcamento.TABLE_NAME+")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        double valor = 0;
        if (cursor.moveToFirst()){
            valor = cursor.getDouble(cursor.getColumnIndex(DbTableOrcamento.VALOR));
        }

        return valor;
    }

    public List<RegistoMovimentos> getListRegistoMovimentos(){

        String query = "SELECT * FROM "+DbTableRegistoMovimentos.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<RegistoMovimentos> registosLinkedList = new LinkedList<>();
        RegistoMovimentos registoMovimentos;

        if (cursor.moveToFirst()){
            do{
                registoMovimentos = new RegistoMovimentos();

                registoMovimentos.setId_movimento(cursor.getString(cursor.getColumnIndex(DbTableRegistoMovimentos._ID)));
                registoMovimentos.setDia(cursor.getInt(cursor.getColumnIndex(DbTableRegistoMovimentos.DIA)));
                registoMovimentos.setMes(cursor.getInt(cursor.getColumnIndex(DbTableRegistoMovimentos.MES)));
                registoMovimentos.setAno(cursor.getInt(cursor.getColumnIndex(DbTableRegistoMovimentos.ANO)));
                registoMovimentos.setReceitadespesa(cursor.getString(cursor.getColumnIndex(DbTableRegistoMovimentos.RECEITADESPESA)));
                registoMovimentos.setDesignacao(cursor.getString(cursor.getColumnIndex(DbTableRegistoMovimentos.DESGINACAO)));
                registoMovimentos.setValor(cursor.getDouble(cursor.getColumnIndex(DbTableRegistoMovimentos.VALOR)));
                registoMovimentos.setTiporeceita(cursor.getInt(cursor.getColumnIndex(DbTableRegistoMovimentos.FK_ID_RECEITA)));
                registoMovimentos.setTipodespesa(cursor.getInt(cursor.getColumnIndex(DbTableRegistoMovimentos.FK_ID_DESPESA)));

                registosLinkedList.add(registoMovimentos);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return registosLinkedList;
    }


    public String getTipoDespesaByID(int id){
        String query = "SELECT "+DbTableTipoDespesa.CATEGORIA_DESPESAS+" FROM "+DbTableTipoDespesa.TABLE_NAME+" WHERE "+DbTableTipoDespesa._ID+"=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{Integer.toString(id)});

        String categoria = "";
        if (cursor.moveToFirst()){
            categoria = cursor.getString(cursor.getColumnIndex(DbTableTipoDespesa.CATEGORIA_DESPESAS));
        }

        cursor.close();
        db.close();

        return categoria;
    }

    public String getTipoReceitaByID(int id){
        String query = "SELECT "+DbTableTipoReceita.CATEGORIA_RECEITA+" FROM "+DbTableTipoReceita.TABLE_NAME+" WHERE "+DbTableTipoReceita._ID+"=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{Integer.toString(id)});

        String categoria = "";
        if (cursor.moveToFirst()){
            categoria = cursor.getString(cursor.getColumnIndex(DbTableTipoReceita.CATEGORIA_RECEITA));
        }

        cursor.close();
        db.close();

        return categoria;
    }

    public TipoReceita getCatReceita(){
        String query = "SELECT * FROM "+DbTableTipoReceita.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        TipoReceita tipoReceita = new TipoReceita();
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do{
                tipoReceita.setId_receita(cursor.getColumnIndex(DbTableTipoReceita._ID));
                tipoReceita.setCategoria(String.valueOf(cursor.getColumnIndex(DbTableTipoReceita.CATEGORIA_RECEITA)));
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tipoReceita;
    }







}
