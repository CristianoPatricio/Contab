package com.cristianodevpro.contab;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbTableOrcamento implements BaseColumns {

    public static final String ID_ORCAMENTO = "id_orcamento";
    public static final String VALOR = "valor";
    public static final String TABLE_NAME = "Orcamento";
    private SQLiteDatabase db;

    public DbTableOrcamento(SQLiteDatabase db) {
        this.db = db;
    }

    //criação da tabela
    public void create() {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ID_ORCAMENTO + " TEXT PRIMARY KEY," +
                VALOR + " REAL NOT NULL" +
                ")"
        );
    }
}
