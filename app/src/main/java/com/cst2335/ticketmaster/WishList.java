package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WishList extends AppCompatActivity {
    // testtest
    ArrayList<Events> events = new ArrayList<>();
    WishListAdapter wishListAdapter;
    EventOpener helper;
    SQLiteDatabase db;
    Cursor cursor;
    WishFragment wishFm;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        String resultText;
        ListView listView = findViewById(R.id.myWishList);
        listView.setAdapter(wishListAdapter = new WishListAdapter());
        Button btn1 = findViewById(R.id.wish_btn1);

        helper = new EventOpener(this);
        db = helper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + helper.TABLE_NAME + ";", null);

        int idIndex = cursor.getColumnIndex("_id");
        int nameIndex = cursor.getColumnIndex(helper.COL_NAME);
        int imgUrlIndex = cursor.getColumnIndex(helper.COL_IMG_URL);
        int cityIndex = cursor.getColumnIndex(helper.COL_LOCATION);
        int startDateIndex = cursor.getColumnIndex(helper.COL_DATE);

        // cursor is pointing to row - 1
        // keep looping until no more data
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(idIndex);
            String name = cursor.getString(nameIndex);
            String imgUrl = cursor.getString(imgUrlIndex);
            String city = cursor.getString(cityIndex);
            String startDate = cursor.getString(startDateIndex);

            events.add(new Events(name, "", id + "", "", imgUrl, startDate, "", city));

        }
        listView.setOnItemClickListener((p, b, position, id) -> {
            Events clickedEvent = events.get(position);
            wishFm = new WishFragment();
            bundle = new Bundle();
            bundle.putString("mySubName",clickedEvent.getName());
            bundle.putString("mySubImage",clickedEvent.getImgUrl());
            wishFm.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.myFrame, wishFm).commit();
        });


//        Log.i("isPhone","false");


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues newRow = new ContentValues();
                for (int i = 0; i < events.size(); i++) {

                    newRow.put(helper.COL_NAME, events.get(i).getName());
                    newRow.put(helper.COL_TYPE, "F");
                    newRow.put(helper.COL_URL, events.get(i).getUrl());
                    newRow.put(helper.COL_IMG_URL, events.get(i).getImgUrl());
                    newRow.put(helper.COL_DATE, events.get(i).getStartDate());
                    newRow.put(helper.COL_STATUS, events.get(i).getStatus());
                    newRow.put(helper.COL_LOCATION, events.get(i).getCity());
                    newRow.put(helper.COL_CATEGORY, "COL_CATEGORY");
                    newRow.put(helper.COL_PRICE, "COL_PRICE");
                    newRow.put(helper.COL_DETAILS, "COL_DETAILS");
//                    long id = db.insert(helper.TABLE_NAME,null,newRow);
//                    Log.i("id",id+"");
                }
            }
        });

//        try {
//            resultText = new WishTask().execute().get();
//            Log.i("test", "GOOOD!!");
//            listjsonParser(resultText, events);
//            wishListAdapter.notifyDataSetChanged();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
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
            textView.setText(ticket.getName() + "/" + ticket.getCity() + "/" + ticket.getStartDate());
            return newView;
        }
    }
}







