package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

public class NovaReceita extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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

        //Atribuir às variáveis dia, mes e ano
        EditText editTextDesignacao = (EditText) findViewById(R.id.editTextDesignacao);

        editTextDesignacao.setText(""+dayOfMonth+"/"+month+"/"+year);
    }

    public void addCategoria(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NovaReceita.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_inserir_categoria, null);
        final TextInputEditText textInputEditText = (TextInputEditText) findViewById(R.id.textInputCategoriaReceitas);
        final Button buttonInserirCategoriasReceitas = (Button) findViewById(R.id.buttonInserirCategoriaReceitas);

        buttonInserirCategoriasReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verificar se o campo está vazio
                if (!textInputEditText.getText().toString().isEmpty()){
                    //atribuir à variavel categoriaReceita e inserir na BD
                    Toast.makeText(NovaReceita.this, "Inserido!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(NovaReceita.this, "Preencha o campo!", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
