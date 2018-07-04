package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NovaDespesa extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogFragmentCategoria.ExampleDialogListener {


    /***************************Variáveis*************************/

    public static final String DESPESA = "Despesa";
    private static Boolean isClicked = false;
    private RegistoMovimentos registoMovimentos;
    private EditText editTextDesignacaoDespesa;
    private EditText editTextValorDespesa;
    private Spinner spinnerCategoriaDespesa;
    private TextView textViewSelectedDateDespesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_despesa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /************************Construção dos objetos***************************/

        registoMovimentos = new RegistoMovimentos();
        editTextDesignacaoDespesa = (EditText) findViewById(R.id.editTextDesignacaoDespesa);
        editTextValorDespesa = (EditText) findViewById(R.id.editTextValorDespesa);
        spinnerCategoriaDespesa = (Spinner) findViewById(R.id.spinnerCategoriaDespesa);
        textViewSelectedDateDespesa = (TextView) findViewById(R.id.textViewSelectedDateDespesa);

        loadSpinnerData(); //atualizar spinner

        setDefaultDateToTextView(); //data atual na textview data
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /************************Buttons actions**************************************************/

    public void addCategoriaDespesa(View view) { //Botão adicionar categoria Despesa
        DialogFragmentCategoria dialogFragmentCategoria = new DialogFragmentCategoria();
        dialogFragmentCategoria.show(getSupportFragmentManager(), "DialogFragmentDespesas");
    }


    public void cancel(View view) { //Botão "cancelar"
        finish();
    }

    @Override
    public void setTexts(String categoria) { //Ação do botão "addCategoriaDespesa"

        try {
            if (checkCategoriaDespesa(categoria) != -1){ //Se devolver um id != -1 é porque já exite uma categoria com o nome que vamos inserir
                Toast.makeText(NovaDespesa.this,R.string.cate_ja_exist,Toast.LENGTH_LONG).show();
                return;
            }
            insertCategoriaDespesaDb(categoria);
            Toast.makeText(NovaDespesa.this,R.string.sms_cat_inserida_success,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(NovaDespesa.this,R.string.sms_error_inserir_cat_db,Toast.LENGTH_LONG).show();
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

        textViewSelectedDateDespesa.setText(""+dayOfMonth+"/"+month+"/"+year);

        isClicked = true;
    }

    public void inserirDespesaDb(View view) { //Botão inserir despesa

        //Verificar se o campo valor foi preenchido
        double valor = 0;
        try {
            valor = Double.parseDouble(editTextValorDespesa.getText().toString());
        } catch (NumberFormatException e) {
            editTextValorDespesa.setError(getString(R.string.insira_um_valor));
            editTextValorDespesa.requestFocus();
            return;
        }

        if (valor == 0) {
            editTextValorDespesa.setError(getString(R.string.insira_um_valor_maior_que_0));
            editTextValorDespesa.requestFocus();
            return;
        }

        //Se o botão "definirData" não foi clicado
        if (!isClicked){
            setDefaultDateDb();
        }

        //Verificar se o spinner está vazio
        if (spinnerCategoriaDespesa.getCount() == 0){
            Toast.makeText(this,R.string.adicione_uma_categoria,Toast.LENGTH_LONG).show();
            return;
        }

        double valorDespesa = Double.parseDouble(editTextValorDespesa.getText().toString());
        String designacaoDespesa = editTextDesignacaoDespesa.getText().toString();
        String tipoDespesa = spinnerCategoriaDespesa.getSelectedItem().toString().trim();
        int idTipoDespesa = getIdCategoriaDespesa(tipoDespesa);

        registoMovimentos.setId_movimento(getNowDate());
        registoMovimentos.setReceitadespesa(DESPESA);
        registoMovimentos.setDesignacao(designacaoDespesa);
        registoMovimentos.setValor(valorDespesa);
        registoMovimentos.setTipodespesa(idTipoDespesa);

        //Debug
        //TextView textViewDataReadyInsertDb = (TextView) findViewById(R.id.textViewDataReadyInsertDb);
        //textViewDataReadyInsertDb.setText(""+registoMovimentos.getId_movimento()+"-"+registoMovimentos.getDia()+"-"+registoMovimentos.getMes()+"-"+registoMovimentos.getAno()+"-"+registoMovimentos.getReceitadespesa()+"-"+registoMovimentos.getDesignacao()+"-"+registoMovimentos.getValor()+"-"+registoMovimentos.getTipodespesa());

        isClicked = false;

        try { //verifica se data seleciona <= data atual
            if (!checkDataBeforeInsert(registoMovimentos.getDia(),registoMovimentos.getMes(),registoMovimentos.getAno())){
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.registo_inserido_success,Snackbar.LENGTH_LONG).setAction(R.string.anular_registo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        deleteRegisto(registoMovimentos.getId_movimento());
                        Toast.makeText(getApplicationContext(), R.string.registo_eliminado_success, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.erro_eliminar_registo, Toast.LENGTH_LONG).show();
                    }
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } catch (Exception e) {
            Snackbar.make(view, R.string.erro_inserir_registo_bd,Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        //Limpa os campos preenchidos
        editTextDesignacaoDespesa.setText("");
        editTextValorDespesa.setText("");
        textViewSelectedDateDespesa.setText("");
        setDefaultDateToTextView();

    }



    /*****************************Functions and Methods****************************************/

    /**
     *
     * @param id_movimento
     * @param dia
     * @param mes
     * @param ano
     * @param receitadespesa
     * @param designacao
     * @param valor
     * @param tipodespesa
     *
     * inserir registo despesa BD
     */
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

    /**
     * @param categoria
     *
     * inserir categoria despesa
     */
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

    /**
     * definir data atual por defeito
     */
    private void setDefaultDateDb(){
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH) + 1;
        int dia = c.get(Calendar.DAY_OF_MONTH);

        registoMovimentos.setAno(ano);
        registoMovimentos.setMes(mes);
        registoMovimentos.setDia(dia);
    }

    /**
     * @return dia atual
     */
    private int getCurrentDay(){
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        return dia;
    }

    /**
     * @return mes atual
     */
    private int getCurrentMonth(){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        return month;
    }

    /**
     * @return ano atual
     */
    private int getCurrentYear(){
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        return ano;
    }

    /**
     * @return data completa atual concatenada: ddMMyyHHmmss
     */
    private String getNowDate(){
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

    /**
     * @return categoria despesa
     */
    private ArrayList<String> getCategoriasDespesaFromDb(){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);

        Cursor cursor = dbTableTipoDespesa.query(DbTableTipoDespesa.CATEGORIA_COLUMN, null, null, null, null, DbTableTipoDespesa._ID);

        ArrayList<String> list = DbTableTipoDespesa.getCategoriasDespesaFromDb(cursor);

        cursor.close();
        db.close();
        return list;
    }

    /**
     * carregar todas categorias despesa no spinner
     */
    private void loadSpinnerData(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoriasDespesaFromDb());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaDespesa.setAdapter(adapter);
    }

    /**
     * @param categoria
     * @return id da categoria
     */
    public int checkCategoriaDespesa(String categoria){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        String query = "SELECT "+DbTableTipoDespesa._ID+" FROM "+DbTableTipoDespesa.TABLE_NAME+" WHERE "+DbTableTipoDespesa.CATEGORIA_DESPESAS+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{categoria});

        int id = -1;

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(DbTableTipoDespesa._ID));
        }

        cursor.close();
        db.close();
        return id;
    }

    /**
     * @param id
     *
     * elimina registo com o id
     */
    private void deleteRegisto(String id){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);
        tableRegistoMovimentos.delete(DbTableRegistoMovimentos._ID+"=?",new String[]{id});

        db.close();
    }

    /**
     * @param categoria
     * @return id categoria despesa
     */
    private int getIdCategoriaDespesa(String categoria){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);

        Cursor cursor = dbTableTipoDespesa.query(DbTableTipoDespesa.ID_COLUMN,DbTableTipoDespesa.CATEGORIA_DESPESAS+"=?",new String[]{categoria},null,null,null);

        int id = DbTableTipoDespesa.getIdCategoriaDespesa(cursor);

        cursor.close();
        db.close();
        return id;
    }

    /**
     * coloca na textview da data a data atual dd/mm/yyyy
     */
    private void setDefaultDateToTextView(){
        TextView textViewDate = (TextView) findViewById(R.id.textViewSelectedDateDespesa);

        int dia = getCurrentDay();
        int mes = getCurrentMonth();
        int ano = getCurrentYear();

        textViewDate.setText(""+dia+"/"+mes+"/"+ano);
    }

    /**
     * @param dia
     * @param mes
     * @param ano
     * @return false se a data selecionada > data atual
     * @throws ParseException
     */
    private Boolean checkDataBeforeInsert(int dia, int mes, int ano) throws ParseException {
        boolean insert = true;
        int diaAtual = getCurrentDay();
        int mesAtual = getCurrentMonth();
        int anoAtual = getCurrentYear();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Date atualDate = format.parse(diaAtual+"-"+mesAtual+"-"+anoAtual);
        Date selectDate = format.parse(dia+"-"+mes+"-"+ano);

        if (atualDate.compareTo(selectDate) < 0) {
            Toast.makeText(getApplicationContext(),R.string.sms_alert_inserir_registos_data_futura, Toast.LENGTH_LONG).show();
            insert = false;
        }

        return insert;
    }

}
