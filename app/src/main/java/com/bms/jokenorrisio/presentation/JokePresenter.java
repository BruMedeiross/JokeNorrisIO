package com.bms.jokenorrisio.presentation;

import com.bms.jokenorrisio.JokeActivity;
import com.bms.jokenorrisio.datasource.JokeRemoteDataSource;
import com.bms.jokenorrisio.model.JokeItem;

import javax.sql.DataSource;


//actiivity escuta os eventos ex: botão clicado na categoria
//presenter irá dizer a activyty o que ira mostrar mas não aonde
//o que vai mostrar e o presenter q decide - ex: uma progress, um dilog, um texto
//aonde ira mostrar é de responsabilidade da VIEW

public class JokePresenter implements JokeRemoteDataSource.JokeCallback {

    private final JokeActivity view;
    private final JokeRemoteDataSource dataSource;

    //construtor
    public JokePresenter(JokeActivity jokeActivity, JokeRemoteDataSource dataSource) {
    this.view = jokeActivity;
    this.dataSource = dataSource;

    }

    //metodo - o que faz?
    // primeiro - mostra a progress bar
    // segundo - faz o callback e chama a piada de acordo com a categoria
    public void findJokeBy(String category) {
        this.view.showProgressBar();
        this.dataSource.findJokeBy(this,category);
    }

    //implementa os 3 metodos do callback(on sucess, failure e oncomplete)
    @Override
    public void onSuccess(JokeItem response) {
        this.view.showJoke(response);
    }

    @Override
    public void onError(String message) {

        this.view.showFailure(message);
    }

    @Override
    public void onComplete() {
        this.view.hideProgressBar();
    }
}
