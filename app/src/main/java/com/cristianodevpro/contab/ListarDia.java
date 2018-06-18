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
    private ImageButton imageButtonSelectDateListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_dia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageButtonSelectDateListar = (ImageButton) findViewById(R.id.imageButtonSelectDateListar);
        textViewSelectDate = (TextView) findViewById(R.id.textViewSelectDateListar);

        int dia = getCurrentDay();
        int mes = getCurrentMonth();
        int ano = getCurrentYear();

        textViewSelectDate.setText(""+dia+"/"+mes+"/"+ano);

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(this);
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosDia(dia,mes,ano), this, recyclerViewListarDia);

        //verificar se há registos
        if (dbContabOpenHelper.getListRegistoMovimentosDia(dia,mes,ano).size()==0){
            Toast.makeText(this, "Não foram encontrados registos para a data de "+textViewSelectDate.getText(), Toast.LENGTH_LONG).show();
        }

        recyclerViewListarDia = (RecyclerView) findViewById(R.id.recyclerViewListarDia);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListarDia.setLayoutManager(layoutManager);
        recyclerViewListarDia.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListarDia.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewListarDia.setAdapter(adapter);
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
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(this);
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentosDia(dayOfMonth,month,year), this, recyclerViewListarDia);

        //verificar se há registos
        if (dbContabOpenHelper.getListRegistoMovimentosDia(dayOfMonth,month,year).size()==0){
            Toast.makeText(this, "Não foram encontrados registos para a data de "+textViewSelectDate.getText(), Toast.LENGTH_LONG).show();
        }

        recyclerViewListarDia = (RecyclerView) findViewById(R.id.recyclerViewListarDia);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListarDia.setLayoutManager(layoutManager);
        recyclerViewListarDia.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListarDia.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewListarDia.setAdapter(adapter);

        //Mostra data selecionada na textview ao lado do botão
        textViewSelectDate.setText(""+dayOfMonth+"/"+month+"/"+year);
    }

    /**************************************Functions***********************************************/

    private int getCurrentDay(){ //Data atual
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        return dia;
    }

    private int getCurrentMonth(){ //Data atual
        Calendar c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH)+1;
        return mes;
    }

    private int getCurrentYear(){ //Data atual
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        return ano;
    }

}
