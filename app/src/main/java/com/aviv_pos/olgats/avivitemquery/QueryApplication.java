package com.aviv_pos.olgats.avivitemquery;

import android.app.Application;

/**
 * Created by olgats on 12/04/2016.
 */
public class QueryApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary — это Open Source Java библиотека для обнаружения утечек памяти в отладочных сборках.
        // LeakCanary.install(this);
    }
}
