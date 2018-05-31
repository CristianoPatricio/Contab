package com.cristianodevpro.contab;

import android.content.ContentValues;
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

    //CRUD
    public static ContentValues getContentValues(TipoReceita tipoReceita){
        ContentValues values = new ContentValues();
        values.put(ID_RECEITA, tipoReceita.getId_receita());
        values.put(CATEGORIA_RECEITA, tipoReceita.getCategoria());

        return values;
    }

    //insert
    public long insert(ContentValues values) {
        return db.insert(TABLE_NAME, null, values);
    }
}
