package com.cristianodevpro.contab;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String categoria = adapterView.getItemAtPosition(position).toString();
                final int id = getIdCategoriaReceita(categoria);
                //Dialog de confirmação
                /****************************************************************************************/
                AlertDialog.Builder builder = new AlertDialog.Builder(GerirCategorias.this);
                builder.setTitle("Eliminar categoria \""+categoria+"\"")
                        .setMessage("Tem a certeza?")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    deleteCategoriaReceita(id); //elimina categoria receita
                                    Toast.makeText(getApplicationContext(),"Categoria eliminada com sucesso!", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),"Erro ao eliminar categoria...", Toast.LENGTH_LONG).show();
                                }
                                updateListCategoriasReceitas(); //atualiza lista
                            }
                        })
                        .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel(); //cancela a operação
                            }
                        })
                        .show();
                /**************************************************************************************/
            }
        });

        //ListView Categorias Despesas
        ListAdapter listAdapterDespesas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getCategoriasDespesaFromDb());
        ListView listViewDespesas = (ListView) findViewById(R.id.listViewDespesas);
        listViewDespesas.setAdapter(listAdapterDespesas);

        listViewDespesas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String categoria = adapterView.getItemAtPosition(position).toString();
                final int id = getIdCategoriaDespesa(categoria);
                //Dialog de confirmação
                /****************************************************************************************/
                AlertDialog.Builder builder = new AlertDialog.Builder(GerirCategorias.this);
                builder.setTitle("Eliminar categoria \""+categoria+"\"")
                        .setMessage("Tem a certeza?")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    deleteCategoriaDespesa(id); //elimina categoria despesa
                                    Toast.makeText(getApplicationContext(),"Categoria eliminada com sucesso!", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),"Erro ao eliminar categoria...", Toast.LENGTH_LONG).show();
                                }
                                updateListCategoriasDespesas(); //atualiza lista
                            }
                        })
                        .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel(); //cancela a operação
                            }
                        })
                        .show();
                /**************************************************************************************/
            }
        });
    }

    /**********************************Functions and Methods***************************************/
    private ArrayList<String> getCategoriasReceitaFromDb(){
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

    private ArrayList<String> getCategoriasDespesaFromDb(){
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

    private int getIdCategoriaReceita(String categoria){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);

        Cursor cursor = dbTableTipoReceita.query(DbTableTipoReceita.ID_COLUMN,DbTableTipoReceita.CATEGORIA_RECEITA+"=?",new String[]{categoria},null,null,null);

        int id = DbTableTipoReceita.getIdCategoriaReceita(cursor);

        cursor.close();
        db.close();
        return id;
    }

    private int getIdCategoriaDespesa(String categoria){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);

        Cursor cursor = dbTableTipoDespesa.query(DbTableTipoDespesa.ID_COLUMN,DbTableTipoDespesa.CATEGORIA_DESPESAS+"=?",new String[]{categoria},null,null,null);

        int id = DbTableTipoDespesa.getIdCategoriaDespesa(cursor);

        cursor.close();
        db.close();
        return id;
    }

    private void deleteCategoriaReceita(int id){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);
        dbTableTipoReceita.delete(DbTableTipoReceita._ID+"=?",new String[]{Integer.toString(id)});

        db.close();
    }

    private void deleteCategoriaDespesa(int id){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);
        dbTableTipoDespesa.delete(DbTableTipoDespesa._ID+"=?",new String[]{Integer.toString(id)});

        db.close();
    }

    private void updateListCategoriasReceitas(){
        //ListView Categorias Receitas
        ListAdapter listAdapterReceitas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getCategoriasReceitaFromDb());
        ListView listViewReceitas = (ListView) findViewById(R.id.listViewReceitas);
        listViewReceitas.setAdapter(listAdapterReceitas);
    }

    private void updateListCategoriasDespesas(){
        //ListView Categorias Despesas
        ListAdapter listAdapterDespesas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getCategoriasDespesaFromDb());
        ListView listViewDespesas = (ListView) findViewById(R.id.listViewDespesas);
        listViewDespesas.setAdapter(listAdapterDespesas);
    }
}
