package com.taxilive_driver.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 1/13/2017.
 */

public abstract class GetAsync extends AsyncTask<String, Void, String> {

    String url = "";
    RequestBody requestBody;
    Context context;
    WeakReference<Context> contextWeakReference;
    AlertDialog.Builder builder;

    public GetAsync(Context context, String url, RequestBody requestBody) {
        this.url = url;
        this.requestBody = requestBody;
        contextWeakReference = new WeakReference<Context>(context);
        this.context = contextWeakReference.get();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonData = "";
        try {
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(url)
                        .build();
                try {
                    Response responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                } catch (SocketTimeoutException ex) {
                    showDialog();
                } catch (ConnectException ex) {
                    showDialog();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) getValueParse(result);
    }

    public abstract void getValueParse(String listValue);

    public abstract void retry();

    private void showDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Warning!");
        builder.setMessage("Connection problem");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        retry();
                    }
                });

        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

    }
}