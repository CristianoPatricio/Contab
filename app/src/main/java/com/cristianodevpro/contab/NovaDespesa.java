package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class NovaDespesa extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static Boolean isClicked = false;

    RegistoMovimentos registoMovimentos = new RegistoMovimentos();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_despesa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void inserirDespesaDb(View view) {
    }

    public void addCategoriaDespesa(View view) {
    }

    public void definirDataDespesa(View view) {
        DialogFragment newFragment = new DatePickerFragment();
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

        registoMovimentos.setDia(dayOfMonth);
        registoMovimentos.setMes(month);
        registoMovimentos.setAno(year);

        TextView textViewSelectedDateDespesa = (TextView) findViewById(R.id.textViewSelectedDateDespesa);
        textViewSelectedDateDespesa.setText(""+dayOfMonth+"/"+month+"/"+year);

        isClicked = true;
    }
}
