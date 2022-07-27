package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

        Event event = (Event) getIntent().getSerializableExtra("Event");
        TextView title = findViewById(R.id.eventTitle);
        title.setText(event.getName());

        Button wish = findViewById(R.id.wishButton);
        Button cart = findViewById(R.id.cartButton);

        wish.setOnClickListener(v -> addEvent(database, event, "W"));
        cart.setOnClickListener(v -> addEvent(database, event, "C"));
    }

    private void addEvent(SQLiteDatabase database, Event event, String type) {
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