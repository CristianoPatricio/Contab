package com.cristianodevpro.contab;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toolbar;
import android.database.Cursor;

import java.util.Locale;

public class DbTableOrcamento implements BaseColumns {

    public static final String _ID = "id_orcamento";
    public static final String VALOR = "valor";
    public static final String TABLE_NAME = "Orcamento";

    public static final String[] ALL_COLUMNS = new String[]{_ID, VALOR};
    public static final String[] VALOR_COLUMN = new String[]{VALOR};
    public static final String[] ID_COLUMN = new String[]{_ID};

    private SQLiteDatabase db;

    public DbTableOrcamento(SQLiteDatabase db) {
        this.db = db;
    }

    //criação da tabela
    public void create() {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                VALOR + " REAL NOT NULL" +
                ")"
        );
    }

    //CRUD
    public static ContentValues getContentValues(Orcamento orcamento){
        ContentValues values = new ContentValues();
        //values.put(_ID, orcamento.getId_orcamento());
        values.put(VALOR, orcamento.getValor());

        return values;
    }

    public static Orcamento getCurrentOrcamentoFromCursor(Cursor cursor){
        final int posId = cursor.getColumnIndex(_ID);
        final int posValor = cursor.getColumnIndex(VALOR);

        Orcamento orcamento = new Orcamento();

        orcamento.setId_orcamento(cursor.getInt(posId));
        orcamento.setValor(cursor.getDouble(posValor));

        return orcamento;
    }

    public static double getValorOrcamentoFromDb(Cursor cursor){ //Obter o ultimo valor de orçamento definido
        final int posValor = cursor.getColumnIndex(DbTableOrcamento.VALOR);

        double valor = 0;
        if (cursor.getCount() > 0){ //Encontra pelo menos um registo
            cursor.moveToFirst();
            valor = cursor.getDouble(posValor);
        }

        return valor;
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
