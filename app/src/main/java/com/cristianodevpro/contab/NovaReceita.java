package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;

public class NovaReceita extends AppCompatActivity{

    Calendar c;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_receita);

        //Declarações de objetos
        ImageButton datePickerButton = (ImageButton) findViewById(R.id.datePickerButton);
        final EditText editTextDesignacao = (EditText) findViewById(R.id.editTextDesignacao);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                //datePicker.show(getSupportFragmentManager(), "datePicker");
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(NovaReceita.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {
                            editTextDesignacao.setText(Day + "/" + (Month+1) + "/" + Year);
                    }
                }, day, month, year);
                datePickerDialog.show();
            }
        });
    }

    public void definirData(View view) {

    }

    /*
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //Atribuir às variáveis dia, mes e ano
    }*/
}
