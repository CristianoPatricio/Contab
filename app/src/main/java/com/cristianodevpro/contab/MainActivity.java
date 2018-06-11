package com.cristianodevpro.contab;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogFragmentOrcamento.ExampleDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DbContabOpenHelper db = new DbContabOpenHelper(getApplicationContext());

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();
            //}
      //  });

        //Ler valor do orçamento e apresentá-lo numa textview;
        TextView textViewShowSaldoMain = findViewById(R.id.textViewShowSaldoMain);
        double valorOrcamento = getValorOrcamentoFromDb();
        textViewShowSaldoMain.setText(""+valorOrcamento+"€");

        //Apagar todos os orçamentos
        //DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //db.deleteAllOrcamento();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ldia) {
            // Handle the camera action
        } else if (id == R.id.nav_lmes) {

        } else if (id == R.id.nav_lano) {

        } else if (id == R.id.nav_ltodos) {

        } else if (id == R.id.nav_orca) {
            DialogFragmentOrcamento dialogFragmentOrcamento = new DialogFragmentOrcamento();
            dialogFragmentOrcamento.show(getSupportFragmentManager(), "DialogFragmentOrcamento");
        } else if (id == R.id.nav_sobre) {
            Intent i = new Intent(this, Sobre.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void setValue(double orcamento) {
        //Teste
        TextView textViewShowSaldoMain = (TextView) findViewById(R.id.textViewShowSaldoMain);
        //textViewShowSaldoMain.setText(""+orcamento);
        //DbContabOpenHelper db = new DbContabOpenHelper(getApplicationContext());
        try {
            insertOrcamentoDb(orcamento); //valor do orçamento é inserido na bd
            Toast.makeText(MainActivity.this, "Limite de orçamento definido com sucesso!",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Erro ao inserir valor na BD",Toast.LENGTH_LONG).show();
        }

        //Teste
        double valorOrcamento = getValorOrcamentoFromDb();
        textViewShowSaldoMain.setText(""+valorOrcamento+"€");
    }

    private void insertOrcamentoDb(double orcamento) {
        //Abrir BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Op. escrita
        SQLiteDatabase db = dbContabOpenHelper.getWritableDatabase();

        DbTableOrcamento tableOrcamento = new DbTableOrcamento(db);

        Orcamento Orcamento = new Orcamento();
        Orcamento.setValor(orcamento);

        tableOrcamento.insert(DbTableOrcamento.getContentValues(Orcamento));
        db.close();
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

    public String getNowDate(){ //Data e Hora atuais
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

    public void novaReceita(View view) {
        Intent i = new Intent(this, NovaReceita.class);
        startActivity(i);
    }

    public void novaDespesa(View view) {
        Intent i = new Intent(this, NovaDespesa.class);
        startActivity(i);
    }
}
