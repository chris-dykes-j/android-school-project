package com.cst2335.ticketmaster;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class SearchEventActivity extends AppCompatActivity {

    private static final String TABLE_NAME = "Events";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // getting database
        EventOpener helper = new EventOpener(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        Events event = (Events) getIntent().getSerializableExtra("Event");
        TextView title = findViewById(R.id.eventTitle);
        TextView desc = findViewById(R.id.searchEventDate);

        title.setText(event.getName());
        desc.setText(event.getStartDate()); // Should be description.
        new DownloadImageTask(findViewById(R.id.eventImg))
                .execute(event.getImgUrl());

        // The toast
        // Toast.makeText(this, R.string.searchToast, Toast.LENGTH_SHORT).show();

        ImageButton wish = findViewById(R.id.wishEventButton);
        ImageButton cart = findViewById(R.id.cartEventButton);

        wish.setOnClickListener(v -> eventAlert(database, event, "W"));
        cart.setOnClickListener(v -> eventAlert(database, event, "C"));
    }

    private void addEvent(SQLiteDatabase database, Events event, String type) {
        ContentValues cv = new ContentValues();
        cv.put(EventOpener.COL_NAME, event.getName());
        cv.put(EventOpener.COL_TYPE, type);
        cv.put(EventOpener.COL_CATEGORY, event.getType());
        cv.put(EventOpener.COL_DATE, event.getStartDate());
        cv.put(EventOpener.COL_LOCATION, event.getCity());
        cv.put(EventOpener.COL_IMG_URL, event.getImgUrl());
        cv.put(EventOpener.COL_STATUS, event.getStatus());
        database.insert(TABLE_NAME, "NullColumn", cv);
    }

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

    private void eventSnack(SQLiteDatabase database, Events event) {
        Snackbar snack = Snackbar.make(findViewById(R.id.eventActivity), R.string.searchSnack, Snackbar.LENGTH_LONG)
                .setAction(R.string.searchUndo, e -> {
                    database.delete(TABLE_NAME, "_id=?", new String[] { event.getId() });
                    Toast.makeText(this, R.string.searchRemove, Toast.LENGTH_SHORT).show();
                });
        snack.show();
    }
}