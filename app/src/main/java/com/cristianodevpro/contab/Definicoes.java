package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Definicoes extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Spinner spinnerAnos;
    Spinner spinnerAno2;
    Spinner spinnerMes;
    RadioGroup radioGroup;
    RelativeLayout relativeLayoutAno;
    RelativeLayout relativeLayoutMes;
    RelativeLayout relativeLayoutAno2;
    ImageButton imageButtonSelectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerAnos = (Spinner) findViewById(R.id.spinnerAnos);
        spinnerAno2 = (Spinner) findViewById(R.id.spinnerAno2);
        spinnerMes = (Spinner) findViewById(R.id.spinnerMes);
        relativeLayoutAno = (RelativeLayout) findViewById(R.id.relativeLayoutAno);
        relativeLayoutMes = (RelativeLayout) findViewById(R.id.relativeLayoutMes);
        relativeLayoutAno2 = (RelativeLayout) findViewById(R.id.relativeLayoutAno2);
        imageButtonSelectDate = (ImageButton) findViewById(R.id.imageButtonSelectDate);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        //Hide spinners and layouts
        spinnerAnos.setVisibility(View.INVISIBLE);
        spinnerAno2.setVisibility(View.INVISIBLE);
        spinnerMes.setVisibility(View.INVISIBLE);
        relativeLayoutAno.setVisibility(View.INVISIBLE);
        relativeLayoutMes.setVisibility(View.INVISIBLE);
        relativeLayoutAno2.setVisibility(View.INVISIBLE);
        imageButtonSelectDate.setVisibility(View.INVISIBLE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                View radioButton = radioGroup.findViewById(checkedId);
                int radioSelected = radioGroup.indexOfChild(radioButton); //index dos radio buttons

                switch (radioSelected){
                    case 0: //Resultados Ano
                        loadSpinnerDataAno();
                        spinnerAnos.setVisibility(View.VISIBLE);
                        spinnerAno2.setVisibility(View.INVISIBLE);
                        spinnerMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno.setVisibility(View.VISIBLE);
                        relativeLayoutMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno2.setVisibility(View.INVISIBLE);
                        imageButtonSelectDate.setVisibility(View.INVISIBLE);
                        break;
                    case 1: //Resultados Mes
                        loadSpinnerDataMes();
                        loadSpinnerDataAno2();
                        spinnerAnos.setVisibility(View.INVISIBLE);
                        spinnerAno2.setVisibility(View.VISIBLE);
                        spinnerMes.setVisibility(View.VISIBLE);
                        relativeLayoutAno.setVisibility(View.INVISIBLE);
                        relativeLayoutMes.setVisibility(View.VISIBLE);
                        relativeLayoutAno2.setVisibility(View.VISIBLE);
                        imageButtonSelectDate.setVisibility(View.INVISIBLE);
                        break;
                    case 2: //Resultados Dia
                        spinnerAnos.setVisibility(View.INVISIBLE);
                        spinnerAno2.setVisibility(View.INVISIBLE);
                        spinnerMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno.setVisibility(View.INVISIBLE);
                        relativeLayoutMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno2.setVisibility(View.INVISIBLE);
                        imageButtonSelectDate.setVisibility(View.VISIBLE);
                        break;
                    case 3: //Todos
                        spinnerAnos.setVisibility(View.INVISIBLE);
                        spinnerAno2.setVisibility(View.INVISIBLE);
                        spinnerMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno.setVisibility(View.INVISIBLE);
                        relativeLayoutMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno2.setVisibility(View.INVISIBLE);
                        imageButtonSelectDate.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });


        spinnerAnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int ano = Integer.parseInt(spinnerAnos.getSelectedItem().toString());
                Toast.makeText(Definicoes.this, "Ano selecionado: "+ano,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        TextView textViewShowValorOrcamento = (TextView) findViewById(R.id.textViewShowValorOrcamento);
        double valorOrcamento = getValorOrcamentoFromDb();
        textViewShowValorOrcamento.setText(""+valorOrcamento+"€");
    }

    public void selectDia(View view) {
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

        int dia = dayOfMonth;
        int mes = month;
        int ano = year;
    }


    public void loadSpinnerDataAno(){
        Spinner spinnerAnos = (Spinner) findViewById(R.id.spinnerAnos);
        List<String> list = new ArrayList<>();
        for (int i = 2018; i < 2030; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnos.setAdapter(adapter);
    }

    public void loadSpinnerDataAno2(){
        Spinner spinnerAno2 = (Spinner) findViewById(R.id.spinnerAno2);
        List<String> list = new ArrayList<>();
        for (int i = 2018; i < 2030; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAno2.setAdapter(adapter);
    }

    public void loadSpinnerDataMes(){
        Spinner spinnerMes = (Spinner) findViewById(R.id.spinnerMes);
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
        spinnerMes.setAdapter(adapter);
    }

    //Teste
    public double getValorOrcamentoFromDb(){ //Obter o ultimo valor de orçamento definido
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableOrcamento dbTableOrcamento = new DbTableOrcamento(db);

        Cursor cursor = dbTableOrcamento.query(DbTableOrcamento.VALOR_COLUMN, DbTableOrcamento._ID+"= (SELECT MAX(id_orcamento) FROM Orcamento)", null, null, null, null);

        double valor = 0;
        valor = DbTableOrcamento.getValorOrcamentoFromDb(cursor);

        cursor.close();
        db.close();
        return valor;
    }


}
