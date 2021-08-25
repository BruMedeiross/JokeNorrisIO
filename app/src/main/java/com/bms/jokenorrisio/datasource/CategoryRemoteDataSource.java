package com.bms.jokenorrisio.datasource;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

//convesão sobre nome: nome de uso - neste caso CATEGORYRemote(dadosexternos)DataSouce;
//onde estao os dados - LOCAL(no proprio cel) ou REMOTE(dados externos); banco de dados - DATASOURCE ou REPOSITORY
//obeto responsavel pelas requisicoes web- thread paralela
// evita travar a execucao do app
public class CategoryRemoteDataSource {

    // callback - são chamadas que sao executa para devolver alguma coisa para quem a chamou/pediu a informação
    // neste caso o presenter pediu a informação
    //depois de ter a resposta(lista) devolvermos ao presenter, que devolve a view(activity)
    public interface  ListCategoriesCallback{
        void onSuccess(List<String> response);

        void onError(String message);

        void onComplete(List<String> response);
    }

    public void findAll(ListCategoriesCallback callback){
        //aki chama o metodo que fez a requisicao(category task)
        //precisa ser chamado no presenter, o presenter tbm implementa o callback
        new CategoryTask(callback).execute(); }


    //asynctask - classe para fazer apenas requisições na internet
    //3 tipos genericos (parametros, (progress - ex tempo de execucao de download), tipo de resposta)
    private static class CategoryTask extends AsyncTask<Void,Void, List<String>>{

        private final  ListCategoriesCallback callback;
        private String errorMessage;

        public CategoryTask(ListCategoriesCallback callback) {
            this.callback = callback;
        }

        //executado na async task - aqui bate na internet e retorna o json pronto
        @Override
        protected List<String> doInBackground(Void... voids) {
            //lista esta vazia - precisa ser respondida
            List<String> response = new ArrayList<>();

            // estava assim antes - HttpsURLConnection urlConnection = null;
            HttpsURLConnection urlConnection;
            try{
                //popular a lista:endpoint - definindo url que sera manipulada (get category, neste caso)
                URL url =  new URL(Endpoint.GET_CATEGORIES);

                //abrindo conexao
                urlConnection = (HttpsURLConnection)url.openConnection();
                urlConnection.setReadTimeout(2000);
                urlConnection.setConnectTimeout(2000);

                //status da resposta(200 = sucesso, >400 = erro)
                int responseCode =  urlConnection.getResponseCode();
                if(responseCode>400){
                    throw new IOException("Erro na comunicação com o servidor");
                }

                //transformando dados bytes em json
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //leitor de json
                JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
                //inicio da lista
                jsonReader.beginArray();
                //enquanto tiver um prox elemento, inclua no array
                while(jsonReader.hasNext()){
                    response.add(jsonReader.nextString());
                }
                //feche o array
                jsonReader.endArray();


            } catch (MalformedURLException e){
                errorMessage = e.getMessage();
            } catch (IOException e) {
                errorMessage = e.getMessage();
            }

            //lista pronta!!!
            return response;
        }

        //executado na main thread - reposta pronta sera manipulado na interface grafica,
        // lista sera exibida ao usuario - sucesso (string) ou falha(erros mssg)
        @Override
        protected void onPostExecute(List<String> strings) {
            if(errorMessage != null){
                Log.i("TESTE", errorMessage);
                callback.onError(errorMessage);
            }else{
                Log.i("TESTE", strings.toString());
                callback.onSuccess(strings);
            }
            callback.onComplete(strings);
        }
    }
}
