package com.cristianodevpro.contab;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbTableRegistoMovimentos implements BaseColumns {

    public static final String TABLE_NAME = "RegistoMovimentos";
    public static final String ID_MOVIMENTO = "id_movimento";
    public static final String DIA = "dia";
    public static final String MES = "mes";
    public static final String ANO = "ano";
    public static final String RECEITADESPESA = "receitadespesa";
    public static final String DESGINACAO = "desginacao";
    public static final String VALOR = "valor";
    public static final String FK_ID_RECEITA = "id_Receita";
    public static final String FK_ID_DESPESA = "id_Despesa";

    public static final String[] ALL_COLUMNS = new String[]{ID_MOVIMENTO, DIA, MES, ANO, RECEITADESPESA, DESGINACAO, VALOR, FK_ID_RECEITA, FK_ID_DESPESA};

    private SQLiteDatabase db;

    //Construtor
    public DbTableRegistoMovimentos(SQLiteDatabase db) {
        this.db = db;
    }

    //Criação da tabela
    public void create(){
        db.execSQL(
                "CREATE TABLE "+TABLE_NAME+" " +
                        "("+
                ID_MOVIMENTO+" TEXT PRIMARY KEY, "+
                DIA+" INTEGER NOT NULL, "+
                MES+" INTEGER NOT NULL, "+
                ANO+" INTEGER NOT NULL, "+
                RECEITADESPESA+" TEXT NOT NULL, "+
                DESGINACAO+" TEXT, "+
                VALOR+" REAL NOT NULL, "+
                FK_ID_RECEITA+" INTEGER REFERENCES "+DbTableTipoReceita.TABLE_NAME+" ("+DbTableTipoReceita.ID_RECEITA+"), "+
                        FK_ID_DESPESA+" INTEGER REFERENCES "+DbTableTipoDespesa.TABLE_NAME+" ("+DbTableTipoDespesa.ID_DESPESA+")" + ")"
        );
    }

    //CRUD
    public static ContentValues getContentValues (RegistoMovimentos registoMovimentos){
        ContentValues values = new ContentValues();
        values.put(ID_MOVIMENTO, registoMovimentos.getId_movimento());
        values.put(DIA, registoMovimentos.getDia());
        values.put(MES, registoMovimentos.getMes());
        values.put(ANO, registoMovimentos.getAno());
        values.put(RECEITADESPESA, registoMovimentos.getReceitadespesa());
        values.put(DESGINACAO, registoMovimentos.getDesignacao());
        values.put(VALOR, registoMovimentos.getValor());
        values.put(FK_ID_RECEITA, registoMovimentos.getTiporeceita());
        values.put(FK_ID_DESPESA, registoMovimentos.getTipodespesa());

        return values;
    }

    public static RegistoMovimentos getCurrentTipoDespesaRegistoMovimentoFromCursor(Cursor cursor){
        final int posId = cursor.getColumnIndex(ID_MOVIMENTO);
        final int posDia = cursor.getColumnIndex(DIA);
        final int posMes = cursor.getColumnIndex(MES);
        final int posAno = cursor.getColumnIndex(ANO);
        final int posRecDes = cursor.getColumnIndex(RECEITADESPESA);
        final int posDesig = cursor.getColumnIndex(DESGINACAO);
        final int posValor = cursor.getColumnIndex(VALOR);
        final int posIdRec = cursor.getColumnIndex(FK_ID_RECEITA);
        final int posIdDes = cursor.getColumnIndex(FK_ID_DESPESA);

        RegistoMovimentos registoMovimentos = new RegistoMovimentos();

        registoMovimentos.setId_movimento(cursor.getString(posId));
        registoMovimentos.setDia(cursor.getInt(posDia));
        registoMovimentos.setMes(cursor.getInt(posMes));
        registoMovimentos.setAno(cursor.getInt(posAno));
        registoMovimentos.setReceitadespesa(cursor.getString(posRecDes));
        registoMovimentos.setDesignacao(cursor.getString(posDesig));
        registoMovimentos.setValor(cursor.getDouble(posValor));
        registoMovimentos.setTiporeceita(cursor.getInt(posIdRec));
        registoMovimentos.setTipodespesa(cursor.getInt(posIdDes));

        return registoMovimentos;
    }

    //insert
    public long insert (ContentValues values){
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
