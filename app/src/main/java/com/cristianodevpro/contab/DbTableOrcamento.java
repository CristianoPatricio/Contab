package com.cristianodevpro.contab;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toolbar;
import android.database.Cursor;

import java.util.Locale;

public class DbTableOrcamento implements BaseColumns {

    public static final String ID_ORCAMENTO = "id_orcamento";
    public static final String VALOR = "valor";
    public static final String TABLE_NAME = "Orcamento";

    public static final String[] ALL_COLUMNS = new String[]{ID_ORCAMENTO, VALOR};
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

    public static Orcamento getCurrentOrcamentoFromCursor(Cursor cursor){
        final int posId = cursor.getColumnIndex(ID_ORCAMENTO);
        final int posValor = cursor.getColumnIndex(VALOR);

        Orcamento orcamento = new Orcamento();

        orcamento.setId_orcamento(cursor.getString(posId));
        orcamento.setValor(cursor.getDouble(posValor));

        return orcamento;
    }

    //create
    public long insert(ContentValues values){
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
