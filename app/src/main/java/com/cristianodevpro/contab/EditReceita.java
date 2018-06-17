package com.cristianodevpro.contab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.Calendar;

public class EditReceita extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogFragmentCategoria.ExampleDialogListener{

    /*************Global Variables********************************************/
    public static final String RECEITA = "Receita";
    private DbContabOpenHelper dbContabOpenHelper;
    RegistoMovimentos registoMovimentos = new RegistoMovimentos();
    private TextView textViewSelectDate;
    private Spinner spinnerCategoria;
    private EditText editTextDesignacaoReceita;
    private EditText editTextValorReceita;
    private String id_registo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receita);

        loadSpinnerData();

        //inicializar objetos
        textViewSelectDate = (TextView) findViewById(R.id.textViewSelectedDate);
        spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        editTextDesignacaoReceita = (EditText) findViewById(R.id.editTextDesignacaoReceita);
        editTextValorReceita = (EditText) findViewById(R.id.editTextValorReceita);

        dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());

        try { //getIntent da atividade ListarTodos
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            id_registo = extras.getString("ID");
        } catch (Exception e) {
            Toast.makeText(EditReceita.this, "Não foi possível obter o id", Toast.LENGTH_LONG).show();
        }

        //Buscar dados do registo para colocar nos campos
        registoMovimentos = dbContabOpenHelper.getRegistoFromRecycler(id_registo);
        textViewSelectDate.setText(""+registoMovimentos.getDia()+"/"+registoMovimentos.getMes()+"/"+registoMovimentos.getAno());
        String categoriaReceita = dbContabOpenHelper.getTipoReceitaByID(registoMovimentos.getTiporeceita());
        setSpinnerToValue (spinnerCategoria, categoriaReceita);
        editTextDesignacaoReceita.setText(""+registoMovimentos.getDesignacao());
        editTextValorReceita.setText(""+registoMovimentos.getValor());

    }

    /*****************************Buttons actions*****************************************************/

    public void definirDataEditReceita(View view) { //botão definir data
        android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void atualizarReceita(View view) { //botão atualizar receita DB
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

        //Verificar se o spinner está vaio
        if (spinnerCategoria.getCount() == 0){
            Toast.makeText(this, "Por favor, adicione uma categoria!",Toast.LENGTH_LONG).show();
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

        //Teste
        //      TextView textViewTestDate = (TextView) findViewById(R.id.textViewTestDate);
//        textViewTestDate.setText(""+registoMovimentos.getId_movimento()+"-"+registoMovimentos.getDia()+"-"+registoMovimentos.getMes()+"-"+registoMovimentos.getAno()+"-"+registoMovimentos.getReceitadespesa()+"-"+registoMovimentos.getDesignacao()+"-"+registoMovimentos.getValor()+"-"+registoMovimentos.getTiporeceita());

        //atualizar registo na DB
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
        editTextDesignacaoReceita.setText("");
        editTextValorReceita.setText("");
        textViewSelectedDateReceita.setText("");
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

        TextView textViewSelectedDate = (TextView) findViewById(R.id.textViewSelectedDate);
        textViewSelectedDate.setText(""+dayOfMonth+"/"+month+"/"+year);
    }

    @Override
    public void setTexts(String categoria) {
        try {
            if(checkCategoriaReceita(categoria) != -1) { //Se devolver um id != -1 é porque já exite uma categoria com o nome que vamos inserir
                Toast.makeText(EditReceita.this, "Categoria já existente!",Toast.LENGTH_LONG).show();
                return;
            }
            insertCategoriaReceitaDb(categoria);
            Toast.makeText(EditReceita.this, "Categoria inserida com sucesso!",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(EditReceita.this, "Erro ao inserir a categoria na BD!",Toast.LENGTH_LONG).show();
        }

        loadSpinnerData(); //atualizar spinner
    }

    /*******************************Functions and Methods********************************************/
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

    private void insertCategoriaReceitaDb(String categoria) { //inserir categorias Receitas
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

    private void loadSpinnerData(){ //atualizar spinner
        Spinner spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoriasReceitaFromDb());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    public ArrayList<String> getCategoriasReceitaFromDb(){ //buscar as categorias das receitas da BD
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
