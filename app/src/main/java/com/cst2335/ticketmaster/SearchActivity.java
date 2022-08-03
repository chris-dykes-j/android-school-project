package com.cst2335.ticketmaster;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// Need Progress Bar (from Di)

// The top navigation layout should have the Activity’s title, author, and version number
// Add fragment for search results (make event activity a fragment?)
// Help menu item with alert dialog (instructions) use an about icon

// Shared preferences. How to implement? previous search results?
// Translate strings to french at the end.
// JavaDoc comments
// Style GUI at the end

public class SearchActivity extends BaseActivity {

    private static final String TAG = "SearchActivity";
    private ArrayList<Events> eventList;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setLayout(R.layout.activity_search);
        //setContentView(R.layout.activity_search);

        eventList = new ArrayList<>();
        adapter = new EventAdapter();

        EventSearch searchEvents = new EventSearch("https://app.ticketmaster.com/discovery/v2/events.json?apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb");
        ListView searchResults = findViewById(R.id.searchEventList);
        searchResults.setAdapter(adapter);
        ImageButton searchButton = findViewById(R.id.searchButton);
        TextView searchQuery = findViewById(R.id.searchInput);

        // Test
        try {
            searchEvents.execute().get();
            adapter.notifyDataSetChanged();
        } catch (ExecutionException | /* JSONException | */InterruptedException e) {
            e.printStackTrace();
        }

        searchButton.setOnClickListener(view -> {
            try {
                String keyWord = searchQuery.getText().toString();
                String searchResult = "";
                if (!keyWord.equals(""))
                    searchResult = new EventSearch("https://app.ticketmaster.com/discovery/v2/events.json?apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb&keyword=" + keyWord).execute().get();
                else {
                    searchResult = new EventSearch("https://app.ticketmaster.com/discovery/v2/events.json?apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb").execute().get();
                    Toast.makeText(this, R.string.emptySearch, Toast.LENGTH_SHORT).show();
                }
                eventList = parseJson(searchResult);
                adapter.notifyDataSetChanged();
            } catch (ExecutionException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        searchResults.setOnItemClickListener((list, view, pos, id) -> {
            Intent goToItem = new Intent(this, SearchEventActivity.class);
            goToItem.putExtra("Event", eventList.get(pos));
            startActivity(goToItem);
        });
    }

    private ArrayList<Events> parseJson(String input) throws JSONException {
        ArrayList<Events> events = new ArrayList<>();
        if (!input.isEmpty()) {
            JSONObject jsonObject = new JSONObject(input).optJSONObject("_embedded");
            JSONArray jsonArray = jsonObject.optJSONArray("events");
            for (int i = 0; i < jsonArray.length(); i++) {
                String eventName = ""; String eventType = ""; String eventId = ""; String eventUrl= "";
                String eventImg= ""; String eventDate= ""; String eventStatus= ""; String eventCity = "";
                double eventPrice = 0.0;
                JSONObject eventJson = jsonArray.optJSONObject(i);
                try {
                    eventName = eventJson.optString("name");
                    eventType = eventJson.optJSONArray("classifications") // <---- Updated to grab category (ie. sports)
                            .optJSONObject(0)
                            .optJSONObject("segment")
                            .optString("name");
                    eventId = eventJson.optString("id");
                    eventUrl = eventJson.optString("url");
                    eventImg = eventJson.optJSONArray("images")
                            .optJSONObject(0)
                            .optString("url");
                    eventDate = eventJson.optJSONObject("dates")
                            .optJSONObject("start")
                            .optString("localDate");
                    eventStatus = eventJson
                            .optJSONObject("dates")
                            .optJSONObject("status")
                            .optString("code");
                    eventCity = eventJson
                            .optJSONObject("_embedded")
                            .optJSONArray("venues")
                            .optJSONObject(0)
                            .optJSONObject("city")
                            .optString("name");
                    eventPrice = Double.parseDouble(eventJson
                            .optJSONArray("priceRanges")
                            .optJSONObject(0)
                            .optString("min"));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                events.add(Events.buildEvent()
                        .setName(eventName)
                        .setType(eventType)
                        .setId(eventId)
                        .setUrl(eventUrl)
                        .setImgUrl(eventImg)
                        .setStartDate(eventDate)
                        .setStatus(eventStatus)
                        .setCity(eventCity)
                        .setPrice(eventPrice)
                        .setTicketNum(1)
                        .setIsActive("Y")
                        .build());
            }
        }
        return events;
    }

    // I like inner classes.
    private class EventSearch extends AsyncTask<String, String, String> {
        private final String strUrl;
        private String receive;

            EventSearch(String strUrl) {
                this.strUrl = strUrl;
            }

        // Get the JSON
        protected String doInBackground(String... args) {
            URL url;
            try {
                url = new URL(strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                if (connection.getResponseCode() == connection.HTTP_OK) {
                    InputStreamReader input = new InputStreamReader(connection.getInputStream());
                    BufferedReader read = new BufferedReader(input);
                    StringBuffer buffy = new StringBuffer();
                    String str;
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
            try {
                eventList = parseJson(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Leaving this here so that the activities don't get too crowded.
    private class EventAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.search_list, parent, false);
            Events event = eventList.get(position);
            TextView title = view.findViewById(R.id.searchEventTitle);
            new DownloadImageTask(view.findViewById(R.id.searchEventImg))
                    .execute(event.getImgUrl());
            title.setText(event.getName());
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