package com.bms.jokenorrisio;

import android.graphics.Color;

import java.util.Random;

public class Colors {

    public  static int ranadonColor(){
        Random random = new Random();
        int r = random.nextInt(0xff);
        int g = random.nextInt(0xff);
        int b = random.nextInt(0xff);

        return Color.argb(0x80, r,g,g);
    }
}
