package com.taxilive_driver.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.taxilive_driver.parser.AppController;
import com.taxilive_driver.util.ConnectivityReceiver;
import com.taxilive_driver.util.SavePref;

import java.util.ArrayList;


public class NetworkServices extends Service implements ConnectivityReceiver.ConnectivityReceiverListener {
    Context context;
    SavePref savePref;
    private int uploadCounter;
    ArrayList<String> uplaodList;
    boolean isConnect = false;
    String dataCut = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void NetworkServices(Context context) {
        this.context = context;
        savePref = new SavePref(context);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppController.getInstance().setConnectivityListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected, Context context) {

        this.context = context;
        this.isConnect = isConnected;

        if (isConnected) {

        }

    }


}
