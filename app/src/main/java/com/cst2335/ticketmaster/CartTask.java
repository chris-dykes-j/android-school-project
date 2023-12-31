package com.cst2335.ticketmaster;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// get attraction
class CartTask extends AsyncTask<String, Void, String> {
    private String str, receiveMsg;

    @Override
    protected String doInBackground(String... strings) {

        // url should be passed as parameters
        String strUrl = "https://app.ticketmaster.com/discovery/v2/events.json?sort=date,asc&dmaId=527&size=5&countryCode=CA&apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb";
        URL url = null;
        try {
            url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
                Log.i("HTTP IS ", "OKAY");
            } else {
                Log.i("HTTP IS ", conn.getResponseCode() + "NO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

