package com.cst2335.ticketmaster;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class WishFragment extends Fragment {
    ArrayList<Events> events = new ArrayList<>();
    WishSubListAdapter wishSubListAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MYSUBCITY = "mySubCity";
    private static final String MYSUBTYPE = "mySubType";

    // TODO: Rename and change types of parameters
    private String mySubCity;
    private String mySubType;
    private ImageView imageView;
    private TextView textView;
    private View newView;

    public WishFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mySubCity = getArguments().getString(MYSUBCITY);
            mySubType = getArguments().getString(MYSUBTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newView = inflater.inflate(R.layout.fragment_my_wish_fragment, container, false);

        return newView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView listView = newView.findViewById(R.id.mySubList);
        Log.i("chk", listView+"");
        listView.setAdapter(wishSubListAdapter = new WishSubListAdapter());
        listView.setRotation(90);

        Log.i("mySubCity : ", mySubCity);
        Log.i("mySubType : ", mySubType);
        try {
            String resultText = new WishTask().execute("keyword=" + mySubType).get();
            Log.i("test", "GOOOD!!");

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
                JSONObject jobject = new JSONObject(resultText);
                JSONArray jsonArray = jobject.optJSONObject("_embedded").optJSONArray("events");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.optJSONObject(i);

                    name = jObject.optString("name");
                    imgUrl = jObject.optJSONArray("images").optJSONObject(0).optString("url");
                    startDate = jObject.optJSONObject("dates").optJSONObject("start").optString("localDate");
                    status = jObject.optJSONObject("dates").optJSONObject("status").optString("code");
                    city = jObject.optJSONObject("_embedded").optJSONArray("venues").optJSONObject(0).optJSONObject("city").optString("name");
                    events.add(new Events(name, type, id, url, imgUrl, startDate, status, city));
                }
                wishSubListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        super.onViewCreated(view, savedInstanceState);
    }

    private class WishSubListAdapter extends BaseAdapter {

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
            View newView = inflater.inflate(R.layout.my_sub_list, parent, false);
            TextView textView = newView.findViewById(R.id.mySubList_name);
            Events ticket = (Events) getItem(position);
            textView.setRotation(270);
            new DownloadImageTask((ImageView) newView.findViewById(R.id.mySubList_image))
                    .execute(ticket.getImgUrl());
            textView.setText(ticket.getName() + "/" + ticket.getCity() + "/" + ticket.getStartDate());
            Log.i("testtesting",ticket.getName() + "/" + ticket.getCity() + "/" + ticket.getStartDate());

            return newView;
        }
    }
}