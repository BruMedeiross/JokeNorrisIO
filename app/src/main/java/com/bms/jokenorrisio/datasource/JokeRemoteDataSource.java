package com.bms.jokenorrisio.datasource;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.bms.jokenorrisio.model.JokeItem;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

//obeto responsavel pelas requisicoes web- thread paralela
// evita travar a execucao do app
public class JokeRemoteDataSource {

    // callback - são chamadas que sao executa para devolver alguma coisa para quem a chamou/pediu a informação
    // neste caso o presenter pediu a informação
    //depois de ter a resposta(texto da piada) devolvermos ao presenter, que devolve a view(activityjoke)
    public interface JokeCallback {
        void onSuccess(JokeItem response);

        void onError(String message);

        void onComplete();
    }

    //aki chama o metodo que fez a requisicao(category task)
    //precisa ser chamado no presenter, o presenter tbm implementa o callback
    public void findJokeBy(JokeCallback callback, String category) {
        new JokeTask(callback, category).execute();
    }

    //asynctask - classe para fazer apenas requisições na internet
    //3 tipos genericos (parametros, (progress - ex tempo de execucao de download), tipo de resposta)
    private static class JokeTask extends AsyncTask<Void, Void, JokeItem> {

        private final JokeCallback callback;
        private final String category;

        private String errorMessage;

        //
        public JokeTask(JokeCallback callback, String category) {
            this.callback = callback;
            this.category = category;
        }
        //METODOS DA ASYNC - DOINBACK E POSTEXECUTE
        //requisicao web
        @Override
        protected JokeItem doInBackground(Void... voids) {
            //objeto - precisa retornar uma info
            JokeItem jokeItem = null;

            // estava assim antes - HttpsURLConnection urlConnection = null;
            HttpsURLConnection urlConnection;
            try {
                String endpoint = String.format("%s?category=%s", Endpoint.GET_JOKE, category);
                //definindo url que sera manipulada (get joke via category, neste caso)
                URL url = new URL(endpoint);

                //abrindo conexao
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setReadTimeout(2000);
                urlConnection.setConnectTimeout(2000);

                //status da resposta(200 = sucesso, >400 = erro)
                int responseCode = urlConnection.getResponseCode();
                if (responseCode > 400) {
                    throw new IOException("Erro na comunicação com o servidor");
                }

                //transformando dados bytes em json
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //leitor de json
                JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
                //como nao é um array - mas um objeto json  // parser do JSON - transforma o json num objeto joke

                //primeiro -va para o começo do objeto
                jsonReader.beginObject();

                //depois - guarda apenas as variaveis que precisa(icone e valor)
                String iconURL = null;
                String value = null;

                //looping no json
                while (jsonReader.hasNext()){
                    //token é uma sequencia de caracter
                    JsonToken token = jsonReader.peek();


                    //condicao de IF
                    // se a chave for igual category(skip - pule o valor)
                    // armazene o valor nas variaveis apenas icon e value
                    //caso contratio pule
                    if(token == JsonToken.NAME){
                        String name = jsonReader.nextName();
                        if(name.equals("category"))
                            jsonReader.skipValue();
                        else if (name.equals("icon_url"))
                            iconURL = jsonReader.nextString();
                        else if(name.equals("value"))
                            value = jsonReader.nextString();
                        else
                            jsonReader.skipValue();
                    }

                }
                //DEPOIS DE PEGAR AS VARIAVEIS QUE EU PRECISAVA
                // GUARDA NA VARIAVEL EM JOKEITEM E RETORNA A RESPOSTA
                jokeItem = new JokeItem(iconURL,value);
                jsonReader.endObject();

            } catch (MalformedURLException e) {
                errorMessage = e.getMessage();
            } catch (IOException e) {
                errorMessage = e.getMessage();
            }

            //retorne a piada de acordo com a categoria!!!
            return jokeItem;
        }

        //==CATEGORY REMOTE
        @Override
        protected void onPostExecute(JokeItem jokeItem) {

            if (errorMessage != null) {
                callback.onError(errorMessage);
            } else {
                callback.onSuccess(jokeItem);
            }
            callback.onComplete();
        }
    }


}
