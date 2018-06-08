package com.cristianodevpro.contab;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbContabTests {
    @Before
    public void setUp(){
        getContext().deleteDatabase(DbContabOpenHelper.DATABASE_NAME);
    }


    @Test
    public void openDbContabTest() {
        // Context of the app under test.
        Context appContext = getContext();

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(appContext);

        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase(); //Db para leitura
        assertTrue("Não foi possível abrir/criar a Db Contab",db.isOpen()); //devolve a mensagem se não conseguir criar/abrir a Base de Dados
        db.close();
    }

    private Context getContext(){
        return InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void OrcamentoCRUDTest(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getContext());

        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableOrcamento tableOrcamento = new DbTableOrcamento(db);

        Orcamento orcamento = new Orcamento();
        orcamento.setId_orcamento("060618234301");
        orcamento.setValor(500.0);

        //Insert/Create (C)RUD
        long insert = tableOrcamento.insert(DbTableOrcamento.getContentValues(orcamento));
        assertNotEquals("Erro ao inserir orçamento",-1,insert); //Se der -1 é porque não foi possível inserir o registo

        //query/Read C/R)UD
        orcamento = ReadFirstOrcamento(tableOrcamento, 500.0, "060618234301");

        //update CR(U)D
        orcamento.setId_orcamento("070618000902");
        orcamento.setValor(600.0);

        int rowsAffected = tableOrcamento.update(
                DbTableOrcamento.getContentValues(orcamento),
                DbTableOrcamento.ID_ORCAMENTO + "=?",
                new String[]{"060618234301"}
        );
        assertEquals("Erro ao atualizar orçamento",1,rowsAffected);

        //delete CRU(D)
        rowsAffected = tableOrcamento.delete(
                DbTableOrcamento.ID_ORCAMENTO+"=?",
                new String[]{"070618000902"}
                );
        assertEquals("Erro ao eliminar orçamento",1,rowsAffected);

        Cursor cursor = tableOrcamento.query(DbTableOrcamento.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Registos de orçamentos encontrados depois de eliminados...",0,cursor.getCount());

    }

    @Test
    public void TipoReceitaCRUDTest(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getContext());

        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableTipoReceita tableTipoReceita = new DbTableTipoReceita(db);

        TipoReceita tipoReceita = new TipoReceita();
        tipoReceita.setCategoria("Vencimento");

        //Insert/Create (C)RUD
        long id = tableTipoReceita.insert(DbTableTipoReceita.getContentValues(tipoReceita));
        assertNotEquals("Erro ao inserir categoria",-1,id); //Se der -1 é porque não foi possível inserir o registo

        //query/Read C/R)UD
        tipoReceita = ReadFirstTipoReceita(tableTipoReceita,"Vencimento",id);
        //int idCat = ReadIdCategoriaReceita(tableTipoReceita, "Vencimento", 2);

        //update CR(U)D
        tipoReceita.setCategoria("Salário");

        int rowsAffected = tableTipoReceita.update(
                DbTableTipoReceita.getContentValues(tipoReceita),
                DbTableTipoReceita.ID_RECEITA + "=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao atualizar categoria",1,rowsAffected);

        //delete CRU(D)
        rowsAffected = tableTipoReceita.delete(
                DbTableTipoReceita.ID_RECEITA+"=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao eliminar categoria",1,rowsAffected);


        Cursor cursor = tableTipoReceita.query(DbTableTipoReceita.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Categorias encontradas depois de eliminadas...",0,cursor.getCount());
    }

    @Test
    public void TipoDespesaCRUDTest(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getContext());

        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableTipoDespesa tableTipoDespesa = new DbTableTipoDespesa(db);

        TipoDespesa tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Compras");

        //Insert/Create (C)RUD
        long id = tableTipoDespesa.insert(DbTableTipoDespesa.getContentValues(tipoDespesa));
        assertNotEquals("Erro ao inserir categoria",-1,id); //Se der -1 é porque não foi possível inserir o registo

        //query/Read C/R)UD
        tipoDespesa = ReadFirstTipoDespesa(tableTipoDespesa,"Compras",id);

        //update CR(U)D
        tipoDespesa.setCategoria("Alimentação");

        int rowsAffected = tableTipoDespesa.update(
                DbTableTipoDespesa.getContentValues(tipoDespesa),
                DbTableTipoDespesa.ID_DESPESA + "=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao atualizar categoria",1,rowsAffected);

        //delete CRU(D)
        rowsAffected = tableTipoDespesa.delete(
                DbTableTipoDespesa.ID_DESPESA+"=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao eliminar categoria",1,rowsAffected);


        Cursor cursor = tableTipoDespesa.query(DbTableTipoDespesa.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Categorias encontradas depois de eliminadas...",0,cursor.getCount());
    }


    private Orcamento ReadFirstOrcamento(DbTableOrcamento tableOrcamento, double expectedValue, String expectedId) {
        Cursor cursor = tableOrcamento.query(DbTableOrcamento.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Erro ao ler orçamento",1,cursor.getCount()); //caso não devolva 1 linha, dá erro

        //Obter o primeiro registo de orçamento
        assertTrue("Erro ao ler o primeiro registo de orçamento",cursor.moveToNext());

        Orcamento orcamento = DbTableOrcamento.getCurrentOrcamentoFromCursor(cursor);

        assertEquals("Valor do orçamento incorreto",expectedValue, orcamento.getValor(), 0.001);
        assertEquals("Id do orçamento incorreto",expectedId,orcamento.getId_orcamento());

        return orcamento;
    }

    private TipoReceita ReadFirstTipoReceita(DbTableTipoReceita tableTipoReceita, String expectedName, long expectedId) {
        Cursor cursor = tableTipoReceita.query(DbTableTipoReceita.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Erro ao ler tipo receita",1,cursor.getCount()); //caso não devolva 1 linha, dá erro

        //Obter a primeira categoria de receitas
        assertTrue("Erro ao ler a categoria da receita",cursor.moveToNext());

        TipoReceita tipoReceita = DbTableTipoReceita.getCurrentTipoReceitaFromCursor(cursor);

        assertEquals("Nome da categoria incorreta",expectedName, tipoReceita.getCategoria());
        assertEquals("Id do categoria incorreto",expectedId,tipoReceita.getId_receita());

        return tipoReceita;
    }

    private TipoDespesa ReadFirstTipoDespesa(DbTableTipoDespesa tableTipoDespesa, String expectedName, long expectedId) {
        Cursor cursor = tableTipoDespesa.query(DbTableTipoDespesa.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Erro ao ler tipo receita",1,cursor.getCount()); //caso não devolva 1 linha, dá erro

        //Obter a primeira categoria de receitas
        assertTrue("Erro ao ler a categoria da receita",cursor.moveToNext());

        TipoDespesa tipoDespesa = DbTableTipoDespesa.getCurrentTipoDespesaFromCursor(cursor);

        assertEquals("Nome da categoria incorreta",expectedName, tipoDespesa.getCategoria());
        assertEquals("Id da categoria incorreto",expectedId,tipoDespesa.getId_despesa());

        return tipoDespesa;
    }

    private int ReadIdCategoriaReceita(DbTableTipoReceita tableTipoReceita, String categoria, int expectedId){
        Cursor cursor = tableTipoReceita.query(DbTableTipoReceita.ID_COLUMN,DbTableTipoReceita.CATEGORIA_RECEITA + "= '" + categoria + "'",null, null, null, null);

        int id = DbTableTipoReceita.getIdCategoriaReceita(cursor);

        assertEquals("Id incorreto",expectedId,id);

        return id;
    }
}
