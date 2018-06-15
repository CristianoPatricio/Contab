package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NovaDespesa extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogFragmentCategoria.ExampleDialogListener {


    /*************Global Variables********************************************/
    public static final String DESPESA = "Despesa";
    private static Boolean isClicked = false;
    RegistoMovimentos registoMovimentos = new RegistoMovimentos();
    private static Boolean valorLimite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_despesa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadSpinnerData(); //atualizar spinner
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

    /************************Buttons actions**************************************************/

    public void addCategoriaDespesa(View view) { //Botão adicionar categoria Despesa
        DialogFragmentCategoria dialogFragmentCategoria = new DialogFragmentCategoria();
        dialogFragmentCategoria.show(getSupportFragmentManager(), "DialogFragmentDespesas");
    }

    @Override
    public void setTexts(String categoria) { //Ação do botão "addCategoriaDespesa"

        try {
            if (checkCategoriaDespesa(categoria) != -1){ //Se devolver um id != -1 é porque já exite uma categoria com o nome que vamos inserir
                Toast.makeText(NovaDespesa.this, "Categoria já existente!",Toast.LENGTH_LONG).show();
                return;
            }
            insertCategoriaDespesaDb(categoria);
            Toast.makeText(NovaDespesa.this, "Categoria inserida com sucesso!",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(NovaDespesa.this, "Erro ao inserir a categoria na BD!",Toast.LENGTH_LONG).show();
        }

        loadSpinnerData(); //Atualizar spinner

    }

    public void definirDataDespesa(View view) { //Botão "definir data"
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

    public void inserirDespesaDb(View view) { //Botão inserir despesa
        //Declaração de objetos
        final EditText editTextDesignacaoDespesa = (EditText) findViewById(R.id.editTextDesignacaoDespesa);
        EditText editTextValorDespesa = (EditText) findViewById(R.id.editTextValorDespesa);
        Spinner spinnerCategoriaDespesa = (Spinner) findViewById(R.id.spinnerCategoriaDespesa);
        TextView textViewSelectedDateDespesa = (TextView) findViewById(R.id.textViewSelectedDateDespesa);

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

        //Verificar se o spinner está vaio
        if (spinnerCategoriaDespesa.getCount() == 0){
            Toast.makeText(this, "Por favor, adicione uma categoria!",Toast.LENGTH_LONG).show();
            return;
        }

        double valorDespesa = Double.parseDouble(editTextValorDespesa.getText().toString());
        String designacaoDespesa = editTextDesignacaoDespesa.getText().toString();
        int tipoDespesa = spinnerCategoriaDespesa.getSelectedItemPosition()+1;

        registoMovimentos.setId_movimento(getNowDate());
        registoMovimentos.setReceitadespesa(DESPESA);
        registoMovimentos.setDesignacao(designacaoDespesa);
        registoMovimentos.setValor(valorDespesa);
        registoMovimentos.setTipodespesa(tipoDespesa);

        //Verifica valor da despesa com o valor de orçamento definido
        if (!valorLimite) checkOrcamento(valorDespesa);

        //Teste
//        TextView textViewDataReadyInsertDb = (TextView) findViewById(R.id.textViewDataReadyInsertDb);
//        textViewDataReadyInsertDb.setText(""+registoMovimentos.getId_movimento()+"-"+registoMovimentos.getDia()+"-"+registoMovimentos.getMes()+"-"+registoMovimentos.getAno()+"-"+registoMovimentos.getReceitadespesa()+"-"+registoMovimentos.getDesignacao()+"-"+registoMovimentos.getValor()+"-"+registoMovimentos.getTipodespesa());

        isClicked = false;

        //insere registo na BD
        try {
            insertRegistoDespesaDb(registoMovimentos.getId_movimento(),
                                    registoMovimentos.getDia(),
                                    registoMovimentos.getMes(),
                                    registoMovimentos.getAno(),
                                    registoMovimentos.getReceitadespesa(),
                                    registoMovimentos.getDesignacao(),
                                    registoMovimentos.getValor(),
                                    registoMovimentos.getTipodespesa()
            );
            Snackbar.make(view,"Registo inserido com sucesso!",Snackbar.LENGTH_LONG).setAction("Cancelar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        deleteRegisto(registoMovimentos.getId_movimento());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Erro ao eliminar registo...", Toast.LENGTH_LONG).show();
                    }
                }
            }).show();
        } catch (Exception e) {
            Snackbar.make(view,"Erro ao inserir registo na BD!",Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        //Limpa os campos preenchidos
        editTextDesignacaoDespesa.setText("");
        editTextValorDespesa.setText("");
        textViewSelectedDateDespesa.setText("");

    }

    /*****************************Functions and Methods****************************************/

    private void insertRegistoDespesaDb(String id_movimento, int dia, int mes, int ano, String receitadespesa, String designacao, double valor, int tipodespesa){
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
        registoMovimentos.setTipodespesa(tipodespesa);

        tableRegistoMovimentos.insert(DbTableRegistoMovimentos.getContentValues(registoMovimentos));
        db.close();
    }

    private void insertCategoriaDespesaDb(String categoria) {
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableTipoDespesa tableTipoDespesa = new DbTableTipoDespesa(db);

        TipoDespesa tipoDespesa = new TipoDespesa();
        tipoDespesa.setCategoria(categoria);

        tableTipoDespesa.insert(DbTableTipoDespesa.getContentValues(tipoDespesa));
        db.close();
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

    public ArrayList<String> getCategoriasDespesaFromDb(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);

        Cursor cursor = dbTableTipoDespesa.query(DbTableTipoDespesa.CATEGORIA_COLUMN, null, null, null, null, DbTableTipoDespesa._ID);

        ArrayList<String> list = DbTableTipoDespesa.getCategoriasDespesaFromDb(cursor);

        cursor.close();
        db.close();
        return list;
    }

    private void loadSpinnerData(){
        Spinner spinnerCategoriaDespesa = (Spinner) findViewById(R.id.spinnerCategoriaDespesa);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoriasDespesaFromDb());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaDespesa.setAdapter(adapter);
    }

    public int checkCategoriaDespesa(String categoria){//retorna id da categoria
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        String query = "SELECT "+DbTableTipoDespesa._ID+" FROM "+DbTableTipoDespesa.TABLE_NAME+" WHERE "+DbTableTipoDespesa.CATEGORIA_DESPESAS+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{categoria});

        int id = -1;

        if (cursor.getCount() > 0){ //Se devolver pelo menos uma linha é porque a categoria já existe.
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(DbTableTipoDespesa._ID));
        }

        cursor.close();
        db.close();
        return id;
    }

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

    public void checkOrcamento(double valor){ //verifica se a despesa ultrapassa os limites de orçamento definidos
        double valorAlerta = getValorOrcamentoFromDb() - valor;
        new DecimalFormat("0.00").format(valorAlerta);
        if(getValorOrcamentoFromDb() < valor){
            Toast.makeText(this,"A despesa que inseriu ultrapassou o seu limite de orçamento mensal!",Toast.LENGTH_LONG).show();
            valorLimite = true;
        }else if (valorAlerta < 50 && valorAlerta != 0){
            Toast.makeText(this, "Está a "+valorAlerta+" € de atingir o seu limite de orçamento mensal!",Toast.LENGTH_LONG).show();
        }else if (valorAlerta == 0){
            Toast.makeText(this,"Atingiu o limite de orçamento mensal!",Toast.LENGTH_LONG).show();
            valorLimite = true;
        }
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

}
