package com.cst2335.ticketmaster;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;
import java.util.ArrayList;

public class StarListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BC_MyOpenHelper myOpener;
    SQLiteDatabase theDatabase;

    ArrayList<Star_Events> events = new ArrayList<>();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);



        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        tBar.setTitle("Star List");
        setSupportActionBar(tBar);


        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView starList = findViewById(R.id.starEventsList);
        starList.setAdapter(aListAdapter = new ListAdapter());

        myOpener = new BC_MyOpenHelper(this);
        theDatabase = myOpener.getWritableDatabase();

        //load from the database:
        Cursor results = theDatabase.rawQuery("Select * from " + myOpener.TABLE_NAME + ";", null);//no arguments to the query

        //Convert column names to indices:
        int idIndex = results.getColumnIndex(myOpener.COL_ID);
        int eNameIndex = results.getColumnIndex(myOpener.COL_EVENT_NAME);
        int eStatusIndex = results.getColumnIndex(myOpener.COL_EVENT_STATUS);
        int eStartDateIndex = results.getColumnIndex(myOpener.COL_START_DATE);
        int eCityIndex = results.getColumnIndex(myOpener.COL_EVENT_CITY);
        int eMinPriceIndex = results.getColumnIndex(myOpener.COL_MIN_PRICE);
        int eMaxPriceIndex = results.getColumnIndex(myOpener.COL_MAX_PRICE);
        int eImgUrlIndex = results.getColumnIndex(myOpener.COL_IMG_URL);

        //cursor is pointing to row -1
        while (results.moveToNext()) //returns false if no more data
        { //pointing to row 2
            eventID = results.getString(idIndex);
            eventName = results.getString(eNameIndex);
            eventStatus = results.getString(eStatusIndex);
            eventStartDate = results.getString(eStartDateIndex);
            eventMinPrice = results.getString(eMinPriceIndex);
            eventMaxPrice = results.getString(eMaxPriceIndex);
            eventCity = results.getString(eCityIndex);
            eventImgUrl = results.getString(eImgUrlIndex);

            //add to arrayList:
            events.add(new Star_Events(eventID, eventName, eventImgUrl, eventStartDate, eventStatus, eventCity, eventMinPrice,  eventMaxPrice));
        }
        /**
         * go to star list.
         */
        starList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Star_Events e = (Star_Events) aListAdapter.getItem(i);

                eventName = e.getName();
                eventStatus = e.getStatus();
                eventStartDate = e.getStartDate();
                eventCity = e.getCity();
                eventMinPrice = e.getMinPrice();
                eventMaxPrice = e.getMaxPrice();
                eventImgUrl = e.getImgUrl();
                eventID = e.getId();

                Bundle args = new Bundle();

                args.putString("eventName", eventName);
                args.putString("eventStatus", eventStatus);
                args.putString("eventStartDate", eventStartDate);
                args.putString("eventCity", eventCity);
                args.putString("eventImgUrl", eventImgUrl);
                args.putString("eventMinPrice", eventMinPrice);
                args.putString("eventMaxPrice", eventMaxPrice);
                args.putString("eventID", eventID);

                Intent goToDetail = new Intent(StarListActivity.this, BC_EventDetailActivity.class);
                goToDetail.putExtras(args);
                startActivity(goToDetail);

            }
        });

        starList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             * Response to delete one item from the star list.
             * @param parent parent view
             * @param view view
             * @param i position of an item of a list
             * @param id item id
             * @return on click event
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(StarListActivity.this);
                String alert_msg = getResources().getString(R.string.alert_message_del_item);

                builder.setTitle(R.string.dialog_title_starList)
                        .setMessage(alert_msg + " {" + events.get(i).getName() + "} ")
                        .setNegativeButton(R.string.alert_cancel, (dialog, click1) -> {
                        })
                        .setPositiveButton(R.string.alert_confirm, (dialog, click2) -> {

                            theDatabase.delete(BC_MyOpenHelper.TABLE_NAME, BC_MyOpenHelper.COL_ID + " = ?", new String[]{events.get(i).getId()});

                            events.remove(i);
                            aListAdapter.notifyDataSetChanged();

                            Toast.makeText(getApplicationContext(), getString(R.string.delItem_status), Toast.LENGTH_SHORT).show();

                        }).create().show();

                return false;
            }

        });


    }

    /**
     * Create a list adapter.
     */
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
            Star_Events e = (Star_Events) getItem(position);
            new DownloadImageTask((ImageView) newView.findViewById(R.id.event_image))
                    .execute(e.getImgUrl());

            textView.setText(e.getName() + "/" + e.getStatus() + "/" + e.getStartDate() + "/" + e.getCity());
            return newView;

        }
    }

    /**
     * An object class
     */
    public class Star_Events {
        private String name;
        private String id;
        private String imgUrl;
        private String startDate;
        private String status;
        private String city;
        private String minPrice;
        private String maxPrice;


        public Star_Events(String id, String name, String imgUrl, String startDate, String status, String city, String minPrice, String maxPrice) {
            this.name = name;
            this.id = id;
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

        public String getId() {
            return this.id;
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

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.help:

                AlertDialog.Builder builder = new AlertDialog.Builder( StarListActivity.this );
                String alert_msg = getResources().getString(R.string.alert_message);

                builder.setTitle(R.string.dialog_title)
                        .setMessage(alert_msg)
                        .setNegativeButton(R.string.alert_cancel, (dialog, click1)->{ })
                        .setPositiveButton(R.string.alert_confirm, (dialog, click2)->{ })
                        .create().show();
                break;

            case R.id.star:



        }

        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.navSearch:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.navWish:
                startActivity(new Intent(this, WishList.class));
                break;
            case R.id.navCart:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.navCategories:
                startActivity(new Intent(this, BrowseCateActivity.class));
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
}




