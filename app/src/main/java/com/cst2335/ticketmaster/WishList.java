package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

public class WishList extends AppCompatActivity {
    // testtest
    ArrayList<Events> events = new ArrayList<>();
    WishListAdapter wishListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        String resultText;
        ListView listView = findViewById(R.id.myWishList);
        listView.setAdapter(wishListAdapter = new WishListAdapter());

        try {
            resultText = new WishTask().execute().get();
            Log.i("test", "GOOOD!!");
            listjsonParser(resultText, events);
            wishListAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
                tickets.add(new Events(name, type, id, url, imgUrl, startDate, status));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    class WishListAdapter extends BaseAdapter {

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
            View newView = inflater.inflate(R.layout.my_wish_list, parent, false);
            TextView textView = newView.findViewById(R.id.myWishList_name);
            Events ticket = (Events) getItem(position);
            new DownloadImageTask((ImageView) newView.findViewById(R.id.myWishList_image))
                    .execute(ticket.getImgUrl());
            textView.setText(ticket.getName() + "/" + ticket.getStatus() + "/" + ticket.getStartDate());
            return newView;
        }
    }
}







