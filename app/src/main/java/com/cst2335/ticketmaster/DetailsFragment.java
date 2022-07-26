package com.cst2335.ticketmaster;

import static java.lang.Long.parseLong;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class DetailsFragment extends Fragment {

    ArrayList<Events> events = new ArrayList<>();
    ListAdapter aListAdapter;

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

            BrowseCateTask myTask = new BrowseCateTask();

            resultText = new BrowseCateTask().execute(b.getString("countryCode")).get();
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

                aListAdapter.notifyDataSetChanged();
            }
        });

//        cityEventsList.setOnItemLongClickListener((p, b, pos, id) -> {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setTitle("Select an option below")
//                    .setMessage("What do you want to do")
//                    .setPositiveButton("Save it", (click, arg) -> {
//
//                        aListAdapter.notifyDataSetChanged();
//                    })
//                    .setNeutralButton("Like it", (click, arg) -> {
//
//                        aListAdapter.notifyDataSetChanged();
//                    })
//                    .setNegativeButton("CLOSE", (click, arg) -> { })
//                    .create().show();
//            return true;
//        });


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
        public long getItemId(int position) {
            return Long.parseLong(events.get(position).getId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView textView = newView.findViewById(R.id.event_info);
            Events ticket = (Events) getItem(position);
            new DownloadImageTask((ImageView) newView.findViewById(R.id.event_image))
                    .execute(ticket.getImgUrl());
            textView.setText(ticket.getName() + "/" + ticket.getStatus() + "/" + ticket.getStartDate() + "/" + ticket.getCity());
            return newView;
        }
    }

    public ArrayList listjsonParser(String jsonString, ArrayList<Events> tickets) {

        String name = null;
        String type = null;
        String id = null;
        String url = null;
        String imgUrl = null;
        JSONArray jsonArr = null;
        String startDate = null;
        String status = null;
        String city = null;

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

                tickets.add(new Events(name, type, id, url, imgUrl, startDate, status, city));


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tickets;
    }

}
