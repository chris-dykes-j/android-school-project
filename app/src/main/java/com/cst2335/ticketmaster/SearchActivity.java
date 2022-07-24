package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

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
        ImageButton searchButton = findViewById(R.id.searchButton);
        ListView searchResults = findViewById(R.id.searchEventList);
        searchResults.setAdapter(adapter);

        searchButton.setOnClickListener(view -> {
            try {
                String searchResult = searchEvents.execute().get();
                eventList = parseJson(searchResult);
                Log.e("TEST", eventList.toString());
                adapter.notifyDataSetChanged();
            } catch (ExecutionException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private ArrayList<Event> parseJson(String input) throws JSONException {
        ArrayList<Event> events = new ArrayList<>();
        if (!input.isEmpty()) {
            JSONObject jsonObject = new JSONObject(input).getJSONObject("_embedded");
            JSONArray jsonArray = jsonObject.getJSONArray("events");
            // Log.e("", Integer.toString(jsonArray.length())); Bug fixing, all good now.
            for (int i = 0; i < jsonArray.length(); i++) {
                Event event;
                JSONObject eventJson = jsonArray.getJSONObject(i);
                String eventName = eventJson.getString("name");
                String eventUrl = eventJson.getString("url");
                String eventGenre = eventJson.getJSONArray("classifications")
                        .getJSONObject(0)
                        .getJSONObject("segment")
                        .getString("name");
                event = new Event(eventName, eventUrl, eventGenre);
                events.add(event);
            }
        }
        return events;
    }

    private class EventAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return eventList.size();
        }

        @Override
        public Object getItem(int position) {
            return eventList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.event_item, parent, false);
            TextView textView = view.findViewById(R.id.searchEventTitle);
            Event ticket = (Event) getItem(position);
            textView.setText(ticket.getName());
            return view;
        }
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
}