package com.cristianodevpro.contab;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogFragmentOrcamento.ExampleDialogListener {


    /*****************************Variáveis*****************************/

    private static Boolean isClicked = false;
    private static final String DESPESA = "Despesa";
    private static TextView textViewStatusOrcamento;
    private static DbContabOpenHelper db;
    private static TextView textViewShowSaldoMain;
    private static TextView textViewShowDespesasMain;
    private static TextView textViewShowReceitasMain;
    private static TextView textViewShowDiaMesAno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*************************Construção dos objetos******************************/

        db = new DbContabOpenHelper(getApplicationContext());
        textViewShowSaldoMain = findViewById(R.id.textViewShowSaldoMain);
        textViewShowDespesasMain = findViewById(R.id.textViewShowDespesasMain);
        textViewShowReceitasMain = (TextView) findViewById(R.id.textViewShowReceitasMain);
        textViewShowDiaMesAno = (TextView) findViewById(R.id.textViewShowDiaMesAno);
        textViewStatusOrcamento = (TextView) findViewById(R.id.textViewStatusOrcamento);

        //Debug
        //db.deleteAllRegistosMovimentos();
        //db.deleteAllOrcamento();
        //db.deleteAllCategoriaReceita();
        //db.deleteAllCategoriaDespesa();

        initComponents();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initComponents();
    }

    /*******************************************Buttons actions**************************************************/

    @Override
    public void onBackPressed() { //botão back
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void exit(View view) { //botão sair
        this.finish();
        System.exit(0);
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
            Intent i = new Intent(this, Definicoes.class);
            startActivity(i);
            isClicked = true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ldia) {
            Intent i = new Intent(this, ListarDia.class);
            startActivity(i);
        } else if (id == R.id.nav_lmes) {
            Intent i = new Intent(this, ListarMes.class);
            startActivity(i);
        } else if (id == R.id.nav_lano) {
            Intent i = new Intent(this, ListarAno.class);
            startActivity(i);
        } else if (id == R.id.nav_ltodos) {
            Intent i = new Intent(this, ListarTodos.class);
            startActivity(i);
        } else if (id == R.id.nav_orca) {
            DialogFragmentOrcamento dialogFragmentOrcamento = new DialogFragmentOrcamento();
            dialogFragmentOrcamento.show(getSupportFragmentManager(), "DialogFragmentOrcamento");
        } else if (id == R.id.nav_sobre) {
            Intent i = new Intent(this, Sobre.class);
            startActivity(i);
        } else if (id == R.id.nav_gestcategorias) {
            Intent i = new Intent(this, GerirCategorias.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setValue(double orcamento) { //Ação do botão "DEFINIR ORÇAMENTO" do dialog fragment
        try {
            insertOrcamentoDb(orcamento);
            Toast.makeText(MainActivity.this, R.string.valor_orca_definido_sucesso,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, R.string.erro_inserir_valor_bd,Toast.LENGTH_LONG).show();
        }

    }

    public void novaReceita(View view) { //Botão "NOVA RECEITA"
        Intent i = new Intent(this, NovaReceita.class);
        startActivity(i);
    }

    public void novaDespesa(View view) { //Botão "NOVA DESPESA"
        Intent i = new Intent(this, NovaDespesa.class);
        startActivity(i);
    }

    /*********************************Functions and Methods****************************************/

    /**
     *
     * @param orcamento
     *
     * inserir valor orçamento na BD
     */
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

    /**
     * @return ultimo valor de orçamento definido
     */
    private double getValorOrcamentoFromDb(){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableOrcamento dbTableOrcamento = new DbTableOrcamento(db);

        Cursor cursor = dbTableOrcamento.query(DbTableOrcamento.VALOR_COLUMN, DbTableOrcamento._ID+"= (SELECT MAX(id_orcamento) FROM Orcamento)", null, null, null, null);

        double valor = 0;
        valor = DbTableOrcamento.getValorOrcamentoFromDb(cursor);

        cursor.close();
        db.close();
        return valor;
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
     * @param mes
     * @return somatório dos valores das despesas por mes
     */
    private double getValorDespesaMesFromDb(int mes){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Escrita
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);

        Cursor cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"},DbTableRegistoMovimentos.RECEITADESPESA+" =? AND "+DbTableRegistoMovimentos.MES+" =?",new String[]{"Despesa", Integer.toString(mes)},null,null,null);

        double valor = 0;
        valor = DbTableRegistoMovimentos.getValorDespesasFromDb(cursor);

        cursor.close();
        db.close();
        return  valor;
    }

    /**
     * verifica se a despesa ultrapassa os limites de orçamento definidos
     */
    private void checkOrcamento(){
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        double currentMonth = getCurrentMonth();
        double valorAlerta = getValorOrcamentoFromDb() - (getValorDespesaMesFromDb((int) currentMonth));
        if((getValorOrcamentoFromDb() < (getValorDespesaMesFromDb((int) currentMonth))) && getValorOrcamentoFromDb() != 0){
            textViewStatusOrcamento.setText(R.string.ultrapassou_limit_orca_mensal);
            imageView.setImageResource(R.drawable.status_main_vermelho);
        }else if (valorAlerta < 50 && valorAlerta != 0 && getValorOrcamentoFromDb()!=0){
            textViewStatusOrcamento.setText(""+String.format("%.2f",valorAlerta)+" "+getString(R.string.euros_atingir_limit_orca_mensal));
        }else if (valorAlerta == 0 && getValorDespesaMesFromDb((int)currentMonth) != 0){
            textViewStatusOrcamento.setText(R.string.atingiu_limit_orca_mensal);
        }else{
            imageView.setImageResource(R.drawable.status_main);
        }
    }

    /**
     * @return somatório dos valores das despesas
     */
    private double getValorDespesaFromDb(){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);

        Cursor cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"},DbTableRegistoMovimentos.RECEITADESPESA+" =?",new String[]{"Despesa"},null,null,null);

        double valor = 0;
        valor = DbTableRegistoMovimentos.getValorDespesasFromDb(cursor);

        cursor.close();
        db.close();
        return  valor;
    }

    /**
     * @return somatório do valor das receitas
     */
    private double getValorReceitaFromDb(){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);

        Cursor cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =?",new String[]{"Receita"},null, null, null);

        double valorReceita = 0;
        valorReceita = DbTableRegistoMovimentos.getValorReceitasFromDb(cursor);

        cursor.close();
        db.close();
        return valorReceita;
    }

    /**
     * @param tipo
     * @return somatório do valor das receitas por tipo
     */
    private double getValorReceitaTipoFromDb(String tipo){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);

        Cursor cursor = null;

        if (tipo.equals("ano")){
            cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =? AND "+DbTableRegistoMovimentos.ANO+" =?",new String[]{"Receita",Integer.toString(DadosDefinicoesToMain.getAno())},null, null, null);
        }else if(tipo.equals("dia")){
            cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =? AND "+DbTableRegistoMovimentos.DIA+" =? AND "+DbTableRegistoMovimentos.MES+" =? AND "+DbTableRegistoMovimentos.ANO+" =?",new String[]{"Receita",Integer.toString(DadosDefinicoesToMain.getDia()),Integer.toString(DadosDefinicoesToMain.getMes()),Integer.toString(DadosDefinicoesToMain.getAno())},null, null, null);
        }else if (tipo.equals("mes")){
            cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =? AND "+DbTableRegistoMovimentos.MES+" =? AND "+DbTableRegistoMovimentos.ANO+" =?",new String[]{"Receita",Integer.toString(DadosDefinicoesToMain.getMes()),Integer.toString(DadosDefinicoesToMain.getAno())},null, null, null);
        }else if (tipo.equals("todos")){
            cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =?",new String[]{"Receita"},null, null, null);
        }

        double valorReceita = 0;
        valorReceita = DbTableRegistoMovimentos.getValorReceitasFromDb(cursor);

        cursor.close();
        db.close();
        return valorReceita;
    }

    /**
     * @param tipo
     * @return somatório do valor das despesas por tipo
     */
    private double getValorDespesaTipoFromDb(String tipo){
        //Abrir a BD
        DbContabOpenHelper dbContabOpenHelper = new DbContabOpenHelper(getApplicationContext());
        //Leitura
        SQLiteDatabase db = dbContabOpenHelper.getReadableDatabase();

        DbTableRegistoMovimentos tableRegistoMovimentos = new DbTableRegistoMovimentos(db);

        Cursor cursor = null;

        if (tipo.equals("ano")){
            cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =? AND "+DbTableRegistoMovimentos.ANO+" =?",new String[]{DESPESA,Integer.toString(DadosDefinicoesToMain.getAno())},null, null, null);
        }else if(tipo.equals("dia")){
            cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =? AND "+DbTableRegistoMovimentos.DIA+" =? AND "+DbTableRegistoMovimentos.MES+" =? AND "+DbTableRegistoMovimentos.ANO+" =?",new String[]{DESPESA,Integer.toString(DadosDefinicoesToMain.getDia()),Integer.toString(DadosDefinicoesToMain.getMes()),Integer.toString(DadosDefinicoesToMain.getAno())},null, null, null);
        }else if (tipo.equals("mes")){
            cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =? AND "+DbTableRegistoMovimentos.MES+" =? AND "+DbTableRegistoMovimentos.ANO+" =?",new String[]{DESPESA,Integer.toString(DadosDefinicoesToMain.getMes()),Integer.toString(DadosDefinicoesToMain.getAno())},null, null, null);
        }else if (tipo.equals("todos")){
            cursor = tableRegistoMovimentos.query(new String[]{"SUM("+DbTableRegistoMovimentos.VALOR+")"}, DbTableRegistoMovimentos.RECEITADESPESA+" =?",new String[]{DESPESA},null, null, null);
        }

        double valorDespesa = 0;
        valorDespesa = DbTableRegistoMovimentos.getValorDespesasFromDb(cursor);

        cursor.close();
        db.close();
        return valorDespesa;
    }

    private void initComponents(){
        //Ler saldo e apresentá-lo numa text view
        double saldo = getValorReceitaFromDb()-getValorDespesaFromDb();
        textViewShowSaldoMain.setText(""+String.format("%.2f",saldo)+"€");

        //Ler valor das despesas e apresentá-las numa textview
        double valorDespesas = getValorDespesaFromDb();
        textViewShowDespesasMain.setText(""+String.format("%.2f",valorDespesas)+"€");

        //Ler valor das receitas e apresentá-las numa textview
        double valorReceitas = getValorReceitaFromDb();
        textViewShowReceitasMain.setText(""+String.format("%.2f",valorReceitas)+"€");

        //Se o botão das definições foi clicado
        if(isClicked) {
            double valorReceitasTipo = getValorReceitaTipoFromDb(DadosDefinicoesToMain.getTipo());
            double valorDespesasTipo = getValorDespesaTipoFromDb(DadosDefinicoesToMain.getTipo());
            textViewShowReceitasMain.setText(""+String.format("%.2f",valorReceitasTipo)+"€");
            textViewShowDespesasMain.setText(""+String.format("%.2f",valorDespesasTipo)+"€");
            textViewShowSaldoMain.setText(""+String.format("%.2f",saldo)+"€");

            switch (DadosDefinicoesToMain.getTipo()){
                case "dia":
                    textViewShowDiaMesAno.setText(""+DadosDefinicoesToMain.getDia()+"/"+DadosDefinicoesToMain.getMes()+"/"+DadosDefinicoesToMain.getAno());
                    break;
                case "mes":
                    textViewShowDiaMesAno.setText(""+DadosDefinicoesToMain.getMes()+"/"+DadosDefinicoesToMain.getAno());
                    break;
                case "ano":
                    textViewShowDiaMesAno.setText("Ano "+DadosDefinicoesToMain.getAno());
                    break;
                default:
                    textViewShowDiaMesAno.setText("Geral");
            }
        }

        checkOrcamento();
    }


}
