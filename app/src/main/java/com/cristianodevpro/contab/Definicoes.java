package com.cristianodevpro.contab;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Definicoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinnerOpcoesQuadroMain = (Spinner) findViewById(R.id.spinnerOpcoesQuadroMain);
        List<String> list = new ArrayList<>();
        list.add("Resultados dia");
        list.add("Resultados mês");
        list.add("Resultados ano");
        list.add("Todos");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpcoesQuadroMain.setAdapter(adapter);

        TextView textViewShowValorOrcamento = (TextView) findViewById(R.id.textViewShowValorOrcamento);
        double valorOrcamento = getValorOrcamentoFromDb();
        textViewShowValorOrcamento.setText(""+valorOrcamento+"€");
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
