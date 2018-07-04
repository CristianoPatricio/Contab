package com.cristianodevpro.contab;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

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
        orcamento.setValor(600.0);
        orcamento.setValor(700.0);
        orcamento.setValor(800.0);

        //Insert/Create (C)RUD
        long id = tableOrcamento.insert(DbTableOrcamento.getContentValues(orcamento));
        assertNotEquals("Erro ao inserir orçamento",-1,id); //Se der -1 é porque não foi possível inserir o registo

        //query/Read C/R)UD
        orcamento = ReadFirstOrcamento(tableOrcamento, 600.0, id);

        //update CR(U)D
        orcamento.setValor(200.0);

        int rowsAffected = tableOrcamento.update(
                DbTableOrcamento.getContentValues(orcamento),
                DbTableOrcamento._ID + "=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao atualizar orçamento",1,rowsAffected);

        //delete CRU(D)
        rowsAffected = tableOrcamento.delete(
                DbTableOrcamento._ID+"=?",
                new String[]{Long.toString(id)}
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
        tipoReceita.setCategoria("Depósito");
        tipoReceita.setCategoria("Economias");

        //Insert/Create (C)RUD
        long id = tableTipoReceita.insert(DbTableTipoReceita.getContentValues(tipoReceita));
        assertNotEquals("Erro ao inserir categoria",-1,id); //Se der -1 é porque não foi possível inserir o registo

        //query/Read C/R)UD
        //tipoReceita = ReadFirstTipoReceita(tableTipoReceita,"Vencimento",id);
        //int idCat = ReadIdCategoriaReceita(tableTipoReceita, "Vencimento", 2);
        //ArrayList<String> list = ReadCategoriasReceitaFromDb(tableTipoReceita, );
        String categoria = dbContabOpenHelper.getTipoReceitaByID((int)id);
        assertEquals("Categoria não encontrada","Economias",categoria);

        //update CR(U)D
        tipoReceita.setCategoria("Salário");

        int rowsAffected = tableTipoReceita.update(
                DbTableTipoReceita.getContentValues(tipoReceita),
                DbTableTipoReceita._ID + "=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao atualizar categoria",1,rowsAffected);

        //delete CRU(D)
        rowsAffected = tableTipoReceita.delete(
                DbTableTipoReceita._ID+"=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao eliminar categoria",1,rowsAffected);


        Cursor cursor1 = tableTipoReceita.query(DbTableTipoReceita.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Categorias encontradas depois de eliminadas...",0,cursor1.getCount());
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
                DbTableTipoDespesa._ID + "=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao atualizar categoria",1,rowsAffected);

        //delete CRU(D)
        rowsAffected = tableTipoDespesa.delete(
                DbTableTipoDespesa._ID+"=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Erro ao eliminar categoria",1,rowsAffected);


        Cursor cursor = tableTipoDespesa.query(DbTableTipoDespesa.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Categorias encontradas depois de eliminadas...",0,cursor.getCount());
    }

    @Test
    public void RegistoMovimentosCRUDTest(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getContext());

        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableTipoDespesa tableTipoDespesa = new DbTableTipoDespesa(db);
        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);

        TipoDespesa tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria("Alimentação");

        long idDespesa = tableTipoDespesa.insert(DbTableTipoDespesa.getContentValues(tipoDespesa));

        //Insert/Create (C)RUD
        RegistoMovimentos registoMovimentos = new RegistoMovimentos();
        registoMovimentos.setId_movimento("080618113201");
        registoMovimentos.setDia(8);
        registoMovimentos.setMes(6);
        registoMovimentos.setAno(2018);
        registoMovimentos.setReceitadespesa("Despesa");
        registoMovimentos.setDesignacao("Almoço cantina");
        registoMovimentos.setValor(4.8);
        registoMovimentos.setTipodespesa((int)idDespesa);

        tableRegistoMovimentos.insert(DbTableRegistoMovimentos.getContentValues(registoMovimentos));
        assertNotEquals("Erro ao inserir registo",-1,1); //Se der -1 é porque não foi possível inserir o registo

        //query/Read C/R)UD
        registoMovimentos = ReadFirstRegisto(tableRegistoMovimentos,"080618113201",8,6,2018,"Despesa","Almoço cantina",4.8,(int) idDespesa);
        double valor =  getValorDespesas(tableRegistoMovimentos, 4.8);

        //update CR(U)D
        registoMovimentos.setDesignacao("Almoço churrasqueira");


        int rowsAffected = tableRegistoMovimentos.update(
                DbTableRegistoMovimentos.getContentValues(registoMovimentos),
                DbTableRegistoMovimentos._ID + "=?",
                new String[]{"080618113201"}
        );
        assertEquals("Erro ao atualizar categoria",1,rowsAffected);

        //delete CRU(D)
        rowsAffected = tableRegistoMovimentos.delete(
                DbTableRegistoMovimentos._ID+"=?",
                new String[]{"080618113201"}
        );
        assertEquals("Erro ao eliminar categoria",1,rowsAffected);


        Cursor cursor = tableRegistoMovimentos.query(DbTableRegistoMovimentos.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Categorias encontradas depois de eliminadas...",0,cursor.getCount());

    }

    private Orcamento ReadFirstOrcamento(DbTableOrcamento tableOrcamento, double expectedValue, long expectedId) {
        Cursor cursor = tableOrcamento.query(DbTableOrcamento.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Erro ao ler orçamento",1,cursor.getCount()); //caso não devolva 1 linha, dá erro

        //Obter o primeiro registo de orçamento
        assertTrue("Erro ao ler o primeiro registo de orçamento",cursor.moveToNext());

        Orcamento orcamento = DbTableOrcamento.getCurrentOrcamentoFromCursor(cursor);

        assertEquals("Valor do orçamento incorreto",expectedValue, orcamento.getValor(), 0.001);
        assertEquals("Id do orçamento incorreto",expectedId, orcamento.getId_orcamento());

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

    private RegistoMovimentos ReadFirstRegisto(DbTableRegistoMovimentos tableRegistoMovimentos, String expectedId, int expectedDia, int expectedMes, int expectedAno, String expectedRecDes, String expectedDesig, double expectedValor, int expectedTipoDes) {
        Cursor cursor = tableRegistoMovimentos.query(DbTableRegistoMovimentos.INSERT_DESPESA_COLUMNS, null, null, null, null, null);
        assertEquals("Erro ao ler tipo receita",1,cursor.getCount()); //caso não devolva 1 linha, dá erro

        //Obter a primeiro registo
        assertTrue("Erro ao ler a categoria da receita",cursor.moveToNext());

        RegistoMovimentos registoMovimentos = DbTableRegistoMovimentos.getCurrentRegistoMovimentoDespesaFromCursor(cursor);

        assertEquals("Id registo incorreto",expectedId,registoMovimentos.getId_movimento());
        assertEquals("Dia registo incorreto",expectedDia,registoMovimentos.getDia());
        assertEquals("Mes registo incorreto",expectedMes,registoMovimentos.getMes());
        assertEquals("Ano registo incorreto",expectedAno,registoMovimentos.getAno());
        assertEquals("ReceitaDespesa registo incorreto",expectedRecDes,registoMovimentos.getReceitadespesa());
        assertEquals("Designacao registo incorreto",expectedDesig,registoMovimentos.getDesignacao());
        assertEquals("Valor registo incorreto",expectedValor,registoMovimentos.getValor(), 0.001);
        assertEquals("Tipo despesa registo incorreto",expectedTipoDes,registoMovimentos.getTipodespesa());

        return registoMovimentos;
    }

    private double getValorDespesas(DbTableRegistoMovimentos tableRegistoMovimentos, double expectedValue){
        Cursor cursor = tableRegistoMovimentos.query(new String[]{"SUM(valor)"}, "receitadespesa =?", new String[]{"Despesa"}, null, null, null);
        assertEquals("Erro ao ler tipo receita",1,cursor.getCount()); //caso não devolva 1 linha, dá erro

        //Obter a primeiro registo

        assertTrue("Erro ao ler a categoria da receita",cursor.moveToNext());
        double valor = 0;
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            valor = cursor.getDouble(cursor.getColumnIndex("valor"));
        }

        assertEquals("Valor incorreto",expectedValue,valor,0.001);
        return valor;

    }

    private int ReadIdCategoriaReceita(DbTableTipoReceita tableTipoReceita, String categoria, int expectedId){
        Cursor cursor = tableTipoReceita.query(DbTableTipoReceita.ID_COLUMN,DbTableTipoReceita.CATEGORIA_RECEITA + "= '" + categoria + "'",null, null, null, null);

        int id = DbTableTipoReceita.getIdCategoriaReceita(cursor);

        assertEquals("Id incorreto",expectedId,id);

        return id;
    }

    /*
    private ArrayList<String> ReadCategoriasReceitaFromDb(DbTableTipoReceita tableTipoReceita, char[] expectedArray){

        Cursor cursor = tableTipoReceita.query(DbTableTipoReceita.CATEGORIA_COLUMN,null, null, null, null, null);
        assertEquals("Erro ao ler tipo receita",1,cursor.getCount()); //caso não devolva 1 linha, dá erro

        return list;
    }*/
}
