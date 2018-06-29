package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NovaReceita extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogFragmentCategoria.ExampleDialogListener{

    /*************Global Variables********************************************/
    public static final String RECEITA = "Receita";
    private static Boolean isClicked = false;
    RegistoMovimentos registoMovimentos = new RegistoMovimentos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_receita);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Apagar todos os registos da tabela TipoReceitas
        //DbContabOpenHelper db = new DbContabOpenHelper(getApplicationContext());
        //db.deleteAllCategoriaReceita();

        loadSpinnerData(); //Carrega categorias da DB para spinner

        setDefaultDateToTextView(); //Definir data atual no textview
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
       finish();
    }

    /************************Handle buttons actions**************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    public void cancel(View view) { //Botão "cancelar"
        finish();
    }

    public void definirData(View view) { //Botão "definir Data"
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

        registoMovimentos.setDia(dayOfMonth);
        registoMovimentos.setMes(month);
        registoMovimentos.setAno(year);

        isClicked = true;

        TextView textViewSelectedDate = (TextView) findViewById(R.id.textViewSelectedDate);
        textViewSelectedDate.setText(""+dayOfMonth+"/"+month+"/"+year);
    }

    public void addCategoria(View view) { //Botão "+"
        DialogFragmentCategoria dialogFragmentCategoria = new DialogFragmentCategoria();
        dialogFragmentCategoria.show(getSupportFragmentManager(), "DialogFragmentCategoria");
    }

    @Override
    public void setTexts(String categoria) { //Ação do botão Inserir Categoria

        try {
            if(checkCategoriaReceita(categoria) != -1) { //Se devolver um id != -1 é porque já exite uma categoria com o nome que vamos inserir
                Toast.makeText(NovaReceita.this, "Categoria já existente!",Toast.LENGTH_LONG).show();
                return;
            }
            insertCategoriaReceitaDb(categoria);
            Toast.makeText(NovaReceita.this, "Categoria inserida com sucesso!",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(NovaReceita.this, "Erro ao inserir a categoria na BD!",Toast.LENGTH_LONG).show();
        }

        loadSpinnerData(); //atualizar spinner

    }

    public void inserirReceitaDb(View view) { //Botão "inserir Receita"
        //Declaração de objetos
        final EditText editTextDesignacaoReceita = (EditText) findViewById(R.id.editTextDesignacaoReceita);
        EditText editTextValorReceita = (EditText) findViewById(R.id.editTextValorReceita);
        Spinner spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        TextView textViewSelectedDateReceita = (TextView) findViewById(R.id.textViewSelectedDate);

        //Verificar se o campo valor foi preenchido
        double valor = 0;
        try {
            valor = Double.parseDouble(editTextValorReceita.getText().toString());
        } catch (NumberFormatException e) {
            editTextValorReceita.setError("Insira um valor!");
            editTextValorReceita.requestFocus();
            return;
        }

        if (valor == 0) {
            editTextValorReceita.setError("Insira um valor maior que 0");
            editTextValorReceita.requestFocus();
            return;
        }

        //Se o botão "definirData" não foi clicado
        if (!isClicked){
            setDefaultDateDb();
        }

        //Verificar se o spinner está vaio
        if (spinnerCategoria.getCount() == 0){
            Toast.makeText(this, "Por favor, adicione uma categoria!",Toast.LENGTH_LONG).show();
            return;
        }

        double valorReceita = Double.parseDouble(editTextValorReceita.getText().toString());
        String designacaoReceita = editTextDesignacaoReceita.getText().toString();
        String tipoReceita = spinnerCategoria.getSelectedItem().toString().trim();
        int idTipoReceita = getIdCategoriaReceita(tipoReceita);

        registoMovimentos.setId_movimento(getNowDate());
        registoMovimentos.setReceitadespesa(RECEITA);
        registoMovimentos.setDesignacao(designacaoReceita);
        registoMovimentos.setValor(valorReceita);
        registoMovimentos.setTiporeceita(idTipoReceita);

        //Teste (last test: 9jun18: Success!)
  //      TextView textViewTestDate = (TextView) findViewById(R.id.textViewTestDate);
//        textViewTestDate.setText(""+registoMovimentos.getId_movimento()+"-"+registoMovimentos.getDia()+"-"+registoMovimentos.getMes()+"-"+registoMovimentos.getAno()+"-"+registoMovimentos.getReceitadespesa()+"-"+registoMovimentos.getDesignacao()+"-"+registoMovimentos.getValor()+"-"+registoMovimentos.getTiporeceita());

        isClicked = false;

        try {
            if (!checkDataToInsert(registoMovimentos.getDia(),registoMovimentos.getMes(),registoMovimentos.getAno())){
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //inserir registo na BD
        try {
            insertRegistoReceitaDb(registoMovimentos.getId_movimento(),
                                    registoMovimentos.getDia(),
                                    registoMovimentos.getMes(),
                                    registoMovimentos.getAno(),
                                    registoMovimentos.getReceitadespesa(),
                                    registoMovimentos.getDesignacao(),
                                    registoMovimentos.getValor(),
                                    registoMovimentos.getTiporeceita()
            );
            Snackbar snackbar = Snackbar.make(view,"Registo inserido com sucesso!",Snackbar.LENGTH_LONG).setAction("Anular", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        deleteRegisto(registoMovimentos.getId_movimento());
                        Toast.makeText(getApplicationContext(),"Registo eliminado com sucesso!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Erro ao eliminar registo...", Toast.LENGTH_LONG).show();
                    }
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } catch (Exception e) {
            Snackbar.make(view,"Erro ao inserir registo na BD!",Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        //Limpa os campos preenchidos
        editTextDesignacaoReceita.setText("");
        editTextValorReceita.setText("");
        textViewSelectedDateReceita.setText("");
    }

    /*****************************Functions and Methods****************************************/

    private void insertCategoriaReceitaDb(String categoria) {
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableTipoReceita tableTipoReceita = new DbTableTipoReceita(db);

        TipoReceita tipoReceita = new TipoReceita();
        tipoReceita.setCategoria(categoria);

        tableTipoReceita.insert(DbTableTipoReceita.getContentValues(tipoReceita));
        db.close();
    }


    public static String getNowDate(){ //Data e Hora atuais
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

    public void setDefaultDateDb(){ //Data atual
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH) + 1;
        int dia = c.get(Calendar.DAY_OF_MONTH);

        registoMovimentos.setAno(ano);
        registoMovimentos.setMes(mes);
        registoMovimentos.setDia(dia);
    }

    private int getCurrentDay(){ //Data atual
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        return dia;
    }

    public int getCurrentMonth(){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        return month;
    }

    private int getCurrentYear(){ //Data atual
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        return ano;
    }

    private void insertRegistoReceitaDb(String id_movimento, int dia, int mes, int ano, String receitadespesa, String designacao, double valor, int tiporeceita){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);

        RegistoMovimentos registoMovimentos = new RegistoMovimentos();

        registoMovimentos.setId_movimento(id_movimento);
        registoMovimentos.setDia(dia);
        registoMovimentos.setMes(mes);
        registoMovimentos.setAno(ano);
        registoMovimentos.setReceitadespesa(receitadespesa);
        registoMovimentos.setDesignacao(designacao);
        registoMovimentos.setValor(valor);
        registoMovimentos.setTiporeceita(tiporeceita);

        tableRegistoMovimentos.insert(DbTableRegistoMovimentos.getContentValues(registoMovimentos));
        db.close();
    }

    public ArrayList<String> getCategoriasReceitaFromDb(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);

        Cursor cursor = dbTableTipoReceita.query(DbTableTipoReceita.CATEGORIA_COLUMN, null, null, null, null, DbTableTipoReceita._ID);

        ArrayList<String> list = DbTableTipoReceita.getCategoriasReceitaFromDb(cursor);

        cursor.close();
        db.close();
        return list;
    }

    private void loadSpinnerData(){
        Spinner spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoriasReceitaFromDb());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    public int checkCategoriaReceita(String categoria){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        String query = "SELECT "+DbTableTipoReceita._ID+" FROM "+DbTableTipoReceita.TABLE_NAME+" WHERE "+DbTableTipoReceita.CATEGORIA_RECEITA+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{categoria});

        int id = -1;

        if (cursor.getCount() > 0){ //Se devolver pelo menos uma linha é porque a categoria já existe.
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(DbTableTipoReceita._ID));
        }

        cursor.close();
        db.close();
        return id;
    }

    private void deleteRegisto(String id){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);
        tableRegistoMovimentos.delete(DbTableRegistoMovimentos._ID+"=?",new String[]{id});

        db.close();
    }

    private int getIdCategoriaReceita(String categoria){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);

        Cursor cursor = dbTableTipoReceita.query(DbTableTipoReceita.ID_COLUMN,DbTableTipoReceita.CATEGORIA_RECEITA+"=?",new String[]{categoria},null,null,null);

        int id = DbTableTipoReceita.getIdCategoriaReceita(cursor);

        cursor.close();
        db.close();
        return id;
    }

    private void setDefaultDateToTextView(){
        TextView textViewDate = (TextView) findViewById(R.id.textViewSelectedDate);

        int dia = getCurrentDay();
        int mes = getCurrentMonth();
        int ano = getCurrentYear();

        textViewDate.setText(""+dia+"/"+mes+"/"+ano);
    }

    public Boolean checkDataToInsert(int dia, int mes, int ano) throws ParseException { //comparar data selecionada com data atual
        boolean insert = true;
        int diaAtual = getCurrentDay();
        int mesAtual = getCurrentMonth();
        int anoAtual = getCurrentYear();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Date atualDate = format.parse(diaAtual+"-"+mesAtual+"-"+anoAtual);
        Date selectDate = format.parse(dia+"-"+mes+"-"+ano);

        if (atualDate.compareTo(selectDate) <= 0) {
            Toast.makeText(getApplicationContext(), "Não pode inserir registos para datas futuras!", Toast.LENGTH_LONG).show();
            insert = false;
        }

        return insert;
    }
}
