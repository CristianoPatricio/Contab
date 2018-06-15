package com.cristianodevpro.contab;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class GerirCategorias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_categorias);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ListView Categorias Receitas
        ListAdapter listAdapterReceitas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getCategoriasReceitaFromDb());
        ListView listViewReceitas = (ListView) findViewById(R.id.listViewReceitas);
        listViewReceitas.setAdapter(listAdapterReceitas);

        listViewReceitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO implementar código para eliminar categoria selecionada
            }
        });

        //ListView Categorias Despesas
        ListAdapter listAdapterDespesas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getCategoriasDespesaFromDb());
        ListView listViewDespesas = (ListView) findViewById(R.id.listViewDespesas);
        listViewDespesas.setAdapter(listAdapterDespesas);

        listViewDespesas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO implementar código para eliminar categoria selecionada
            }
        });
    }

    /**********************************Functions and Methods***************************************/
    public ArrayList<String> getCategoriasReceitaFromDb(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);

        Cursor cursor = dbTableTipoReceita.query(DbTableTipoReceita.CATEGORIA_COLUMN, null, null, null, null, DbTableTipoReceita._ID);

        ArrayList<String> list = DbTableTipoReceita.getCategoriasReceitaFromDb(cursor);

        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<String> getCategoriasDespesaFromDb(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);

        Cursor cursor = dbTableTipoDespesa.query(DbTableTipoDespesa.CATEGORIA_COLUMN, null, null, null, null, DbTableTipoDespesa._ID);

        ArrayList<String> list = DbTableTipoDespesa.getCategoriasDespesaFromDb(cursor);

        cursor.close();
        db.close();
        return list;
    }
}
