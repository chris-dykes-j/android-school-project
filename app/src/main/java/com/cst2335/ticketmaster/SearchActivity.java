package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private ArrayList<Event> eventList;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        eventList = new ArrayList<>();
        adapter = new EventAdapter();

        EventSearch searchEvents = new EventSearch();
        ListView searchResults = findViewById(R.id.searchEventList);
        searchResults.setAdapter(adapter);
        ImageButton searchButton = findViewById(R.id.searchButton);

        // Test
        try {
            String searchResult = searchEvents.execute().get();
            eventList = parseJson(searchResult);
            adapter.notifyDataSetChanged();
        } catch (ExecutionException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }

        searchButton.setOnClickListener(view -> {
            try {
                String searchResult = searchEvents.execute().get();
                eventList = parseJson(searchResult);
                adapter.notifyDataSetChanged();
            } catch (ExecutionException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        searchResults.setOnItemClickListener((list, view, pos, id) -> {
            Intent goToItem = new Intent(this, EventActivity.class);
            goToItem.putExtra("Event", eventList.get(pos));
            startActivity(goToItem);
        });
    }

    private ArrayList<Event> parseJson(String input) throws JSONException {
        ArrayList<Event> events = new ArrayList<>();
        if (!input.isEmpty()) {
            JSONObject jsonObject = new JSONObject(input).optJSONObject("_embedded");
            JSONArray jsonArray = jsonObject.optJSONArray("events");
            for (int i = 0; i < jsonArray.length(); i++) {
                Event event;
                JSONObject eventJson = jsonArray.optJSONObject(i);
                String eventName = eventJson.optString("name");
                String eventType = eventJson.optString("type");
                String eventId = eventJson.optString("id");
                String eventUrl = eventJson.optString("url");
                String eventImg = eventJson.optJSONArray("images")
                        .optJSONObject(0)
                        .optString("url");
                String eventDate = eventJson.optJSONObject("dates")
                        .optJSONObject("start")
                        .optString("localDate");
                String eventStatus = eventJson
                        .optJSONObject("dates")
                        .optJSONObject("status")
                        .optString("code");
                String eventCity = eventJson
                        .optJSONObject("_embedded")
                        .optJSONArray("venues")
                        .optJSONObject(0)
                        .optJSONObject("city")
                        .optString("name");

//                String eventGenre = eventJson.optJSONArray("classifications")
//                        .optJSONObject(0)
//                        .optJSONObject("segment")
//                        .optString("name");

                event = new Event(eventName, eventType, eventId, eventUrl, eventImg, eventDate, eventStatus, eventCity);
                // (name, type, id, url, imgUrl, startDate, status, city)
                events.add(event);
            }
        }
        return events;
    }

    private class EventSearch extends AsyncTask<String, String, String> {
        private String str, receive;

        // Get the JSON
        protected String doInBackground(String... args) {
            String strUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb";
            URL url;
            try {
                url = new URL(strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                if (connection.getResponseCode() == connection.HTTP_OK) {
                    InputStreamReader input = new InputStreamReader(connection.getInputStream());
                    BufferedReader read = new BufferedReader(input);
                    StringBuffer buffy = new StringBuffer();
                    while ((str = read.readLine()) != null)
                        buffy.append(str + "\n");
                    receive = buffy.toString();
                    Log.e("", receive);
                    read.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return receive;
        }

        protected void onProgressUpdate(String ... progress) {

        }

        // Parse the JSON and make visible
        protected void onPostExecute(String result) {
            // Log.e("", result);
        }
    }

    // Leaving this here so that the activities don't get too crowded.
    private class EventAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.search_list, parent, false);
            Event message = eventList.get(position);
            TextView textView = view.findViewById(R.id.searchEventTitle);
            textView.setText(message.getName());
            return view;
        }

        @Override
        public int getCount() {
            return eventList.size();
        }

        @Override
        public Object getItem(int pos) {
            return eventList.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return (long) pos;
        }
    }
}