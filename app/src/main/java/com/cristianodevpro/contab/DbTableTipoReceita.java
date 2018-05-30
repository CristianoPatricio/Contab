package com.cristianodevpro.contab;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbTableTipoReceita implements BaseColumns {

    public static final String TABLE_NAME = "TipoReceita";
    public static final String ID_RECEITA = "id_receita";
    public static final String CATEGORIA_RECEITA = "categoria";
    private SQLiteDatabase db;

    public DbTableTipoReceita(SQLiteDatabase db) {
        this.db = db;
    }

    //criação da tabela
    public void create(){
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                ID_RECEITA + " TEXT PRIMARY KEY," +
                CATEGORIA_RECEITA + " TEXT NOT NULL)"
        );
    }
}
