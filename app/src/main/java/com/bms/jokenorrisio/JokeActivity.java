package com.bms.jokenorrisio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.constraint.solver.Cache;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bms.jokenorrisio.datasource.JokeRemoteDataSource;
import com.bms.jokenorrisio.model.JokeItem;
import com.bms.jokenorrisio.presentation.JokePresenter;
import com.squareup.picasso.Picasso;

public class JokeActivity extends AppCompatActivity {
    static final String CATEGORY_KEY = "category_key";
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //pega a referencia da categoria - ex: movie
        if (getIntent().getExtras()!= null){
            String category = getIntent().getExtras().getString(CATEGORY_KEY);
            Log.i("TESTE", category);

            if (getSupportActionBar() !=null){
                //titulo da categoria exibido na action bar
                getSupportActionBar().setTitle(category);

                //seta de voltar na action bar
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                //instancias - datasourse e presenter

                JokeRemoteDataSource dataSource =  new JokeRemoteDataSource();
                // presenter sempre precisa de uma referencia da activity(joke) para chamar a view de volta
                // e do data source para fazer as requisições
                final JokePresenter presenter = new JokePresenter (this, dataSource);
                //feito isso no presenter podemos chamar o metodo que encontra a piada pela... variavel(categoria neste caso)
                //esta no presenter o metodo findJokeBY...
                presenter.findJokeBy(category);

                //fab - botão refresh
                //qdo clicado deve atualizar, encontrando a piada novamente pela categoria
                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener((View view) ->
                        presenter.findJokeBy(category));


            }
        }


    }
    //evento de sucesso- vem do servidor um objeto do tipo piada já formatado (jokeItem)
    public void showJoke(JokeItem jokeItem){
        //pegando o texto da piada - value
        TextView txtJoke = findViewById(R.id.txt_joke);
        txtJoke.setText(jokeItem.getValue());

        ImageView iv = findViewById(R.id.img_icon);
        Picasso.get().load(jokeItem.getIconUrl()).into(iv);

    }

    //FALHA
    //msg de erro do servidor em caso de erro na requisicao == activity
    public void showFailure(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    //exibe a progress pro usuario == activity
    public void showProgressBar(){
        if (progress== null){
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.loading));
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
        progress.show();
    }
    //esconde a progress apos o retorno do banco de dados == activity
    public void hideProgressBar(){
        if(progress != null){
            progress.hide();
        }
    }

    //ao item ser selecionado irá direcionar para a HOME
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return true; }
    }
}