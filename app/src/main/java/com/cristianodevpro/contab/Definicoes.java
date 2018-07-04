package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Definicoes extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //Variáveis
    private Spinner spinnerAnos;
    private Spinner spinnerAno2;
    private Spinner spinnerMes;
    private RadioGroup radioGroup;
    private RelativeLayout relativeLayoutAno;
    private RelativeLayout relativeLayoutMes;
    private RelativeLayout relativeLayoutAno2;
    private ImageButton imageButtonSelectDate;
    private TextView textViewShowSelectDateDefinicoes;
    private TextView textViewShowValorOrcamento;
    private RadioButton radioButtonTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Construção dos objetos
        spinnerAnos = (Spinner) findViewById(R.id.spinnerAnos);
        spinnerAno2 = (Spinner) findViewById(R.id.spinnerAno2);
        spinnerMes = (Spinner) findViewById(R.id.spinnerMes);
        relativeLayoutAno = (RelativeLayout) findViewById(R.id.relativeLayoutAno);
        relativeLayoutMes = (RelativeLayout) findViewById(R.id.relativeLayoutMes);
        relativeLayoutAno2 = (RelativeLayout) findViewById(R.id.relativeLayoutAno2);
        imageButtonSelectDate = (ImageButton) findViewById(R.id.imageButtonSelectDateListar);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        textViewShowSelectDateDefinicoes = (TextView) findViewById(R.id.textViewShowSelectDateDefinicoes);
        textViewShowValorOrcamento = (TextView) findViewById(R.id.textViewShowValorOrcamento);
        radioButtonTodos = (RadioButton) findViewById(R.id.radioButtonGeral);

        //Hide spinners and layouts
        spinnerAnos.setVisibility(View.INVISIBLE);
        spinnerAno2.setVisibility(View.INVISIBLE);
        spinnerMes.setVisibility(View.INVISIBLE);
        relativeLayoutAno.setVisibility(View.INVISIBLE);
        relativeLayoutMes.setVisibility(View.INVISIBLE);
        relativeLayoutAno2.setVisibility(View.INVISIBLE);
        imageButtonSelectDate.setVisibility(View.INVISIBLE);
        textViewShowSelectDateDefinicoes.setVisibility(View.INVISIBLE);

        //Ações quando se clica num radio button
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                View radioButton = radioGroup.findViewById(checkedId);
                int radioSelected = radioGroup.indexOfChild(radioButton); //index dos radio buttons

                switch (radioSelected){
                    case 0: //Resultados Ano
                        loadSpinnerDataAno();
                        int anoSpinner = getCurrentYear();
                        setSpinnerToValue(spinnerAnos,Integer.toString(anoSpinner));
                        spinnerAnos.setVisibility(View.VISIBLE);
                        spinnerAno2.setVisibility(View.INVISIBLE);
                        spinnerMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno.setVisibility(View.VISIBLE);
                        relativeLayoutMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno2.setVisibility(View.INVISIBLE);
                        imageButtonSelectDate.setVisibility(View.INVISIBLE);
                        textViewShowSelectDateDefinicoes.setVisibility(View.INVISIBLE);
                        break;
                    case 1: //Resultados Mes
                        loadSpinnerDataMes();
                        loadSpinnerDataAno2();
                        int mesSpinner = getCurrentMonth();
                        setSpinnerToValue(spinnerMes, mesToString(mesSpinner));
                        int anoSpinner2 = getCurrentYear();
                        setSpinnerToValue(spinnerAno2,Integer.toString(anoSpinner2));
                        spinnerAnos.setVisibility(View.INVISIBLE);
                        spinnerAno2.setVisibility(View.VISIBLE);
                        spinnerMes.setVisibility(View.VISIBLE);
                        relativeLayoutAno.setVisibility(View.INVISIBLE);
                        relativeLayoutMes.setVisibility(View.VISIBLE);
                        relativeLayoutAno2.setVisibility(View.VISIBLE);
                        imageButtonSelectDate.setVisibility(View.INVISIBLE);
                        textViewShowSelectDateDefinicoes.setVisibility(View.INVISIBLE);
                        break;
                    case 2: //Resultados Dia
                        spinnerAnos.setVisibility(View.INVISIBLE);
                        spinnerAno2.setVisibility(View.INVISIBLE);
                        spinnerMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno.setVisibility(View.INVISIBLE);
                        relativeLayoutMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno2.setVisibility(View.INVISIBLE);
                        imageButtonSelectDate.setVisibility(View.VISIBLE);
                        textViewShowSelectDateDefinicoes.setVisibility(View.VISIBLE);
                        textViewShowSelectDateDefinicoes.setText(""+getCurrentDay()+"/"+getCurrentMonth()+"/"+getCurrentYear());
                        DadosDefinicoesToMain.setDia(getCurrentDay());
                        DadosDefinicoesToMain.setMes(getCurrentMonth());
                        DadosDefinicoesToMain.setAno(getCurrentYear());
                        DadosDefinicoesToMain.setTipo("dia");
                        break;
                    case 3: //Todos
                        spinnerAnos.setVisibility(View.INVISIBLE);
                        spinnerAno2.setVisibility(View.INVISIBLE);
                        spinnerMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno.setVisibility(View.INVISIBLE);
                        relativeLayoutMes.setVisibility(View.INVISIBLE);
                        relativeLayoutAno2.setVisibility(View.INVISIBLE);
                        imageButtonSelectDate.setVisibility(View.INVISIBLE);
                        textViewShowSelectDateDefinicoes.setVisibility(View.INVISIBLE);
                        DadosDefinicoesToMain.setTipo("todos");
                        break;
                }
            }
        });

        //Mostra valor do orçamento
        double valorOrcamento = getValorOrcamentoFromDb();
        textViewShowValorOrcamento.setText(""+String.format("%.2f",valorOrcamento)+"€");

        if(radioButtonTodos.isChecked()){
            DadosDefinicoesToMain.setTipo("todos");
        }

        //Enviar dados para a Main Activity aquando da seleção de um item num spinner
        spinnerAnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Enviar dados para a atividade Main
                int ano = Integer.parseInt(spinnerAnos.getSelectedItem().toString());
                DadosDefinicoesToMain.setAno(ano);
                DadosDefinicoesToMain.setTipo("ano");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerAno2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Enviar dados para a atividade Main
                int ano = Integer.parseInt(spinnerAno2.getSelectedItem().toString());
                DadosDefinicoesToMain.setAno(ano);
                DadosDefinicoesToMain.setTipo("mes");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Enviar dados para a atividade Main
                int mes = spinnerMes.getSelectedItemPosition()+1;
                DadosDefinicoesToMain.setMes(mes);
                DadosDefinicoesToMain.setTipo("mes");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /***************************************buttons actions*****************************************/

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
    } //Botão back do telemovel

    public void selectDia(View view) { //Botão "SetDate"
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) { //Ação do botão "OK" do DatePicker
        //Código que obtém a data selecionada pelo utilizador
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        month++;

        //Enviar dados para a atividade Main
        DadosDefinicoesToMain.setDia(dayOfMonth);
        DadosDefinicoesToMain.setMes(month);
        DadosDefinicoesToMain.setAno(year);
        DadosDefinicoesToMain.setTipo("dia");

        textViewShowSelectDateDefinicoes.setText(""+dayOfMonth+"/"+month+"/"+year);
    }

    /*******************************************Functions and Methods************************************************/

    public void loadSpinnerDataAno(){ //carrega os dados para o spinner dos anos
        List<String> list = new ArrayList<>();
        for (int i = 2018; i < 2031; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnos.setAdapter(adapter);
    }

    public void loadSpinnerDataAno2(){ //carrega os dados para o spinner dos anos2
        List<String> list = new ArrayList<>();
        for (int i = 2018; i < 2031; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAno2.setAdapter(adapter);
    }

    public void loadSpinnerDataMes(){ //carrega os dados para o spinner dos meses
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
        spinnerMes.setAdapter(adapter);
    }

    /**
     * @return last defined budged value
     */
    public double getValorOrcamentoFromDb(){
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

    /**
     * @return current day (int)
     */
    private int getCurrentDay(){ //Data atual
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        return dia;
    }

    /**
     * @return current month (int)
     */
    private int getCurrentMonth(){ //Data atual
        Calendar c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH)+1;
        return mes;
    }

    /**
     * @return current year (int)
     */
    private int getCurrentYear(){ //Data atual
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        return ano;
    }

    /**
     * @param spinner
     * @param value
     */
    private void setSpinnerToValue (Spinner spinner, String value){ //colocar no spinner um valor definido
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
     * @param mes
     * @return nome do mes
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



}
