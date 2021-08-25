package com.bms.jokenorrisio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    private View mContentView;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // tira a status bar e deixa a tela full screen
            //Remoção atrasada de status e barra de navegação
            // Observe que algumas dessas constantes são novas a partir da API 16 (Jelly Bean)
            // e API 19 (KitKat). É seguro usá-los, pois são embutidos
            // em tempo de compilação e não faz nada em dispositivos anteriores.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            delayedShow(1500);//delai de 1,5 segundos
        }
    };
    //parte de exibicao: é onde ira exibir a activitymain
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };



    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            show();
        }
    };


    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        //definicao do layout


        mContentView = findViewById(R.id.fullscreen_content);
        //

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Acione o hide () inicial logo após a atividade ter sido
        //criado (on create), para sugerir brevemente ao usuário que a IU controla
        //Estão disponíveis
        delayedHide(100);
    }



    private void hide() {

        // Schedule a runnable to remove the status and navigation bar after a delay
        //Programe um executável para remover o status e a barra de navegação após um atraso
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        // mostra a barra do sistema
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    // mHideRunnable - objeto responsaovel por escutar eventos q acontecem em background
    // conceito de thread - enquanto o app é inicializado executa um processo paralelo
    // chama o metodo hide, hide: remover o status e a barra de navegação após um atraso
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    // ira esconder o mshowRunnable
    // chama o metodo show, show- esconde todos os itens, tela fica full e depois de 1,5s mostra a status bar
    // executa a parte 2 de mostragem, executa a activity
    private void delayedShow(int delayMillis) {
        mHideHandler.removeCallbacks(mShowRunnable);
        mHideHandler.postDelayed(mShowRunnable, delayMillis);
    }



}