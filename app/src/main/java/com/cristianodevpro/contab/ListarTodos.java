package com.cristianodevpro.contab;

import android.app.LoaderManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ListarTodos extends AppCompatActivity {

    public static final int CURSOR_LOADER_ID = 0;
    private RegistoMovimentosAdapter adapter;
    private RecyclerView recyclerViewRegistos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_todos);

        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(this);
        adapter = new RegistoMovimentosAdapter(dbContabOpenHelper.getListRegistoMovimentos(), this, recyclerViewRegistos);

        recyclerViewRegistos = (RecyclerView) findViewById(R.id.recyclerViewListarTodos);
        recyclerViewRegistos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRegistos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRegistos.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewRegistos.setAdapter(adapter);
    }

}
