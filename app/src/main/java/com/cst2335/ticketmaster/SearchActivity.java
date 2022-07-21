package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchEvents searchEvents = new SearchEvents();
        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> {
            searchEvents.onPostExecute("https://app.ticketmaster.com/discovery/v2/events.json?apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb");
        });
    }

    private class SearchEvents extends AsyncTask<String, Integer, String> {

        // Get the JSON
        protected String doInBackground(String... args) {
            String result = "";
            try {
                URL url = new URL(args[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream response = connection.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8); // what is sz?
                StringBuilder strBuilder = new StringBuilder();
                String line;
                while ((line = read.readLine()) != null)
                    strBuilder.append(line + "\n");
                result = strBuilder.toString(); // The result from while loop.
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("EVENT_SEARCH", result);
            return result;
        }

        protected void onProgressUpdate(Integer progress) {

        }

        // Parse the JSON and make visible
        protected void onPostExecute(String result) {

        }
    }
}