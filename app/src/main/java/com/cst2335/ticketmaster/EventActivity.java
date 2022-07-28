package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity {

    private static final String TABLE_NAME = "Events";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // or Database here?
        EventOpener helper = new EventOpener(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        Events event = (Events) getIntent().getSerializableExtra("Event");
        TextView title = findViewById(R.id.eventTitle);
        TextView desc = findViewById(R.id.searchEventDate);

        title.setText(event.getName());
        desc.setText(event.getStartDate()); // Should be description.
        new DownloadImageTask(findViewById(R.id.eventImg))
                .execute(event.getImgUrl());

        ImageButton wish = findViewById(R.id.wishEventButton);
        ImageButton cart = findViewById(R.id.cartEventButton);

        wish.setOnClickListener(v -> addEvent(database, event, "W"));
        cart.setOnClickListener(v -> addEvent(database, event, "C"));
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
        // cv.put(EventOpener);
        database.insert(TABLE_NAME, "NullColumn", cv);

//        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        int i = cursor.getColumnIndex("type");
//        cursor.moveToFirst();
//        while (cursor.moveToNext())
//            Log.e("Cool", cursor.getString(i));
    }
}