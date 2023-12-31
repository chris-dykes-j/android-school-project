package com.cst2335.ticketmaster;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class extends BaseActivity and display wishlist
 */
public class WishList extends BaseActivity {
    ArrayList<Events> events = new ArrayList<>();
    WishListAdapter wishListAdapter;
    EventOpener helper;
    SQLiteDatabase db;
    Cursor cursor;
    WishFragment wishFm;
    Bundle bundle;
    ProgressBar progressBar;

    /**
     *  This method create initial view for wish list
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("BaseActivity", "Testing");
        super.onCreate(savedInstanceState);
        super.setLayout(R.layout.activity_wish_list);
        ListView listView = findViewById(R.id.myWishList);
        progressBar = (ProgressBar) findViewById(R.id.myList_progressBar);
        listView.setAdapter(wishListAdapter = new WishListAdapter());
        helper = new EventOpener(this);
        db = helper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + helper.TABLE_NAME + " WHERE type='W' AND isActive!='N' ORDER BY _id DESC;", null);

        int idIndex = cursor.getColumnIndex("_id");
        int nameIndex = cursor.getColumnIndex(helper.COL_NAME);
        int imgUrlIndex = cursor.getColumnIndex(helper.COL_IMG_URL);
        int cityIndex = cursor.getColumnIndex(helper.COL_LOCATION);
        int startDateIndex = cursor.getColumnIndex(helper.COL_DATE);
        int categoryIndex = cursor.getColumnIndex(helper.COL_CATEGORY);
        int i = 10;
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
            Log.i("progressbar",cursor.getPosition() * i+"");
            try {
                progressBar.setProgress(cursor.getPosition() * i);
            } catch (NumberFormatException e) {
                Log.i("Nullpoint", e.getMessage());
            }
        }

        progressBar.setProgress(100);

        listView.setOnItemClickListener((p, b, position, id) -> {
            Events clickedEvent = events.get(position);
            wishFm = new WishFragment();
            bundle = new Bundle();
            bundle.putString("mySubCity", clickedEvent.getCity());
            bundle.putString("mySubType", clickedEvent.getType());
            wishFm.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.myFrame, wishFm).commit();
        });
    }

    /**
     *  WishListAdapter for listview
     */
    class WishListAdapter extends BaseAdapter {
        View newView;
        TextView listName, listCity, listDate;
        ImageButton deleteButton, cartButton;

        /**
         * This method return event size
         * @return the size of events array
         */
        @Override
        public int getCount() {
            return events.size();
        }

        /**
         * This method return Item
         * @param position position of item
         * @return Event object
         */
        @Override
        public Object getItem(int position) {
            return events.get(position);
        }

        /**
         * This method return Item Id
         * @param position position of item
         * @return event id
         */
        @Override
        public long getItemId(int position) {
            return Long.parseLong(events.get(position).getId());
        }

        /**
         * This method return view
         * @param position position of item
         * @param convertView convertView
         * @param parent parent
         * @return View
         */
        @Override
        public View getView(int position, View convertView, ViewGroup  parent) {

            LayoutInflater inflater = getLayoutInflater();
            newView = inflater.inflate(R.layout.my_wish_list, parent, false);
            listName = newView.findViewById(R.id.myWishList_name);
            listCity = newView.findViewById(R.id.myWishList_city);
            listDate = newView.findViewById(R.id.myWishList_startDate);
            deleteButton = (ImageButton) newView.findViewById(R.id.deleteButton);
            cartButton = (ImageButton) newView.findViewById(R.id.cartButton);


            Events ticket = (Events) getItem(position);
            new DownloadImageTask((ImageView) newView.findViewById(R.id.myWishList_image))
                    .execute(ticket.getImgUrl());
            listName.setText(ticket.getName());
            listCity.setText(ticket.getCity());
            listDate.setText(ticket.getStartDate());

            deleteButton.setOnClickListener(v -> eventAlert(db, ticket, "", "N"));
            cartButton.setOnClickListener(v -> eventAlert(db, ticket, "C", "Y"));


            return newView;
        }
    }

    /**
     *  This method display AlertDialog
     * @param database database
     * @param event event
     * @param type type of event
     * @param isActive status of event
     */
    private void eventAlert(SQLiteDatabase database, Events event, String type, String isActive) {
        AlertDialog.Builder build = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.wishAlert))
                .setMessage(getString((type.equals("C") ? R.string.searchAlertCartMsg : R.string.wishAlertDeleteMsg)))
                .setNegativeButton(getString(R.string.alertNo), null)
                .setPositiveButton(getString(R.string.alertYes), (dialog, which) -> {
                    modifyEvent(database, event, type, isActive);

                });
        build.show();
    }

    /**
     *  This method update event status
     * @param database database
     * @param event event
     * @param type type of event
     * @param isActive status of event
     */
    private void modifyEvent(SQLiteDatabase database, Events event, String type, String isActive) {
        ContentValues args = new ContentValues();
        if (type.equals("C")) {
            args.put("type", type);
        } else if (isActive.equals("N")) {
            args.put("isActive", isActive);
        }

        db.update(helper.TABLE_NAME, args, String.format("%s = ?", "_id"),
                new String[]{event.getId()});
        cursor = db.rawQuery("SELECT * FROM " + helper.TABLE_NAME + " ORDER BY _id DESC;", null);
//        this.printCursor(cursor, db.getVersion());
        cursor.close();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    /**
     * This method is to print the cursor
     * @param c  cursor
     * @param version database version
     */
    public void printCursor(Cursor c, int version) {
        c.moveToFirst();
        // 1. The database version number, use db.getVersion() for the version number.
        Log.i("Database version number ", Integer.toString(version));
        // 2. The number of columns in the cursor.
        Log.i("Number of the columns ", Integer.toString(c.getColumnCount()));
        // 3. The name of the columns in the cursor.
        String[] colNames = c.getColumnNames();
        for (String name : colNames) {
            Log.i("name of the columns ", name);
        }

        // 4. The number of rows in the cursor
        Log.i("number of rows ", Integer.toString(c.getCount()));
        // 5. Print out each row of results in the cursor.
        Log.i("print out each row of results ", "");
        int idIdex = c.getColumnIndex(helper.COL_NAME);
        int typeIndex = c.getColumnIndex(helper.COL_TYPE);
        int isActiveIndex = c.getColumnIndex(helper.COL_ISACTIVE);
        Log.i("isActiveIndex", isActiveIndex + "");
        for (int i = 0; i < c.getCount(); i++) {
            Log.i("id ", Integer.toString(c.getInt(idIdex)));
            Log.i("Type ", c.getString(typeIndex) + "");
            Log.i("isActive ", c.getString(isActiveIndex) + "");
            c.moveToNext();
        }
    }


}







