package com.cst2335.ticketmaster;

import androidx.appcompat.app.AlertDialog;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Individual Event Activity that displays a given event after a search.
 */
public class SearchEventActivity extends BaseActivity {

    private static final String TAG = "SearchEventActivity";
    private static final String TABLE_NAME = "Events";

    /**
     * Creates the SearchEventActivity.
     * @param savedInstanceState Bundle needed for onCreate, idk what this is.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_event);

        EventOpener helper = new EventOpener(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        Events event = (Events) getIntent().getSerializableExtra("Event");
//        Log.e(TAG, event.toString());
        TextView title = findViewById(R.id.eventTitle);
        TextView date = findViewById(R.id.searchEventDate);
        TextView price = findViewById(R.id.searchEventPrice);

        title.setText(event.getName());
        date.setText(event.getStartDate()); // Should be description.
        String eventPrice;
        eventPrice = (Double.compare(event.getPrice(), 0.0) == 0) ? getString(R.string.eventFree) :
                NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(event.getPrice());
        price.setText(eventPrice);
        new DownloadImageTask(findViewById(R.id.eventImg))
                .execute(event.getImgUrl());

        ImageButton wish = findViewById(R.id.wishEventButton);
        ImageButton cart = findViewById(R.id.cartEventButton);

        wish.setOnClickListener(v -> eventAlert(database, event, "W"));
        cart.setOnClickListener(v -> eventAlert(database, event, "C"));
    }

    /**
     * Adds Event to the events table.
     * @param database Database with table.
     * @param event Event to be added.
     * @param type Type of event being added.
     */
    private void addEvent(SQLiteDatabase database, Events event, String type) {
        ContentValues cv = new ContentValues();
        cv.put(EventOpener.COL_NAME, event.getName());
        cv.put(EventOpener.COL_TYPE, type);
        cv.put(EventOpener.COL_CATEGORY, event.getType());
        cv.put(EventOpener.COL_DATE, event.getStartDate());
        cv.put(EventOpener.COL_LOCATION, event.getCity());
        cv.put(EventOpener.COL_IMG_URL, event.getImgUrl());
        cv.put(EventOpener.COL_STATUS, event.getStatus());
        cv.put(EventOpener.COL_PRICE, event.getPrice());
        cv.put(EventOpener.COL_TICKETNUM, event.getPrice());
        cv.put(EventOpener.COL_ISACTIVE, event.getIsActive());
        database.insert(TABLE_NAME, "NullColumn", cv);
    }

    /**
     * Alert dialogue for adding events to the database.
     * @param database Database with table.
     * @param event Event to be added.
     * @param type Type of event being added.
     */
    private void eventAlert(SQLiteDatabase database, Events event, String type) {
        AlertDialog.Builder build = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.searchAlert))
                .setMessage(getString((type.equals("W") ? R.string.searchAlertWishMsg : R.string.searchAlertCartMsg)))
                .setNegativeButton(getString(R.string.alertNo), null)
                .setPositiveButton(getString(R.string.alertYes), (dialog, which) -> {
                    addEvent(database, event, type);
                    eventSnack(database, event);
                });
        build.show();
    }

    /**
     * Creates a snackbar for when the event is added. Can undo adding event.
     * @param database Database with table.
     * @param event Event to be removed if user clicks undo.
     */
    private void eventSnack(SQLiteDatabase database, Events event) {
        Snackbar snack = Snackbar.make(findViewById(R.id.eventActivity), R.string.searchSnack, Snackbar.LENGTH_LONG)
                .setAction(R.string.searchUndo, e -> {
                    database.delete(TABLE_NAME, "_id=?", new String[] { event.getId() });
                    Toast.makeText(this, R.string.searchRemove, Toast.LENGTH_SHORT).show();
                });
        snack.show();
    }
}