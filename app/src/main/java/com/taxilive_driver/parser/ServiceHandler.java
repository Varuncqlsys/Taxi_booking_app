package com.taxilive_driver.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Deepak Panwar
 */
public class ServiceHandler {

    private String response = "";
    private HttpURLConnection conn;

    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {

    }

    public String makeServiceConnectionPost(String strUrl, HashMap<String, String> postDataParams) {
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(30000); /* milliseconds */
            conn.setConnectTimeout(18000); /* milliseconds */
            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            conn.setDoOutput(true);
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            InputStream stream = conn.getInputStream();
//          InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = convertInputStreamToString(stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
