package com.cristianodevpro.contab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListarAno extends AppCompatActivity {

    private RegistoMovimentosAdapter adapter;
    private RecyclerView recyclerViewListarAno;
    private Spinner spinnerAnoListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ano);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadSpinnerDataAno(); //carregar dados para o Spinner

        spinnerAnoListar = (Spinner) findViewById(R.id.spinnerAnosListar);

        int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(this);
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosAno(ano), this, recyclerViewListarAno);

        recyclerViewListarAno = (RecyclerView) findViewById(R.id.recyclerViewListarAno);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListarAno.setLayoutManager(layoutManager);
        recyclerViewListarAno.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListarAno.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewListarAno.setAdapter(adapter);

        atualizaLista();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /***************************************Functions and Methods****************************************/

    public void loadSpinnerDataAno(){ //carrega os dados para o spinner dos anos
        Spinner spinnerAnosListar = (Spinner) findViewById(R.id.spinnerAnosListar);
        List<String> list = new ArrayList<>();
        for (int i = 2018; i < 2031; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnosListar.setAdapter(adapter);
    }

    private void atualizaLista(){ //atualiza recyclerview quando é selecionado um item no spinner
        spinnerAnoListar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());

                DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(ListarAno.this);
                adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosAno(ano), ListarAno.this, recyclerViewListarAno);

                if (dbContabOpenHelper.getListRegistoMovimentosAno(ano).size()==0){ //se não devolver registos
                    Toast.makeText(getApplicationContext(),"Não foram encontrados registos para o ano de "+ano, Toast.LENGTH_LONG).show();
                }

                recyclerViewListarAno = (RecyclerView) findViewById(R.id.recyclerViewListarAno);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(ListarAno.this);
                recyclerViewListarAno.setLayoutManager(layoutManager);
                recyclerViewListarAno.setItemAnimator(new DefaultItemAnimator());
                recyclerViewListarAno.addItemDecoration(new DividerItemDecoration(ListarAno.this, LinearLayoutManager.VERTICAL));
                recyclerViewListarAno.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
