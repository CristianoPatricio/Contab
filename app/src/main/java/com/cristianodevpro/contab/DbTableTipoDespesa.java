package com.cristianodevpro.contab;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbTableTipoDespesa implements BaseColumns {

    public static final String TABLE_NAME = "TipoDespesa";
    public static final String ID_DESPESA = "id_despesa";
    public static final String CATEGORIA_DESPESAS = "categoria";
    private SQLiteDatabase db;

    public DbTableTipoDespesa(SQLiteDatabase db) {
        this.db = db;
    }

    //criação da tabela
    public void create(){
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                ID_DESPESA + " TEXT PRIMARY KEY," +
                CATEGORIA_DESPESAS + " TEXT NOT NULL)"
        );
    }
}
