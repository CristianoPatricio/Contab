package com.cristianodevpro.contab;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbTableTipoDespesa implements BaseColumns {

    public static final String TABLE_NAME = "TipoDespesa";
    //public static final int ID_DESPESA = "id_despesa";
    public static final String CATEGORIA_DESPESAS = "categoria";

    public static final String[] ALL_COLUMNS = new String[]{_ID, CATEGORIA_DESPESAS};
    private SQLiteDatabase db;

    public DbTableTipoDespesa(SQLiteDatabase db) {
        this.db = db;
    }

    //criação da tabela
    public void create(){
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CATEGORIA_DESPESAS + " TEXT NOT NULL)"
        );
    }

    //CRUD
    public static ContentValues getContentValues(TipoDespesa tipoDespesa){
        ContentValues values = new ContentValues();
        values.put(_ID, tipoDespesa.getId_despesa());
        values.put(CATEGORIA_DESPESAS, tipoDespesa.getCategoria());

        return values;
    }

    public static TipoDespesa getCurrentTipoDespesaFromCursor(Cursor cursor){
        final int posId = cursor.getColumnIndex(_ID);
        final int posCatDes = cursor.getColumnIndex(CATEGORIA_DESPESAS);

        TipoDespesa tipoDespesa = new TipoDespesa();

        tipoDespesa.setId_despesa(cursor.getInt(posId));
        tipoDespesa.setCategoria(cursor.getString(posCatDes));

        return tipoDespesa;
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
