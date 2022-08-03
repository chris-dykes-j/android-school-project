package com.cst2335.ticketmaster;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class BC_DetailsFragment extends Fragment {

    ArrayList<BC_Events> events = new ArrayList<>();
    ListAdapter aListAdapter;

    String eventName;
    String eventStatus;
    String eventStartDate;
    String eventMinPrice;
    String eventMaxPrice;
    String eventImgUrl;
    String eventCity;
    String eventID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Get back arguments
            if (getArguments() != null) {
//                position = getArguments().getInt("position", 0);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, parent, false);

        String resultText;

        Bundle b = getArguments();
        // update view

        ListView cityEventsList = view.findViewById(R.id.cityEvents);
        cityEventsList.setAdapter(aListAdapter = new ListAdapter());

        try {

            ListTask myTask = new ListTask();

            resultText = myTask.execute(b.getString("countryCode")).get();

            Log.i("test", "GOOOD!!");
            listjsonParser(resultText, events);
            aListAdapter.notifyDataSetChanged();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        cityEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BC_Events ticket = (BC_Events) aListAdapter.getItem(i);

                eventName = ticket.getName();
                eventStatus = ticket.getStatus();
                eventStartDate = ticket.getStartDate();
                eventCity = ticket.getCity();
                eventMinPrice = ticket.getMinPrice();
                eventMaxPrice = ticket.getMaxPrice();
                eventImgUrl = ticket.getImgUrl();
                eventID = ticket.getId();

                Bundle args = new Bundle();

                args.putString("eventName", eventName);
                args.putString("eventStatus", eventStatus);
                args.putString("eventStartDate", eventStartDate);
                args.putString("eventCity", eventCity);
                args.putString("eventImgUrl", eventImgUrl);
                args.putString("eventMinPrice", eventMinPrice);
                args.putString("eventMaxPrice", eventMaxPrice);
                args.putString("eventID", eventID);

                Intent goToDetail = new Intent(getActivity(), BC_EventDetailActivity.class);

                goToDetail.putExtras(args);

                startActivity(goToDetail);

            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int position) {
            return events.get(position);
        }

        @Override
        public long getItemId(int position) { return (long) position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView textView = newView.findViewById(R.id.event_info);
            BC_Events ticket = (BC_Events) getItem(position);
            new DownloadImageTask((ImageView) newView.findViewById(R.id.event_image))
                    .execute(ticket.getImgUrl());

            textView.setText(ticket.getName() + "/" + ticket.getStatus() + "/" + ticket.getStartDate() + "/" + ticket.getCity());
            return newView;
        }
    }

    public ArrayList listjsonParser(String jsonString, ArrayList<BC_Events> tickets) {

        String name = null;
        String type = null;
        String id = null;
        String url = null;
        String imgUrl = null;
        JSONArray jsonArr = null;
        String startDate = null;
        String status = null;
        String city = null;
        String minPrice = null;
        String maxPrice = null;

        try {
            JSONObject jobject = new JSONObject(jsonString);
            JSONArray jsonArray = jobject.optJSONObject("_embedded").optJSONArray("events");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.optJSONObject(i);

                name = jObject.optString("name");
                type = jObject.optString("type");
                id = jObject.optString("id");
                url = jObject.optString("url");

                imgUrl = jObject.optJSONArray("images").optJSONObject(0).optString("url");
                Log.i("dates : ", jObject.optJSONObject("dates").optJSONObject("start").optString("localDate") + "");

                startDate = jObject.optJSONObject("dates").optJSONObject("start").optString("localDate");
                status = jObject.optJSONObject("dates").optJSONObject("status").optString("code");

                city = jObject.optJSONObject("_embedded").optJSONArray("venues").optJSONObject(0).optJSONObject("city").optString("name");

                try {

                    minPrice = String.valueOf(jObject.optJSONArray("priceRanges").optJSONObject(0).optDouble("min"));

                    maxPrice = String.valueOf(jObject.optJSONArray("priceRanges").optJSONObject(0).optDouble("max"));

                    Log.i("test", "GOOOD!!");

                } catch (NullPointerException e) {

                    e.printStackTrace();

                }


                tickets.add(new BC_Events(name, type, id, url, imgUrl, startDate, status, city, minPrice, maxPrice));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    class ListTask extends AsyncTask<String, Integer, String> {
        private String str, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {

            String strUrl = strings[0];

            if (strUrl == "CA") {
                strUrl = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=CA&size=5&apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb";
            }
            else if (strUrl == "US") {
                strUrl = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=AU&size=5&apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb";
            }
            else {
                strUrl = "https://app.ticketmaster.com/discovery/v2/events.json?classificationName=music&dmaId=324&apikey=LJclKZ6rnChg9m4ZwZ3BfUlfOHD69Ekb";
            }


            // url should be passed as parameters
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
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

        @Override
        protected void onProgressUpdate(Integer...values) {

            super.onProgressUpdate(values);

        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public class BC_Events {
        private String name;
        private String type;
        private String id;
        private String url;
        private String imgUrl;
        private String startDate;
        private String status;
        private String city;
        private String minPrice;
        private String maxPrice;


        public BC_Events(String name, String type, String id, String url, String imgUrl, String startDate, String status, String city, String minPrice, String maxPrice) {
            this.name = name;
            this.type = type;
            this.id = id;
            this.url = url;
            this.imgUrl = imgUrl;
            this.startDate = startDate;
            this.status = status;
            this.city = city;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;

        }

        public String getName() {
            return this.name;
        }

        public String getType() {
            return this.type;
        }

        public String getId() {
            return this.id;
        }

        public String getUrl() {
            return this.url;
        }

        public String getImgUrl() {
            return this.imgUrl;
        }

        public String getStartDate() {
            return this.startDate;
        }

        public String getStatus() {
            return this.status;
        }

        public String getCity() {
            return this.city;
        }

        public String getMinPrice() { return this.minPrice; }
        public String getMaxPrice() { return this.maxPrice; }



    }


}


