package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {

    EventOpener dbHelper;
    SQLiteDatabase db = null;
    CartTotalFragment cartTotalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ListView listView = (ListView) findViewById(R.id.cartList);

        dbHelper = new EventOpener( this);
        db = dbHelper.getWritableDatabase();

        String sql = "select * from Events;";
        Cursor c = db.rawQuery(sql, null);
        String[] strs = new String[]{dbHelper.COL_NAME,dbHelper.COL_TYPE, dbHelper.COL_DATE, dbHelper.COL_PRICE };
        int[] ints = new int[]{R.id.cartText};

        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(listView.getContext(), R.layout.my_cart_list, c, strs, ints, 0);

        listView.setAdapter(adapter);

         cartTotalFragment = new CartTotalFragment();

         Button button = (Button) findViewById(R.id.total);
         button.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View view){
                 getSupportFragmentManager().beginTransaction().replace(R.id.cartTotalFragmentSpace, cartTotalFragment).commit();
             }
         });

    }
    public void onChangeFragment(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.cartTotalFragmentSpace, cartTotalFragment).commit();
        }
    }
}

/**package com.cst2335.ticketmaster;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CartActivity extends AppCompatActivity  {
    ArrayList<Events> events = new ArrayList<>();
    CartActivity.CartListAdapter cartListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
            String resultText;
            ListView listView = findViewById(R.id.cartList);
            listView.setAdapter(cartListAdapter = new CartActivity.CartListAdapter());

            try {
                resultText = new WishTask().execute().get();
                Log.i("Is is working?", "Yes");
                listjsonParser(resultText, events);
                cartListAdapter.notifyDataSetChanged();
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

        class CartListAdapter extends BaseAdapter {

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
                View newView = inflater.inflate(R.layout.my_cart_list, parent, false);
                TextView textView = newView.findViewById(R.id.cartText);
                Events ticket = (Events) getItem(position);
                new DownloadImageTask((ImageView) newView.findViewById(R.id.cartImg))
                        .execute(ticket.getImgUrl());
                textView.setText(ticket.getName() + "/" + ticket.getStatus() + "/" + ticket.getStartDate());
                return newView;
            }


        }
    }
*/