package com.cst2335.ticketmaster;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;

public class BC_EventDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BC_MyOpenHelper myOpener;
    SQLiteDatabase theDatabase;

    ProgressBar pb;
    public final static String PREFERENCES_FILE = "MyData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        tBar.setTitle("Event Detail");
        setSupportActionBar(tBar);


        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //

        String eName;
        String eStatus;
        String eStartDate;
        String eCity;
        String eMinPrice;
        String eMaxPrice;
        String eImgUrl;


        myOpener = new BC_MyOpenHelper(this);
        theDatabase = myOpener.getWritableDatabase();

        pb = (ProgressBar) findViewById(R.id.progressBar0);

        Bundle b = getIntent().getExtras();

        TextView eventName = findViewById(R.id.event_name);
        TextView eventStatus = findViewById(R.id.event_status);
        TextView eventStartDate = findViewById(R.id.event_startDate);
        TextView eventCity = findViewById(R.id.event_city);
        TextView eventMinPrice = findViewById(R.id.event_minPrice);

        eName = b.getString("eventName");
        eStatus = b.getString("eventStatus");
        eMinPrice = b.getString("eventMinPrice");
        eMaxPrice = b.getString("eventMaxPrice");
        eCity = b.getString("eventCity");
        eStartDate = b.getString("eventStartDate");
        eImgUrl = b.getString("eventImgUrl");


        eventName.setText(eName);
        eventStatus.setText(eStatus);
        eventStartDate.setText(eStartDate);
        eventMinPrice.setText("Ticket Price: " + eMinPrice + " - " + eMaxPrice);
        eventCity.setText("City: " + eCity);

        new DetailImageTask((ImageView) findViewById(R.id.event_image))
                .execute(eImgUrl);

        Button btn = findViewById(R.id.backButton);
        btn.setOnClickListener(click->{
            super.onBackPressed();
        });

        Button btn_buy = findViewById(R.id.buyButton);
        Button btn_addStar = findViewById(R.id.addStarBTN);

        EditText ticketQuantity = findViewById(R.id.ticket_quantity);

        SharedPreferences prefs = getSharedPreferences(BC_EventDetailActivity.PREFERENCES_FILE, MODE_PRIVATE);
        String previous = prefs.getString("ticketQuantity", "");
        ticketQuantity.setText(previous);

        btn_addStar.setOnClickListener(click-> {

            //insert into database:
            ContentValues newRow = new ContentValues();// like intent or Bundle

            newRow.put(BC_MyOpenHelper.COL_EVENT_NAME, eName);
            newRow.put(BC_MyOpenHelper.COL_EVENT_STATUS, eStatus);
            newRow.put(BC_MyOpenHelper.COL_START_DATE, eStartDate);
            newRow.put(BC_MyOpenHelper.COL_EVENT_CITY, eCity);
            newRow.put(BC_MyOpenHelper.COL_MIN_PRICE, eMinPrice);
            newRow.put(BC_MyOpenHelper.COL_MAX_PRICE, eMaxPrice);
            newRow.put(BC_MyOpenHelper.COL_IMG_URL, eImgUrl);

            theDatabase.insert(BC_MyOpenHelper.TABLE_NAME, null, newRow);

            Toast.makeText(getApplicationContext(), getString(R.string.addItem_status), Toast.LENGTH_SHORT).show();

        });


        btn_buy.setOnClickListener(click->{

            Snackbar mySnackBar = Snackbar.make(btn_buy, "Confirm to purchase " + ticketQuantity.getText() + " ticket(s)?", Snackbar.LENGTH_LONG);

            mySnackBar.setAction("OK", new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = getSharedPreferences(BC_EventDetailActivity.PREFERENCES_FILE, MODE_PRIVATE);
                    SharedPreferences.Editor writer = prefs.edit();
                    writer.putString("ticketQuantity", ticketQuantity.getText().toString());
                    writer.apply();
                }
            });
            mySnackBar.show();


        });


    }

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

                AlertDialog.Builder builder = new AlertDialog.Builder( BC_EventDetailActivity.this );
                String alert_msg = getResources().getString(R.string.alert_message);

                builder.setTitle(R.string.dialog_title)
                        .setMessage(alert_msg)
                        .setNegativeButton(R.string.alert_cancel, (dialog, click1)->{ })
                        .setPositiveButton(R.string.alert_confirm, (dialog, click2)->{ })
                        .create().show();
                break;

            case R.id.star:

                Intent goToStar = new Intent(BC_EventDetailActivity.this, StarListActivity.class);
                startActivity(goToStar);

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
            case R.id.navHome:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.navAbout:
                aboutAlert();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }


    public class DetailImageTask extends AsyncTask<String, Integer, Bitmap> {
        ImageView bmImage;

        public DetailImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap detail_img = null;

            for (int i = 0; i < 31; i++) {

                publishProgress(i);

                try{
                    Thread.sleep(5);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                detail_img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return detail_img;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pb.setMax(30);

        }


        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);

            super.onPostExecute(result);

            pb.setVisibility(View.GONE);

        }

        @Override
        protected void onProgressUpdate(Integer...values) {

            super.onProgressUpdate(values);
            pb.setProgress(values[0]);

        }
    }

    private void aboutAlert() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.dialog_title)
                .setMessage(R.string.helpMessage)
                .setNegativeButton(R.string.alert_cancel, (dialog, click1)->{ })
                .setPositiveButton(R.string.alert_confirm, (dialog, click2)->{ })
                .create().show();
    }




}