package com.bms.jokenorrisio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import android.support.design.widget.NavigationView;

import android.support.annotation.NonNull;

import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bms.jokenorrisio.datasource.CategoryRemoteDataSource;
import com.bms.jokenorrisio.model.CategoryItem;
import com.bms.jokenorrisio.presentation.CategoryPresenter;
import com.xwray.groupie.GroupAdapter;


import java.util.List;

//mvp - model, view, presenter
//activity apenas declara funcoes que se dizem respeito a visualizacao grafica do usuario
//a parte logica de exibição ou não, fica a critério do presenter

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private GroupAdapter adapter;
    private CategoryPresenter presenter;
    private ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toggle -  torna o menu funcional, se o menu esta aberto fecha e vice e versa
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //quem a implemeta
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView rv = findViewById(R.id.rv_main);

        adapter = new GroupAdapter();
        adapter.setOnItemClickListener((item, view) ->{
            //intencao direciona para a activity joke
            Intent intent = new Intent(MainActivity.this, JokeActivity.class);

            CategoryItem categoryItem = (CategoryItem) item;
            //pega o título da categoria
            intent.putExtra(JokeActivity.CATEGORY_KEY,((CategoryItem) item).getCategoryName());
            startActivity(intent);
        });

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //requisicao web
        CategoryRemoteDataSource dataSource = new CategoryRemoteDataSource();
        new CategoryPresenter(this, dataSource).requestAll();

        //igual ao de cima(mais enxuto)
        // presenter = new CategoryPresenter(this);
        //presenter.requestAll();
    }


    //exibe a progress pro usuario
    public void showProgressBar(){
        if (progress== null){
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.loading));
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
        progress.show();
    }
    //esconde a progress apos o retorno do banco de dados
    public void hideProgressBar(){
        if(progress != null){
            progress.hide();
        }
    }

    //devolve dados com sucesso ou falha
    //SUCESSO
    //mostra lista pronta no adpter
    public void showCategories(List<CategoryItem>categoryItems){
        adapter.addAll(categoryItems);
        adapter.notifyDataSetChanged();
    }
    //FALHA
    //msg de erro do servidor
    public void showFailure(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }



    //metodo q escuta o botao de voltar,
    //fecha a activity ou se o menu estiver aberto fecha o menu e depois a activity
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //esse metodo onde ele por meio do id do menu direciona e faz a ação do envento de click
    //drawer layout apos clicar em um dos botões fecha o menu lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
  /* FAB - BOTÃO FLUTUANTE
        FloatingActionButton fab = findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        //snack bar semelhante ao toast - apenas uma vizualizacao diferente
                            Snackbar.make(view, "Atualizando...", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });*/

           /* @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
            }*/

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


//xgoup: essa classe faz a logica do adapter de forma automatica(bind, on bind)



