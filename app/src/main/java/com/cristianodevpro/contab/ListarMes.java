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

import javax.net.ssl.SSLPeerUnverifiedException;

public class ListarMes extends AppCompatActivity {

    private RegistoMovimentosAdapter adapter;
    private RecyclerView recyclerViewListarMes;
    private Spinner spinnerAnoListar;
    private Spinner spinnerMesListar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_mes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadSpinnerDataAno(); //carregar dados para o Spinner
        loadSpinnerDataMes(); //carregas os meses para o Spinner

        spinnerAnoListar = (Spinner) findViewById(R.id.spinnerAnosMesListar);
        spinnerMesListar = (Spinner) findViewById(R.id.spinnerMesListar);

        int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());
        int mes = spinnerMesListar.getSelectedItemPosition()+1;

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(this);
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosMes(mes, ano), this, recyclerViewListarMes);

        recyclerViewListarMes = (RecyclerView) findViewById(R.id.recyclerViewListarMes);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListarMes.setLayoutManager(layoutManager);
        recyclerViewListarMes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListarMes.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewListarMes.setAdapter(adapter);

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
        spinnerAnoListar = (Spinner) findViewById(R.id.spinnerAnosMesListar);
        List<String> list = new ArrayList<>();
        for (int i = 2018; i < 2031; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnoListar.setAdapter(adapter);
    }

    public void loadSpinnerDataMes(){ //carrega os dados para o spinner dos meses
        spinnerMesListar = (Spinner) findViewById(R.id.spinnerMesListar);
        List<String> list = new ArrayList<>();
        list.add("janeiro");
        list.add("fevereiro");
        list.add("março");
        list.add("abril");
        list.add("maio");
        list.add("junho");
        list.add("julho");
        list.add("agosto");
        list.add("setembro");
        list.add("outubro");
        list.add("novembro");
        list.add("dezembro");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMesListar.setAdapter(adapter);
    }

    private void atualizaLista(){ //atualiza recyclerview quando é selecionado um item no spinner
        spinnerAnoListar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());
                int mes = spinnerMesListar.getSelectedItemPosition()+1;
                String mesText = spinnerMesListar.getSelectedItem().toString();

                DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(ListarMes.this);
                adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosMes(mes, ano), ListarMes.this, recyclerViewListarMes);

                if (dbContabOpenHelper.getListRegistoMovimentosMes(mes, ano).size()==0){ //se não devolver registos
                    Toast.makeText(getApplicationContext(),"Não foram encontrados registos para "+mesText+" de "+ano, Toast.LENGTH_LONG).show();
                }

                recyclerViewListarMes = (RecyclerView) findViewById(R.id.recyclerViewListarMes);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(ListarMes.this);
                recyclerViewListarMes.setLayoutManager(layoutManager);
                recyclerViewListarMes.setItemAnimator(new DefaultItemAnimator());
                recyclerViewListarMes.addItemDecoration(new DividerItemDecoration(ListarMes.this, LinearLayoutManager.VERTICAL));
                recyclerViewListarMes.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMesListar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());
                int mes = spinnerMesListar.getSelectedItemPosition()+1;
                String mesText = spinnerMesListar.getSelectedItem().toString();

                DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(ListarMes.this);
                adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosMes(mes, ano), ListarMes.this, recyclerViewListarMes);

                if (dbContabOpenHelper.getListRegistoMovimentosMes(mes, ano).size()==0){ //se não devolver registos
                    Toast.makeText(getApplicationContext(),"Não foram encontrados registos para "+mesText+" de "+ano, Toast.LENGTH_LONG).show();
                }

                recyclerViewListarMes = (RecyclerView) findViewById(R.id.recyclerViewListarMes);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(ListarMes.this);
                recyclerViewListarMes.setLayoutManager(layoutManager);
                recyclerViewListarMes.setItemAnimator(new DefaultItemAnimator());
                recyclerViewListarMes.addItemDecoration(new DividerItemDecoration(ListarMes.this, LinearLayoutManager.VERTICAL));
                recyclerViewListarMes.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
