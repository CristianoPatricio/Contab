package com.cristianodevpro.contab;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class DbTableTipoReceita implements BaseColumns {

    public static final String TABLE_NAME = "TipoReceita";
    public static final String _ID = "id_receita";
    public static final String CATEGORIA_RECEITA = "categoria";

    public static final String[] ALL_COLUMNS = new String[]{_ID, CATEGORIA_RECEITA};
    public static final String[] ID_COLUMN = new String[]{_ID};
    public static final String[] CATEGORIA_COLUMN = new String[]{CATEGORIA_RECEITA};

    private SQLiteDatabase db;

    public DbTableTipoReceita(SQLiteDatabase db) {
        this.db = db;
    }

    //criação da tabela
    public void create(){
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CATEGORIA_RECEITA + " TEXT NOT NULL)"
        );
    }

    //CRUD
    public static ContentValues getContentValues(TipoReceita tipoReceita){
        ContentValues values = new ContentValues();
        //values.put(_ID, tipoReceita.getId_receita());
        values.put(CATEGORIA_RECEITA, tipoReceita.getCategoria());

        return values;
    }

    public static TipoReceita getCurrentTipoReceitaFromCursor(Cursor cursor){
        final int posId = cursor.getColumnIndex(_ID);
        final int posCatRec = cursor.getColumnIndex(CATEGORIA_RECEITA);

        TipoReceita tipoReceita = new TipoReceita();

        tipoReceita.setId_receita(cursor.getInt(posId));
        tipoReceita.setCategoria(cursor.getString(posCatRec));

        return tipoReceita;
    }

    public static int getIdCategoriaReceita(Cursor cursor){
        final int posId = cursor.getColumnIndex(_ID);

        int id = 0;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(posId);
        }

        return id;
    }

    public static ArrayList<String> getCategoriasReceitaFromDb(Cursor cursor){
        final int posCatRec = cursor.getColumnIndex(CATEGORIA_RECEITA);

        ArrayList<String> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do  {
                list.add(cursor.getString(posCatRec));
            }while (cursor.moveToNext());
        }

        return list;
    }

    public static String getTipoReceitaByID(Cursor cursor){
        final int posCatRec = cursor.getColumnIndex(CATEGORIA_RECEITA);

        String categoria = "";
        if (cursor.moveToFirst()){
            categoria = cursor.getString(posCatRec);
        }

        return categoria;
    }

    //insert
    public long insert(ContentValues values) {
        return db.insert(TABLE_NAME, null, values);
    }

    //update
    public int update(ContentValues values, String whereClause, String[] whereArgs){
        return db.update(TABLE_NAME,values,whereClause,whereArgs);
    }

    //delete
    public int delete(String whereClause, String[] whereArgs){
        return db.delete(TABLE_NAME,whereClause,whereArgs);
    }

    //read
    public Cursor query (String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

}
