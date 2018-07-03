package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ListarDia extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private RegistoMovimentosAdapter adapter;
    private RecyclerView recyclerViewListarDia;
    private TextView textViewSelectDate;
    private DbContabOpenHelper dbContabOpenHelper;
    private ImageButton imageButtonSelectDateListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_dia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**********************Construção dos objetos************************/
        dbContabOpenHelper = new DbContabOpenHelper(this);
        recyclerViewListarDia = (RecyclerView) findViewById(R.id.recyclerViewListarDia);
        imageButtonSelectDateListar = (ImageButton) findViewById(R.id.imageButtonSelectDateListar);
        textViewSelectDate = (TextView) findViewById(R.id.textViewSelectDateListar);

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

    /**********************************Buttons actions***************************************************/

    public void selectDia(View view) { //definir Data
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        //Código que obtém a data selecionada pelo utilizador
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        month++;

        //Atualiza dados no recyclerview
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosDia(dayOfMonth,month,year), this, recyclerViewListarDia);

        //verificar se há registos
        if (dbContabOpenHelper.getListRegistoMovimentosDia(dayOfMonth,month,year).size()==0){
            Toast.makeText(this, getString(R.string.nao_foram_encontrados_registos_data)+" "+dayOfMonth+"/"+month+"/"+year, Toast.LENGTH_LONG).show();
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListarDia.setLayoutManager(layoutManager);
        recyclerViewListarDia.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListarDia.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewListarDia.setAdapter(adapter);

        //Mostra data selecionada na textview ao lado do botão
        textViewSelectDate.setText(""+dayOfMonth+"/"+month+"/"+year);
    }

    /**************************************Functions***********************************************/

    /**
     * @return dia atual
     */
    private int getCurrentDay(){
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        return dia;
    }

    /**
     * @return mes atual
     */
    private int getCurrentMonth(){
        Calendar c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH)+1;
        return mes;
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
     * carregar/atualizar recycler view com os dados
     */
    private void initComponents(){
        int dia = getCurrentDay();
        int mes = getCurrentMonth();
        int ano = getCurrentYear();

        textViewSelectDate.setText(""+dia+"/"+mes+"/"+ano);

        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosDia(dia,mes,ano), this, recyclerViewListarDia);

        //verificar se há registos
        if (dbContabOpenHelper.getListRegistoMovimentosDia(dia,mes,ano).size()==0){
            Toast.makeText(this,getString(R.string.nao_foram_encontrados_registos_data)+" "+textViewSelectDate.getText(), Toast.LENGTH_LONG).show();
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListarDia.setLayoutManager(layoutManager);
        recyclerViewListarDia.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListarDia.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewListarDia.setAdapter(adapter);
    }

}
