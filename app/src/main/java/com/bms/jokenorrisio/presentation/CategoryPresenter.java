package com.bms.jokenorrisio.presentation;

import android.os.Handler;

import com.bms.jokenorrisio.Colors;
import com.bms.jokenorrisio.MainActivity;
import com.bms.jokenorrisio.datasource.CategoryRemoteDataSource;
import com.bms.jokenorrisio.model.CategoryItem;
import java.util.ArrayList;
import java.util.List;
//MVP = MODEL - VIEW - PRESENTER
//actiivity escuta os eventos ex: botão clicado na view
//presenter irá dizer a activyty o que sera feito
//como vair fazer, o presenter decide - ex: busca no banco de dados a lista de cat neste caso
public class CategoryPresenter implements CategoryRemoteDataSource.ListCategoriesCallback {
    /*
    //metodo static - só é executado QDO a classe criar vida
    // quando houver referencia da classe
    private static List<CategoryItem> fakeresponse = new ArrayList<>();
    metod subsitituido, antes chamado localmente agora pelo datasource
    */
    private final MainActivity view;
    private final CategoryRemoteDataSource dataSource;

                                                            //LISTA FAKE DE DADOS PARA TESTE - SUBISTITUIDA PELO DATA SOURCE
                                                            /*static {
                                                                fakeresponse.add(new CategoryItem("Categoria 1", 0xFF00FFFF));

                                                            }*/

    //chamando a requisicao - category remote(metodo assync task)
    public CategoryPresenter(MainActivity mainActivity, CategoryRemoteDataSource dataSource) {
        this.view =  mainActivity;
        this.dataSource = dataSource;

    }
    public void requestAll() {
        //chamando um servidor HTTP
        this.view.showProgressBar();
        this.dataSource.findAll(this);
                //apagado- metodo fake subistituido por datasource
                // this.request();
    }

    //resposta do servior - sucesso
    @Override
    public void onSuccess(List<String> response) {
        List<CategoryItem> categoryItems =  new ArrayList<>();
        for(String val: response){
            categoryItems.add(new CategoryItem(val, Colors.ranadonColor()));
        }
         view.showCategories(categoryItems);
    }

    @Override
    //resposta do servior - falha
    public void onError(String message){
        this.view.showFailure(message);
    }
    //depois de buscar os dados no servidor
    //havendo sucesso ou falha - esconderá a progressbar
    @Override
    public void onComplete(List<String> response) {
        this.view.hideProgressBar();
    }


                        /*
                        //METODO FAKE REQUEST- funcao de buscar dados
                        //USADO PARA TESTE E SUBISTITUIDO DEPOIS PELO DATA SOURCE
                        //busca no servidor e traz a lista de cat


                        private void request(){
                            //handler simula o delay
                            new Handler().postDelayed(()->{
                                try{
                                    onSuccess(fakeresponse);
                                             //antes, apagou: forçando a dar o erro
                                             // throw new Exception("Falha ao buscar categorias");
                                }catch (Exception e){
                                    onError(e.getMessage());
                                }finally {
                                    onComplete();
                                }
                            },5000);*/


    }




