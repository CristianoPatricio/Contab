package com.cristianodevpro.contab;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbTableRegistoMovimentos implements BaseColumns {

    public static final String TABLE_NAME = "RegistoMovimentos";
    public static final String ID_MOVIMENTO = "id_movimento";
    public static final String DIA = "dia";
    public static final String MES = "mes";
    public static final String ANO = "ano";
    public static final String RECEITADESPESA = "receitadespesa";
    public static final String DESGINACAO = "desginacao";
    public static final String VALOR = "valor";
    public static final String FK_ID_RECEITA = "id_Receita";
    public static final String FK_ID_DESPESA = "id_Despesa";
    private SQLiteDatabase db;

    //Construtor
    public DbTableRegistoMovimentos(SQLiteDatabase db) {
        this.db = db;
    }

    //Criação da tabela
    public void create(){
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                ID_MOVIMENTO + " TEXT PRIMARY KEY," +
                DIA + " INTEGER NOT NULL," +
                MES + " INTEGER NOT NULL," +
                ANO + " INTEGER NOT NULL," +
                RECEITADESPESA + " TEXT NOT NULL," +
                DESGINACAO + " TEXT NOT NULL," +
                VALOR + " REAL NOT NULL," +
                FK_ID_RECEITA + " TEXT," +
                        "FOREIGN KEY ("+FK_ID_RECEITA+") REFERENCES "+DbTableTipoReceita.TABLE_NAME+"("+DbTableTipoReceita.ID_RECEITA+")" +
                        FK_ID_DESPESA + " TEXT," +
                        "FOREIGN KEY (" + FK_ID_DESPESA + ") REFERENCES "+DbTableTipoDespesa.TABLE_NAME+"("+DbTableTipoDespesa.ID_DESPESA+")"+")"

        );
    }


}
