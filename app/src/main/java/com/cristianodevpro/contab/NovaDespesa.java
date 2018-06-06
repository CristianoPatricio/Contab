package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NovaDespesa extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogFragmentCategoria.ExampleDialogListener {

    public static final String DESPESA = "Despesa";
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

    public String getNowDate(){
        //Data e Hora
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
        SimpleDateFormat horaFormat = new SimpleDateFormat("HHmmss");

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Date now_date = cal.getTime();

        String dataDb = dateFormat.format(now_date);
        String horaDb = horaFormat.format(now_date);

        String concatDate = dataDb+horaDb;

        return concatDate;
    }

    public void setDefaultDateDb(){

        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH) + 1;
        int dia = c.get(Calendar.DAY_OF_MONTH);

        registoMovimentos.setAno(ano);
        registoMovimentos.setMes(mes);
        registoMovimentos.setDia(dia);

    }

    public void addCategoriaDespesa(View view) {
        DialogFragmentCategoria dialogFragmentCategoria = new DialogFragmentCategoria();
        dialogFragmentCategoria.show(getSupportFragmentManager(), "DialogFragmentDespesas");
    }

    @Override
    public void setTexts(String categoria) {
        //Teste spinner
        List<String> list = new ArrayList<String>();
        list.add(categoria);
        Spinner spinnerCategoriaDespesa = (Spinner) findViewById(R.id.spinnerCategoriaDespesa);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaDespesa.setAdapter(adapter);
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

    public void inserirDespesaDb(View view) {
        //Declaração de objetos
        final EditText editTextDesignacaoDespesa = (EditText) findViewById(R.id.editTextDesignacaoDespesa);
        EditText editTextValorDespesa = (EditText) findViewById(R.id.editTextValorDespesa);
        Spinner spinnerCategoriaDespesa = (Spinner) findViewById(R.id.spinnerCategoriaDespesa);

        //Verificar se o campo valor foi preenchido
        double valor = 0;
        try {
            valor = Double.parseDouble(editTextValorDespesa.getText().toString());
        } catch (NumberFormatException e) {
            editTextValorDespesa.setError("Insira um valor!");
            editTextValorDespesa.requestFocus();
            return;
        }

        //Se o botão "definirData" não foi clicado
        if (!isClicked){
            setDefaultDateDb();
        }


        double valorDespesa = Double.parseDouble(editTextValorDespesa.getText().toString());
        String designacaoDespesa = editTextDesignacaoDespesa.getText().toString();
        String tipoDespesa = spinnerCategoriaDespesa.getSelectedItem().toString();


        registoMovimentos.setId_movimento(getNowDate());
        registoMovimentos.setReceitadespesa(DESPESA);
        registoMovimentos.setDesignacao(designacaoDespesa);
        registoMovimentos.setValor(valorDespesa);
        registoMovimentos.setTiporeceita("");
        registoMovimentos.setTipodespesa(tipoDespesa);

        //Teste
        TextView textViewDataReadyInsertDb = (TextView) findViewById(R.id.textViewDataReadyInsertDb);
        textViewDataReadyInsertDb.setText(""+registoMovimentos.getId_movimento()+"-"+registoMovimentos.getDia()+"-"+registoMovimentos.getMes()+"-"+registoMovimentos.getAno()+"-"+registoMovimentos.getReceitadespesa()+"-"+registoMovimentos.getDesignacao()+"-"+registoMovimentos.getValor()+"-"+registoMovimentos.getTipodespesa());

        isClicked = false;
    }
}
