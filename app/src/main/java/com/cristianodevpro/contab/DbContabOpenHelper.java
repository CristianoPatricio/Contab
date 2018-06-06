package com.cristianodevpro.contab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbContabOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contab.db";

    public DbContabOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //criar tabelas
        DbTableRegistoMovimentos dbTableRegistoMovimentos = new DbTableRegistoMovimentos(db);
        dbTableRegistoMovimentos.create();

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);
        dbTableTipoDespesa.create();

        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);
        dbTableTipoReceita.create();

        DbTableOrcamento dbTableOrcamento = new DbTableOrcamento(db);
        dbTableOrcamento.create();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
