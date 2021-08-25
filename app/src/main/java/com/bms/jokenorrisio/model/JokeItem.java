package com.bms.jokenorrisio.model;

import android.media.Image;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.bms.jokenorrisio.R;
import com.xwray.groupie.ViewHolder;

//aqui são definidos as propriedades de exibição = formato de apresentação da piada
// icone (url da imagem) + valor (texto piada)
public class JokeItem {

    private final String iconUrl;
    private final String value;


    public JokeItem(String iconUrl, String value) {
        this.iconUrl = iconUrl;
        this.value = value;

    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getValue() {
        return value;
    }


}

