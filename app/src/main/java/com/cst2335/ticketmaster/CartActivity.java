package com.cst2335.ticketmaster;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class CartActivity extends AppCompatActivity {

    EventOpener dbHelper;
    SQLiteDatabase db = null;
    CartTotalFragment cartTotalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Cart List
        ListView listView = (ListView) findViewById(R.id.cartList);

        dbHelper = new EventOpener(this);
        db = dbHelper.getWritableDatabase();

        String sql = "select * from Events WHERE Type = 'C';";
        Cursor c = db.rawQuery(sql, null);
        String[] strs = new String[]{dbHelper.COL_NAME};
        int[] ints = new int[]{R.id.cartText};

        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(listView.getContext(), R.layout.my_cart_list, c, strs, ints, 0);

        listView.setAdapter(adapter);
        TextView tv = (TextView) findViewById(R.id.testTotal);

        //delete




        //How to
        Button howBtn = (Button) findViewById(R.id.howto);
        howBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);

                builder.setTitle("How to use");
                builder.setMessage("It is a Cart page. If you want to check total price, touch Total button, and touch payment to process the payment");
                builder.setPositiveButton("Thanks", null);

                builder.create().show();

            }


        });



        //fragment connect
        cartTotalFragment = new CartTotalFragment();

        Button button = (Button) findViewById(R.id.total);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction().replace(R.id.cartTotalFragmentSpace, cartTotalFragment).commit();

                Cursor cursor = db.rawQuery("SELECT SUM(" + dbHelper.COL_PRICE + ") as Total FROM " + dbHelper.TABLE_NAME + " WHERE Type='C' ", null);

                if (cursor.moveToFirst()) {

                    int total = cursor.getInt(cursor.getColumnIndex("Total"));

                    Log.i("test", cursor.getColumnIndex("Total") + "");
                    Log.i("test", total + "");


                    tv.setText("" + total);
                }


            }


        });





    }

    public void onChangeFragment(int index) {
        if (index == 0) {

            getSupportFragmentManager().beginTransaction().replace(R.id.cartTotalFragmentSpace, cartTotalFragment).commit();
        }


    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.my_cart_list, null);
            }

            //ImageView imageView = (ImageView)v.findViewById(R.id.cartImg);
            TextView textView = (TextView)v.findViewById(R.id.cartText);
            textView.setText(items.get(position));

            final String text = items.get(position);

            ImageButton deleteCart = (ImageButton) findViewById(R.id.cartDelete);
            deleteCart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Home Button",Toast.LENGTH_LONG).show();// display the toast on home button click

                }
            });

            return v;
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