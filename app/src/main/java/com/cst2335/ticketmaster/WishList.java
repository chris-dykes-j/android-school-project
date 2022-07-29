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
        ListView listView = findViewById(R.id.myWishList);
        listView.setAdapter(wishListAdapter = new WishListAdapter());
        helper = new EventOpener(this);
        db = helper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + helper.TABLE_NAME + " ORDER BY _id DESC;", null);

        int idIndex = cursor.getColumnIndex("_id");
        int nameIndex = cursor.getColumnIndex(helper.COL_NAME);
        int imgUrlIndex = cursor.getColumnIndex(helper.COL_IMG_URL);
        int cityIndex = cursor.getColumnIndex(helper.COL_LOCATION);
        int startDateIndex = cursor.getColumnIndex(helper.COL_DATE);
        int categoryIndex = cursor.getColumnIndex(helper.COL_CATEGORY);


        // cursor is pointing to row - 1
        // keep looping until no more data
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(idIndex);
            String name = cursor.getString(nameIndex);
            String imgUrl = cursor.getString(imgUrlIndex);
            String city = cursor.getString(cityIndex);
            String startDate = cursor.getString(startDateIndex);
            String category = cursor.getString(categoryIndex);

            events.add(new Events(name, category, id + "", "", imgUrl, startDate, "", city));

        }
        listView.setOnItemClickListener((p, b, position, id) -> {
            Events clickedEvent = events.get(position);
            wishFm = new WishFragment();
            bundle = new Bundle();
            bundle.putString("mySubCity",clickedEvent.getCity());
            bundle.putString("mySubType",clickedEvent.getType());
            wishFm.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.myFrame, wishFm).commit();
        });


//        Log.i("isPhone","false");


//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContentValues newRow = new ContentValues();
//                for (int i = 0; i < events.size(); i++) {
//
//                    newRow.put(helper.COL_NAME, events.get(i).getName());
//                    newRow.put(helper.COL_TYPE, "F");
//                    newRow.put(helper.COL_URL, events.get(i).getUrl());
//                    newRow.put(helper.COL_IMG_URL, events.get(i).getImgUrl());
//                    newRow.put(helper.COL_DATE, events.get(i).getStartDate());
//                    newRow.put(helper.COL_STATUS, events.get(i).getStatus());
//                    newRow.put(helper.COL_LOCATION, events.get(i).getCity());
//                    newRow.put(helper.COL_CATEGORY, "COL_CATEGORY");
//                    newRow.put(helper.COL_PRICE, "COL_PRICE");
//                    newRow.put(helper.COL_DETAILS, "COL_DETAILS");
////                    long id = db.insert(helper.TABLE_NAME,null,newRow);
////                    Log.i("id",id+"");
//                }
//            }
//        });


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







