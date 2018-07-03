package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditDespesa extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogFragmentCategoria.ExampleDialogListener{

    /****************Global Variables***************/

    public static final String DESPESA = "Despesa";
    private DbContabOpenHelper dbContabOpenHelper;
    private RegistoMovimentos registoMovimentos;
    private TextView textViewSelectDateDespesa;
    private Spinner spinnerCategoriaDespesa;
    private EditText editTextDesignacaoDespesa;
    private EditText editTextValorDespesa;
    private String id_registo;
    private TextView textViewSelectedDateDespesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_despesa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //***************************Construção dos objetos***********************************

        textViewSelectDateDespesa = (TextView) findViewById(R.id.textViewSelectedDateDespesa);
        spinnerCategoriaDespesa = (Spinner) findViewById(R.id.spinnerCategoriaDespesa);
        editTextDesignacaoDespesa = (EditText) findViewById(R.id.editTextDesignacaoDespesa);
        editTextValorDespesa = (EditText) findViewById(R.id.editTextValorDespesa);
        registoMovimentos = new RegistoMovimentos();
        dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        textViewSelectedDateDespesa = (TextView) findViewById(R.id.textViewSelectedDateDespesa);

        loadSpinnerData();

        try { //getIntent da atividade ListarTodos
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            id_registo = extras.getString("ID");
        } catch (Exception e) {
            Toast.makeText(EditDespesa.this, R.string.sms_nao_foi_possivel_obter_id, Toast.LENGTH_LONG).show();
        }

        //Buscar dados do registo para colocar nos campos
        registoMovimentos = dbContabOpenHelper.getRegistoFromRecycler(id_registo);
        textViewSelectDateDespesa.setText(""+registoMovimentos.getDia()+"/"+registoMovimentos.getMes()+"/"+registoMovimentos.getAno());
        String categoriaDespesa = dbContabOpenHelper.getTipoDespesaByID(registoMovimentos.getTipodespesa());
        setSpinnerToValue (spinnerCategoriaDespesa, categoriaDespesa);
        editTextDesignacaoDespesa.setText(""+registoMovimentos.getDesignacao());
        editTextValorDespesa.setText(""+registoMovimentos.getValor());

    }

    /************************Buttons actions**************************************************/

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    public void addCategoriaEditDespesa(View view) { //Botão adicionar categoria Despesa
        DialogFragmentCategoria dialogFragmentCategoria = new DialogFragmentCategoria();
        dialogFragmentCategoria.show(getSupportFragmentManager(), "DialogFragmentDespesas");
    }

    public void cancel(View view) { //Botão "cancelar"
        finish();
    }

    public void definirDataEditDespesa(View view) { //Botão definir data
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
    }

    @Override
    public void setTexts(String categoria) { //Ação do botão "addCategoriaDespesa"

        try {
            if (checkCategoriaDespesa(categoria) != -1){ //Se devolver um id != -1 é porque já exite uma categoria com o nome que vai ser inserido
                Toast.makeText(EditDespesa.this, R.string.cate_ja_exist,Toast.LENGTH_LONG).show();
                return;
            }
            insertCategoriaDespesaDb(categoria);
            Toast.makeText(EditDespesa.this, R.string.sms_cat_inserida_success,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(EditDespesa.this, R.string.sms_error_inserir_cat_db,Toast.LENGTH_LONG).show();
        }

        loadSpinnerData(); //Atualizar spinner
    }

    public void updateDespesaDb(View view) { //Botão atualizar despesa

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

        //Verificar se o spinner está vazio
        if (spinnerCategoriaDespesa.getCount() == 0){
            Toast.makeText(this, R.string.adicione_uma_categoria,Toast.LENGTH_LONG).show();
            return;
        }

        double valorDespesa = Double.parseDouble(editTextValorDespesa.getText().toString());
        String designacaoDespesa = editTextDesignacaoDespesa.getText().toString();
        String tipoDespesa = spinnerCategoriaDespesa.getSelectedItem().toString().trim();
        int idTipoDespesa = getIdCategoriaDespesa(tipoDespesa);

        registoMovimentos.setId_movimento(id_registo);
        registoMovimentos.setReceitadespesa(DESPESA);
        registoMovimentos.setDesignacao(designacaoDespesa);
        registoMovimentos.setValor(valorDespesa);
        registoMovimentos.setTipodespesa(idTipoDespesa);

        //Debug
        //TextView textViewDataReadyInsertDb = (TextView) findViewById(R.id.textViewDataReadyInsertDb);
        //textViewDataReadyInsertDb.setText(""+registoMovimentos.getId_movimento()+"-"+registoMovimentos.getDia()+"-"+registoMovimentos.getMes()+"-"+registoMovimentos.getAno()+"-"+registoMovimentos.getReceitadespesa()+"-"+registoMovimentos.getDesignacao()+"-"+registoMovimentos.getValor()+"-"+registoMovimentos.getTipodespesa());

        try { //verifica se data seleciona <= data atual
            if (!checkDataBeforeInsert(registoMovimentos.getDia(),registoMovimentos.getMes(),registoMovimentos.getAno())){
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //insere registo na BD
        try {
            updateRegistoDespesaDb(registoMovimentos.getId_movimento(),
                    registoMovimentos.getDia(),
                    registoMovimentos.getMes(),
                    registoMovimentos.getAno(),
                    registoMovimentos.getReceitadespesa(),
                    registoMovimentos.getDesignacao(),
                    registoMovimentos.getValor(),
                    registoMovimentos.getTipodespesa()
            );
            Toast.makeText(getApplicationContext(), R.string.registo_alterado_com_sucesso, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_alterar_registo, Toast.LENGTH_LONG).show();
        }

        //Limpa os campos preenchidos
        editTextDesignacaoDespesa.setText("");
        editTextValorDespesa.setText("");
        textViewSelectedDateDespesa.setText("");

        goBack();
    }


    /*************************************Functions and Methods*********************************************/

    private void goBack(){
        finish();
    }

    /**
     * @param id_movimento
     * @param dia
     * @param mes
     * @param ano
     * @param receitadespesa
     * @param designacao
     * @param valor
     * @param tipodespesa
     *
     * atualizar registo despesa na BD
     */
    private void updateRegistoDespesaDb(String id_movimento, int dia, int mes, int ano, String receitadespesa, String designacao, double valor, int tipodespesa){
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

        tableRegistoMovimentos.update(DbTableRegistoMovimentos.getContentValues(registoMovimentos),DbTableRegistoMovimentos._ID+"=?",new String[]{id_movimento});
        db.close();
    }


    /**
     * @param spinner
     * @param value
     *
     * colocar no spinner um valor definido
     */
    private void setSpinnerToValue (Spinner spinner, String value){
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
     * carrega todas as categorias despesa no spinner
     */
    private void loadSpinnerData(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoriasDespesaFromDb());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaDespesa.setAdapter(adapter);
    }

    /**
     * @return lista com todas as categorias despesa
     */
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

    /**
     * @return mes atual
     */
    public int getCurrentMonth(){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        return month;
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
     * @return ano atual
     */
    private int getCurrentYear(){
        Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        return ano;
    }


    /**
     * @param categoria
     * @return id = -1 se não existir a categoria
     */
    public int checkCategoriaDespesa(String categoria){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
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
     * @param categoria
     *
     * inserir uma categoria despesa na BD
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
     * @param categoria
     * @return id da categoria
     */
    private int getIdCategoriaDespesa(String categoria){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoDespesa dbTableTipoDespesa = new DbTableTipoDespesa(db);

        Cursor cursor = dbTableTipoDespesa.query(DbTableTipoDespesa.ID_COLUMN,DbTableTipoDespesa.CATEGORIA_DESPESAS+"=?",new String[]{categoria},null,null,null);

        int id = DbTableTipoDespesa.getIdCategoriaDespesa(cursor);

        cursor.close();
        db.close();
        return id;
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
            Toast.makeText(getApplicationContext(), R.string.sms_alert_inserir_registos_data_futura, Toast.LENGTH_LONG).show();
            insert = false;
        }

        return insert;
    }
}
