package com.cristianodevpro.contab;

import android.content.ContentValues;
import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbTableRegistoMovimentos implements BaseColumns {

    public static final String TABLE_NAME = "RegistoMovimentos";
    public static final String _ID = "id_movimento";
    public static final String DIA = "dia";
    public static final String MES = "mes";
    public static final String ANO = "ano";
    public static final String RECEITADESPESA = "receitadespesa";
    public static final String DESGINACAO = "desginacao";
    public static final String VALOR = "valor";
    public static final String FK_ID_RECEITA = "id_Receita";
    public static final String FK_ID_DESPESA = "id_Despesa";

    public static final String[] ALL_COLUMNS = new String[]{_ID, DIA, MES, ANO, RECEITADESPESA, DESGINACAO, VALOR, FK_ID_RECEITA, FK_ID_DESPESA};
    public static final String[] INSERT_RECEITA_COLUMNS = new String[]{_ID, DIA, MES, ANO, RECEITADESPESA, DESGINACAO, VALOR, FK_ID_RECEITA};
    public static final String[] INSERT_DESPESA_COLUMNS = new String[]{_ID, DIA, MES, ANO, RECEITADESPESA, DESGINACAO, VALOR, FK_ID_DESPESA};
    public static final String[] VALOR_COLUMN = new String[]{VALOR};

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
                _ID+" TEXT PRIMARY KEY, "+
                DIA+" INTEGER NOT NULL, "+
                MES+" INTEGER NOT NULL, "+
                ANO+" INTEGER NOT NULL, "+
                RECEITADESPESA+" TEXT NOT NULL, "+
                DESGINACAO+" TEXT, "+
                VALOR+" REAL NOT NULL, "+
                FK_ID_RECEITA+" INTEGER REFERENCES "+DbTableTipoReceita.TABLE_NAME+" ("+DbTableTipoReceita._ID+"), "+
                        FK_ID_DESPESA+" INTEGER REFERENCES "+DbTableTipoDespesa.TABLE_NAME+" ("+DbTableTipoDespesa._ID+")" + ")"
        );
    }

    //CRUD
    public static ContentValues getContentValues (RegistoMovimentos registoMovimentos){
        ContentValues values = new ContentValues();
        values.put(_ID, registoMovimentos.getId_movimento());
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

    public static RegistoMovimentos getCurrentRegistoMovimentoDespesaFromCursor(Cursor cursor){
        final int posId = cursor.getColumnIndex(_ID);
        final int posDia = cursor.getColumnIndex(DIA);
        final int posMes = cursor.getColumnIndex(MES);
        final int posAno = cursor.getColumnIndex(ANO);
        final int posRecDes = cursor.getColumnIndex(RECEITADESPESA);
        final int posDesig = cursor.getColumnIndex(DESGINACAO);
        final int posValor = cursor.getColumnIndex(VALOR);
        final int posIdDes = cursor.getColumnIndex(FK_ID_DESPESA);

        RegistoMovimentos registoMovimentos = new RegistoMovimentos();

        registoMovimentos.setId_movimento(cursor.getString(posId));
        registoMovimentos.setDia(cursor.getInt(posDia));
        registoMovimentos.setMes(cursor.getInt(posMes));
        registoMovimentos.setAno(cursor.getInt(posAno));
        registoMovimentos.setReceitadespesa(cursor.getString(posRecDes));
        registoMovimentos.setDesignacao(cursor.getString(posDesig));
        registoMovimentos.setValor(cursor.getDouble(posValor));
        registoMovimentos.setTipodespesa(cursor.getInt(posIdDes));

        return registoMovimentos;
    }

    public static RegistoMovimentos getCurrentRegistoMovimentoReceitaFromCursor(Cursor cursor){
        final int posId = cursor.getColumnIndex(_ID);
        final int posDia = cursor.getColumnIndex(DIA);
        final int posMes = cursor.getColumnIndex(MES);
        final int posAno = cursor.getColumnIndex(ANO);
        final int posRecDes = cursor.getColumnIndex(RECEITADESPESA);
        final int posDesig = cursor.getColumnIndex(DESGINACAO);
        final int posValor = cursor.getColumnIndex(VALOR);
        final int posIdRec = cursor.getColumnIndex(FK_ID_RECEITA);

        RegistoMovimentos registoMovimentos = new RegistoMovimentos();

        registoMovimentos.setId_movimento(cursor.getString(posId));
        registoMovimentos.setDia(cursor.getInt(posDia));
        registoMovimentos.setMes(cursor.getInt(posMes));
        registoMovimentos.setAno(cursor.getInt(posAno));
        registoMovimentos.setReceitadespesa(cursor.getString(posRecDes));
        registoMovimentos.setDesignacao(cursor.getString(posDesig));
        registoMovimentos.setValor(cursor.getDouble(posValor));
        registoMovimentos.setTiporeceita(cursor.getInt(posIdRec));

        return registoMovimentos;
    }

    public static double getValorDespesasFromDb(Cursor cursor){ //Obter o valor das despesas
       final int posValor = cursor.getColumnIndex("valor");

        double totalDespesas = 0;
        double valor = 0;
        if (cursor.getCount() > 0){ //Encontra pelo menos um registo
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                valor = cursor.getDouble(posValor);
                totalDespesas = totalDespesas + valor;
            }
        }

        return totalDespesas;
    }

    public static double getValorReceitasFromDb(Cursor cursor){
        final int posValor = 0; //select SUM(valor) -> coluna 0

        double valorReceitas = 0;
        if (cursor.getCount() > 0){ //Encontra um registo
            cursor.moveToFirst();
            valorReceitas = cursor.getDouble(posValor);
        }

        return valorReceitas;
    }

    public static double getSaldoFromDb(Cursor cursor){
        final int posValor = 0; //select SUM(valor) -> coluna 0

        double saldo = 0;
        if (cursor.getCount() > 0){ //Encontra um registo
            cursor.moveToFirst();
            saldo = cursor.getDouble(posValor);
        }

        return saldo;
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
