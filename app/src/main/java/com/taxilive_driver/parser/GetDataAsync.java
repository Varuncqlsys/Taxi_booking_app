package com.taxilive_driver.parser;

import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;

/**
 * @author Ankit Gupta
 */
public abstract class GetDataAsync extends AsyncTask<String, Void, String> {

    Context mContext;
    ServiceHandler mHandler;
    HashMap<String, String> params_values;


    public GetDataAsync(Context context, HashMap<String, String> params) {
        mContext = context;
        this.params_values = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String responseStr = "";
        mHandler = new ServiceHandler();
        if (params_values != null) {
            responseStr = mHandler.makeServiceConnectionPost(params[0], params_values);
        }
        return responseStr;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            try {
                getValueParse(result);
            } catch (Exception e) {
            }
        }
    }

    public abstract void getValueParse(String listValue);
}
