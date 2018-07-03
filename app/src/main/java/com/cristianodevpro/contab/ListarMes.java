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

public class ListarMes extends AppCompatActivity {

    /************************Variáveis**********************/

    private RegistoMovimentosAdapter adapter;
    private RecyclerView recyclerViewListarMes;
    private Spinner spinnerAnoListar;
    private Spinner spinnerMesListar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_mes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*******************Construção dos objetos******************************/

        spinnerAnoListar = (Spinner) findViewById(R.id.spinnerAnosMesListar);
        spinnerMesListar = (Spinner) findViewById(R.id.spinnerMesListar);
        recyclerViewListarMes = (RecyclerView) findViewById(R.id.recyclerViewListarMes);

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
     * carrega os dados para o spinner dos meses
     */
    private void loadSpinnerDataMes(){
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.janeiro));
        list.add(getString(R.string.fevereiro));
        list.add(getString(R.string.marco));
        list.add(getString(R.string.abril));
        list.add(getString(R.string.maio));
        list.add(getString(R.string.junho));
        list.add(getString(R.string.julho));
        list.add(getString(R.string.agosto));
        list.add(getString(R.string.setembro));
        list.add(getString(R.string.outubro));
        list.add(getString(R.string.novembro));
        list.add(getString(R.string.dezembro));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMesListar.setAdapter(adapter);
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
     * atualiza recycler view quando é selecionado um item no spinner
     */
    private void atualizaLista(){
        spinnerAnoListar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());
                int mes = spinnerMesListar.getSelectedItemPosition()+1;
                String mesText = spinnerMesListar.getSelectedItem().toString();

                DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(ListarMes.this);
                adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosMes(mes, ano), ListarMes.this, recyclerViewListarMes);

                if (dbContabOpenHelper.getListRegistoMovimentosMes(mes, ano).size()==0){ //se não devolver registos
                    Toast.makeText(getApplicationContext(),getString(R.string.nao_foram_encontrados_registos_para)+" "+mesText+" "+getString(R.string.de)+" "+ano, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),getString(R.string.nao_foram_encontrados_registos_para)+" "+mesText+" "+getString(R.string.de)+" "+ano, Toast.LENGTH_LONG).show();
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

    /**
     * @return mes atual
     */
    private int getCurrentMonth(){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        return month;
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
     * @param mes
     * @return String do mes
     */
    private String mesToString(int mes){
        switch (mes){
            case 1:
                return getString(R.string.janeiro);
            case 2:
                return getString(R.string.fevereiro);
            case 3:
                return getString(R.string.marco);
            case 4:
                return getString(R.string.abril);
            case 5:
                return getString(R.string.maio);
            case 6:
                return getString(R.string.junho);
            case 7:
                return getString(R.string.julho);
            case 8:
                return getString(R.string.agosto);
            case 9:
                return getString(R.string.setembro);
            case 10:
                return getString(R.string.outubro);
            case 11:
                return getString(R.string.novembro);
            case 12:
                return getString(R.string.dezembro);
        }
        return null;
    }

    /**
     * Inicializa todos os componentes
     */
    private void initComponents(){

        loadSpinnerDataAno(); //carregar dados para o Spinner
        loadSpinnerDataMes(); //carregas os meses para o Spinner

        int mesSpinner = getCurrentMonth();
        setSpinnerToValue(spinnerMesListar, mesToString(mesSpinner));

        int anoSpinner = getCurrentYear();
        setSpinnerToValue(spinnerAnoListar,Integer.toString(anoSpinner));

        int ano = Integer.parseInt(spinnerAnoListar.getSelectedItem().toString());
        int mes = spinnerMesListar.getSelectedItemPosition()+1;

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(this);
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosMes(mes, ano), this, recyclerViewListarMes);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListarMes.setLayoutManager(layoutManager);
        recyclerViewListarMes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListarMes.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewListarMes.setAdapter(adapter);

        atualizaLista();
    }
}
