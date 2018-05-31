package com.cristianodevpro.contab;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.Locale;

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

    //CRUD
    public static ContentValues getContentValues(Orcamento orcamento){
        ContentValues values = new ContentValues();
        values.put(ID_ORCAMENTO, orcamento.getId_orcamento());
        values.put(VALOR, orcamento.getValor());

        return values;
    }

    //inserir
    public long insert(ContentValues values){
        return db.insert(TABLE_NAME, null, values);
    }
}
