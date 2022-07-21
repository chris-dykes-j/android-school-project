package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<Event> eventList;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ListView listView = findViewById(R.id.searchEventList);
        eventList = new ArrayList<>();
        adapter =  new EventAdapter(eventList, this);
        listView.setAdapter(adapter);

        EventSearch searchEvents = new EventSearch();
        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> {
            try {
                searchEvents.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private class EventSearch extends AsyncTask<String, String, String> {
        private String str, receive;

        // Get the JSON
        protected String doInBackground(String... args) {
            String strUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb";
            URL url = null;
            try {
                url = new URL(strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                if (connection.getResponseCode() == connection.HTTP_OK) {
                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    BufferedReader read = new BufferedReader(isr);
                    StringBuffer buffy = new StringBuffer();
                    while ((str = read.readLine()) != null)
                        buffy.append(str + "\n");
                    receive = buffy.toString();
                    Log.e("", receive);
                    read.close();
                }
                if (!receive.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(receive).getJSONObject("_embedded");
                    JSONArray jsonArray = jsonObject.getJSONArray("events");
                    // eventList.clear(); // Remove any old data, just in case... may not be needed.
                    // Log.e("", Integer.toString(jsonArray.length())); Bug fixing, all good now.
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject eventJson = jsonArray.getJSONObject(i);
                        String eventName = eventJson.getString("name");
                        String eventUrl = eventJson.getString("url");
                        String eventImg = eventJson.getJSONArray("images")
                                .getJSONObject(0)
                                .getString("url");
                        String eventGenre = eventJson.getJSONArray("classifications")
                                .getJSONObject(0)
                                .getJSONObject("segment")
                                .getString("name");
                        Event event = new Event(eventName, eventUrl, eventImg, eventGenre);
                        eventList.add(event);
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Result";
        }

        protected void onProgressUpdate(String ... progress) {
            // add to event list
            // Log.e("SearchActivity")
        }

        // Parse the JSON and make visible
        protected void onPostExecute(String result) {
            Log.e("", result);
        }
    }
}