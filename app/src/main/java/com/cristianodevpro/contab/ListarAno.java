package com.cristianodevpro.contab;

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
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListarAno extends AppCompatActivity {

    /***********************Variáveis***************************/
    private RegistoMovimentosAdapter adapter;
    private RecyclerView recyclerViewListarAno;
    private Spinner spinnerAnoListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ano);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /****************Construção dos objetos********************/
        spinnerAnoListar = (Spinner) findViewById(R.id.spinnerAnosListar);
        recyclerViewListarAno = (RecyclerView) findViewById(R.id.recyclerViewListarAno);

        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponents();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /***************************************Functions and Methods****************************************/

    /**
     * carrega os dados para o spinner dos anos
     */
    private void loadSpinnerDataAno(){
        List<String> list = new ArrayList<>();
        for (int i = 2018; i < 2031; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnoListar.setAdapter(adapter);
    }

    /**
     * atualiza recycler view quando é selecionado um item no spinner
     */
    private void atualizaLista(){
        spinnerAnoListar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());

                DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(ListarAno.this);
                adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosAno(ano), ListarAno.this, recyclerViewListarAno);

                if (dbContabOpenHelper.getListRegistoMovimentosAno(ano).size()==0){ //se não devolver registos
                    Toast.makeText(getApplicationContext(),getString(R.string.nao_foram_encontrados_registos)+" "+ano, Toast.LENGTH_LONG).show();
                }

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

    /**
     * @return ano atual
     */
    private int getCurrentYear(){
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        return ano;
    }

    /**
     * @param spinner
     * @param value
     *
     * colocar no spinner um valor definido
     */
    private void setSpinnerToValue (Spinner spinner, String value){
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)){
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }

    /**
     * carregar/atualizar recycler view com os dados
     */
    private void initComponents(){
        loadSpinnerDataAno(); //carregar dados para o Spinner

        int anoSpinner = getCurrentYear();
        setSpinnerToValue(spinnerAnoListar, Integer.toString(anoSpinner));

        int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(this);
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosAno(ano), this, recyclerViewListarAno);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListarAno.setLayoutManager(layoutManager);
        recyclerViewListarAno.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListarAno.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewListarAno.setAdapter(adapter);

        atualizaLista();
    }
}
