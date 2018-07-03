package com.cristianodevpro.contab;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class ListarTodos extends AppCompatActivity {

    /******************Variáveis**********************/

    private RegistoMovimentosAdapter adapter;
    private RecyclerView recyclerViewRegistos;
    private DbContabOpenHelper dbContabOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_todos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /********************Construção dos objetos***********************/
        dbContabOpenHelper = new DbContabOpenHelper(this);
        recyclerViewRegistos = (RecyclerView) findViewById(R.id.recyclerViewListarTodos);

        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponents();
    }

    /************************************Buttons actions*******************************************/

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /*********************************Funtions and Methods******************************************/

    /**
     * Inicializa todos os componentes
     */
    private void initComponents(){
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentos(), this, recyclerViewRegistos);

        //verificar se há registos
        if (dbContabOpenHelper.getListRegistoMovimentos().size()==0){
            Toast.makeText(this, R.string.nao_foram_encontrados_registos_todos, Toast.LENGTH_LONG).show();
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRegistos.setLayoutManager(layoutManager);
        recyclerViewRegistos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRegistos.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewRegistos.setAdapter(adapter);
    }


}
