package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditReceita extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogFragmentCategoria.ExampleDialogListener {//}, android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    /*************************Global Variables**************************/

    public static final String RECEITA = "Receita";
    private DbContabOpenHelper dbContabOpenHelper;
    private RegistoMovimentos registoMovimentos;
    private TextView textViewSelectDate;
    private Spinner spinnerCategoria;
    private EditText editTextDesignacaoReceita;
    private EditText editTextValorReceita;
    private String id_registo;

    //Content Provider********NÃO USADO
    private static final int CATEGORIES_CURSOR_LOADER_ID = 0;
    private RegistoMovimentosAdapter registoMovimentosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receita);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //******************************Construção dos objetos************************************

        textViewSelectDate = (TextView) findViewById(R.id.textViewSelectedDate);
        spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        editTextDesignacaoReceita = (EditText) findViewById(R.id.editTextDesignacaoReceita);
        editTextValorReceita = (EditText) findViewById(R.id.editTextValorReceita);
        textViewSelectDate = (TextView) findViewById(R.id.textViewSelectedDate);
        registoMovimentos = new RegistoMovimentos();
        dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());

        loadSpinnerData();

        try { //getIntent da atividade ListarTodos
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            id_registo = extras.getString("ID");
        } catch (Exception e) {
            Toast.makeText(EditReceita.this, R.string.sms_nao_foi_possivel_obter_id, Toast.LENGTH_LONG).show();
        }

        //Buscar dados do registo para colocar nos campos
        registoMovimentos = dbContabOpenHelper.getRegistoFromRecycler(id_registo);
        textViewSelectDate.setText(""+registoMovimentos.getDia()+"/"+registoMovimentos.getMes()+"/"+registoMovimentos.getAno());
        String categoriaReceita = dbContabOpenHelper.getTipoReceitaByID(registoMovimentos.getTiporeceita());
        setSpinnerToValue (spinnerCategoria, categoriaReceita);
        editTextDesignacaoReceita.setText(""+registoMovimentos.getDesignacao());
        editTextValorReceita.setText(""+registoMovimentos.getValor());

//        getSupportLoaderManager().initLoader(CATEGORIES_CURSOR_LOADER_ID,null,this);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getSupportLoaderManager().restartLoader(CATEGORIES_CURSOR_LOADER_ID, null, this);
//    }

    /*****************************Buttons actions*****************************************************/
    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    public void definirDataEditReceita(View view) { //botão definir data
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public void cancel(View view) { //Botão "Cancelar"
        finish();
    }

    public void atualizarReceita(View view) { //botão atualizar receita DB

        //Verificar se o campo valor foi preenchido
        double valor = 0;
        try {
            valor = Double.parseDouble(editTextValorReceita.getText().toString());
        } catch (NumberFormatException e) {
            editTextValorReceita.setError(getString(R.string.insira_um_valor));
            editTextValorReceita.requestFocus();
            return;
        }

        if (valor == 0) {
            editTextValorReceita.setError(getString(R.string.insira_um_valor_maior_que_0));
            editTextValorReceita.requestFocus();
            return;
        }

        //Verificar se o spinner está vazio
        if (spinnerCategoria.getCount() == 0){
            Toast.makeText(this, R.string.adicione_uma_categoria,Toast.LENGTH_LONG).show();
            return;
        }

        double valorReceita = Double.parseDouble(editTextValorReceita.getText().toString());
        String designacaoReceita = editTextDesignacaoReceita.getText().toString();
        String tipoReceita = spinnerCategoria.getSelectedItem().toString().trim();
        int idTipoReceita = getIdCategoriaReceita(tipoReceita);

        registoMovimentos.setId_movimento(id_registo);
        registoMovimentos.setReceitadespesa(RECEITA);
        registoMovimentos.setDesignacao(designacaoReceita);
        registoMovimentos.setValor(valorReceita);
        registoMovimentos.setTiporeceita(idTipoReceita);

        //Debug
        //TextView textViewTestDate = (TextView) findViewById(R.id.textViewTestDate);
        //textViewTestDate.setText(""+registoMovimentos.getId_movimento()+"-"+registoMovimentos.getDia()+"-"+registoMovimentos.getMes()+"-"+registoMovimentos.getAno()+"-"+registoMovimentos.getReceitadespesa()+"-"+registoMovimentos.getDesignacao()+"-"+registoMovimentos.getValor()+"-"+registoMovimentos.getTiporeceita());

        try { //verifica se data seleciona <= data atual
            if (!checkDataBeforeInsert(registoMovimentos.getDia(),registoMovimentos.getMes(),registoMovimentos.getAno())){
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //atualiza registo na DB
        try {
            updateRegistoReceitaDb(registoMovimentos.getId_movimento(),
                    registoMovimentos.getDia(),
                    registoMovimentos.getMes(),
                    registoMovimentos.getAno(),
                    registoMovimentos.getReceitadespesa(),
                    registoMovimentos.getDesignacao(),
                    registoMovimentos.getValor(),
                    registoMovimentos.getTiporeceita()
            );
            Toast.makeText(getApplicationContext(),R.string.registo_alterado_com_sucesso, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),R.string.erro_alterar_registo, Toast.LENGTH_LONG).show();
        }

        //Limpa os campos preenchidos
        editTextDesignacaoReceita.setText("");
        editTextValorReceita.setText("");
        textViewSelectDate.setText("");

        goBack();
    }

    public void addCategoriaEditReceita(View view) { //botão adicionar categoria
        DialogFragmentCategoria dialogFragmentCategoria = new DialogFragmentCategoria();
        dialogFragmentCategoria.show(getSupportFragmentManager(), "DialogFragmentCategoria");
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

        textViewSelectDate.setText(""+dayOfMonth+"/"+month+"/"+year);
    }

    @Override
    public void setTexts(String categoria) {  //Ação do botão "addCategoriaReceita"
        try {
            if(checkCategoriaReceita(categoria) != -1) { //Se devolver um id != -1 é porque já exite uma categoria com o nome que vamos inserir
                Toast.makeText(EditReceita.this, R.string.cate_ja_exist,Toast.LENGTH_LONG).show();
                return;
            }
            insertCategoriaReceitaDb(categoria);
            Toast.makeText(EditReceita.this, R.string.sms_cat_inserida_success,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(EditReceita.this, R.string.sms_error_inserir_cat_db,Toast.LENGTH_LONG).show();
        }

        loadSpinnerData(); //atualizar spinner
    }

    /*******************************Functions and Methods********************************************/

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
     * @param tiporeceita
     *
     * atualizar registo receita na BD
     */
    private void updateRegistoReceitaDb(String id_movimento, int dia, int mes, int ano, String receitadespesa, String designacao, double valor, int tiporeceita){
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

        tableRegistoMovimentos.update(DbTableRegistoMovimentos.getContentValues(registoMovimentos),DbTableRegistoMovimentos._ID+"=?",new String[]{id_movimento});
        db.close();
    }

    /**
     * @param spinner
     * @param value
     *
     * coloca no spinner um valor definido
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
     * @param categoria
     *
     * inserir categorias Receitas
     */
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

    /**
     * carrega todas as categorias receita no spinner
     */
    private void loadSpinnerData(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoriasReceitaFromDb());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    /**
     * @return lista com todas as categorias receita
     */
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

    /**
     * @param categoria
     * @return id = -1 se não existir a categoria
     */
    public int checkCategoriaReceita(String categoria){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        String query = "SELECT "+DbTableTipoReceita._ID+" FROM "+DbTableTipoReceita.TABLE_NAME+" WHERE "+DbTableTipoReceita.CATEGORIA_RECEITA+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{categoria});

        int id = -1;

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(DbTableTipoReceita._ID));
        }

        cursor.close();
        db.close();
        return id;
    }

    /**
     * @param categoria
     * @return id da categoria
     */
    private int getIdCategoriaReceita(String categoria){
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableTipoReceita dbTableTipoReceita = new DbTableTipoReceita(db);

        Cursor cursor = dbTableTipoReceita.query(DbTableTipoReceita.ID_COLUMN,DbTableTipoReceita.CATEGORIA_RECEITA+"=?",new String[]{categoria},null,null,null);

        int id = DbTableTipoReceita.getIdCategoriaReceita(cursor);

        cursor.close();
        db.close();
        return id;
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
    public int getCurrentMonth(){
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

//  ***********************************************************************************************************************************
//  ContentProvider
//
//    @NonNull
//    @Override
//    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        if (id == CATEGORIES_CURSOR_LOADER_ID){
//            return new CursorLoader(this, ContabContentProvider.CATEGORIAS_RECEITAS_URI, DbTableTipoReceita.ALL_COLUMNS, null, null, null);
//        }
//        return null;
//    }
//
//
//    @Override
//    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
//        SimpleCursorAdapter cursorAdapterCategorias = new SimpleCursorAdapter(
//                this,
//                android.R.layout.simple_list_item_1,
//                data,
//                new String[]{DbTableTipoReceita.CATEGORIA_RECEITA},
//                new int[]{android.R.id.text1}
//        );
//
//        spinnerCategoria.setAdapter(cursorAdapterCategorias);
//
//        int idCategory = registoMovimentos.getTiporeceita();
//
//        for (int i = 0; i < spinnerCategoria.getCount(); i++) {
//            Cursor cursor = (Cursor) spinnerCategoria.getItemAtPosition(i);
//
//            final int posId = cursor.getColumnIndex(DbTableTipoReceita._ID);
//
//            if(idCategory == cursor.getInt(posId)){
//                spinnerCategoria.setSelection(i);
//                break;
//            }
//
//        }
//    }
//
//    /**
//     * Called when a previously created loader is being reset, and thus
//     * making its data unavailable.  The application should at this point
//     * remove any references it has to the Loader's data.
//     *
//     * @param loader The Loader that is being reset.
//     */
//    @Override
//    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
//        registoMovimentosAdapter.refreshData(null);
//    }

}
