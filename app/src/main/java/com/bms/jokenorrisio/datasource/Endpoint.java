package com.bms.jokenorrisio.datasource;

//ENDPOINT É ONDE SERA DEFINIDA A URL QUE SERA MANIPULADA

public class Endpoint {

    //A URL BASE PODERA SER UTILIZADA PARA OUTRAS REQUISIOCES - BASE+ EXTENSOES
    static final String BASE_URL = "https://api.chucknorris.io/";

    static final String GET_CATEGORIES = BASE_URL+"jokes/categories";

    static final String GET_JOKE = BASE_URL+"jokes/random"; //?category={category}

}
