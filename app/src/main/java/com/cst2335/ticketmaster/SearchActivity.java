package com.cst2335.ticketmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Activity to search for various Ticket Master Events.
 */
public class SearchActivity extends BaseActivity {

    private static final String TAG = "SearchActivity";
    private ArrayList<Events> eventList;
    private EventAdapter adapter;
    private final static String PREVIOUS_SEARCH = "Search Data";

    /**
     * Creates the SearchActivity.
     * @param savedInstanceState Bundle needed for onCreate, idk what this is.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setLayout(R.layout.activity_search);

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
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        searchButton.setOnClickListener(view -> {
            try {
                String keyWord = searchQuery.getText().toString();
                String searchResult;
                if (!keyWord.equals(""))
                    searchResult = new EventSearch("https://app.ticketmaster.com/discovery/v2/events.json?apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb&keyword=" + keyWord).execute().get();
                else {
                    searchResult = new EventSearch("https://app.ticketmaster.com/discovery/v2/events.json?apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb").execute().get();
                    Toast.makeText(this, R.string.emptySearch, Toast.LENGTH_SHORT).show();
                }
                eventList = parseJson(searchResult);
                adapter.notifyDataSetChanged();

                // More shared prefs
                SharedPreferences preferences = getSharedPreferences(SearchActivity.PREVIOUS_SEARCH, MODE_PRIVATE);
                SharedPreferences.Editor writer = preferences.edit();
                writer.putString("searchQuery", searchQuery.getText().toString()).apply();

            } catch (ExecutionException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        searchResults.setOnItemClickListener((list, view, pos, id) -> {
            Intent goToItem = new Intent(this, SearchEventActivity.class);
            goToItem.putExtra("Event", eventList.get(pos));
            startActivity(goToItem);
        });

        // Shared prefs
        SharedPreferences prefs = getSharedPreferences(SearchActivity.PREVIOUS_SEARCH, MODE_PRIVATE);
        String previous = prefs.getString("searchQuery", "");
        searchQuery.setText(previous);
    }

    /**
     * Parses the incoming JSON from the ticketmaster api.
     * @param input Result of the api fetch.
     * @return ArrayList of events
     * @throws JSONException Just in case JSON parsing goes wrong.
     */
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

    /**
     * The AsyncTask for searching events. I like inner classes
     */
    private class EventSearch extends AsyncTask<String, Integer, String> {
        private final String strUrl;
        private String receive;
        ProgressBar progress = findViewById(R.id.progressBar);

        /**
         * Constructor for the EventSearch Async task.
         * @param strUrl The ticket master api url.
         */
        EventSearch(String strUrl) {
            this.strUrl = strUrl;
        }

        /**
         * Before execution...
         */
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        /**
         * Gets the JSON, as a String
         * @param args String arguments
         * @return Result JSON as a String.
         */
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

        /**
         * Updates the progress bar. Not being made use of.
         * @param values Values used to update progress bar.
         */
        protected void onProgressUpdate(Integer ... values) { }

        /**
         * Parses the JSON and make's it usable.
         * @param result Result of api fetch.
         */
        protected void onPostExecute(String result) {
            try {
                eventList = parseJson(result);
                Thread.sleep(1000); // Just to show the progress bar. The api is too damn fast.
                progress.setVisibility(View.GONE);
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adapter for search activity. Deals with the list of events found.
     */
    private class EventAdapter extends BaseAdapter {

        /**
         * Gets view for each event.
         * @param position Position of the event in the list.
         * @param convertView The event view I think??
         * @param parent Group of views.
         * @return The view for the event.
         */
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

        /**
         * Gets size of the event list.
         * @return integer value of list size.
         */
        @Override
        public int getCount() {
            return eventList.size();
        }

        /**
         * Gets an item from the event given a position value.
         * @param pos Position in list of the event
         * @return Event item.
         */
        @Override
        public Object getItem(int pos) {
            return eventList.get(pos);
        }

        /**
         * Gets the item's id.
         * @param pos position of item.
         * @return Event id.
         */
        @Override
        public long getItemId(int pos) {
            return (long) pos;
        }
    }
}