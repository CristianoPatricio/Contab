package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NovaReceita extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogFragmentCategoria.ExampleDialogListener{

    public static final String RECEITA = "Receita";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_receita);

    }

    public void definirData(View view) {
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Código que obtém a data selecionada pelo utilizador
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        month++;

        RegistoMovimentos registoMovimentos = new RegistoMovimentos();

        registoMovimentos.setDia(dayOfMonth);
        registoMovimentos.setMes(month);
        registoMovimentos.setAno(year);

        //Teste
        EditText editTextDesignacao = (EditText) findViewById(R.id.editTextDesignacaoReceita);
        editTextDesignacao.setText(""+dayOfMonth+"/"+month+"/"+year);
    }

    public void addCategoria(View view) {
        DialogFragmentCategoria dialogFragmentCategoria = new DialogFragmentCategoria();
        dialogFragmentCategoria.show(getSupportFragmentManager(), "DialogFragmentCategoria");
    }

    @Override
    public void setTexts(String categoria) {
        //Teste
        EditText editTextDesignacao = (EditText) findViewById(R.id.editTextDesignacaoReceita);
        editTextDesignacao.setText(""+categoria);
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

        RegistoMovimentos registoMovimentos = new RegistoMovimentos();

        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH) + 1;
        int dia = c.get(Calendar.DAY_OF_MONTH);

        registoMovimentos.setAno(ano);
        registoMovimentos.setMes(mes);
        registoMovimentos.setDia(dia);
    }

    public void inserirReceitaDb(View view) {

        //Declaração de objetos
        TextInputLayout editTextDesignacaoReceita = (TextInputLayout) findViewById(R.id.editTextDesignacaoReceita);
        TextView editTextValorReceita = (TextView) findViewById(R.id.editTextValorReceita);
        Spinner spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        ImageButton datePickerButton = (ImageButton) findViewById(R.id.datePickerButton);

        RegistoMovimentos registoMovimentos = new RegistoMovimentos();

        //TODO verificar se todos os campos estão preechidos, incluindo a data, que se não for selecionada, será definida a do próprio dia.
        if (!editTextDesignacaoReceita.getEditText().toString().isEmpty()){
            Toast.makeText(NovaReceita.this, "Por favor, preencha todos os campos!", Toast.LENGTH_LONG).show();
        }

        if (!editTextValorReceita.getText().toString().isEmpty()){
            Toast.makeText(NovaReceita.this, "Por favor, preencha todos os campos!", Toast.LENGTH_LONG).show();
        }

        /*
        if (datePickerButton.){
            ;
        }else{
            setDefaultDateDb();
        }
        */

        double valorReceita = Double.parseDouble(editTextValorReceita.getText().toString());
        String designacaoReceita = editTextDesignacaoReceita.getEditText().toString();
        String tipoReceita = spinnerCategoria.getSelectedItem().toString();

        //Teste
        TextView textViewTestDate = (TextView) findViewById(R.id.textViewTestDate);
        textViewTestDate.setText(""+getNowDate());

        registoMovimentos.setId_movimento(getNowDate());
        registoMovimentos.setReceitadespesa(RECEITA);
        registoMovimentos.setDesignacao(designacaoReceita);
        registoMovimentos.setValor(valorReceita);
        registoMovimentos.setTiporeceita(tipoReceita);
        registoMovimentos.setTipodespesa("");

        //TODO criar função inserirDadosReceitaDb();
    }
}
