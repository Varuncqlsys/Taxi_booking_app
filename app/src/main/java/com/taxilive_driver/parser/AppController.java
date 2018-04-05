package com.taxilive_driver.parser;

import android.app.Application;
import android.content.Intent;

import com.taxilive_driver.Services.NetworkServices;
import com.taxilive_driver.util.ConnectivityReceiver;


public class AppController extends Application {


    public static final String TAG = AppController.class
            .getSimpleName();


    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Intent intent = new Intent(mInstance, NetworkServices.class);
        mInstance.startService(intent);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}