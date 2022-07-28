package com.cst2335.ticketmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

public class EventOpener extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "TicketmasterEvents";
    static final int VERSION_NUMBER = 1;

    public EventOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    static final String TABLE_NAME = "Events";
    static final String COL_NAME = "Name";
    static final String COL_TYPE = "Type"; // Favourites, Cart
    static final String COL_URL = "Url";
    static final String COL_IMG_URL = "ImageUrl";
    static final String COL_DATE = "EventDate";
    static final String COL_STATUS = "EventStatus";
    static final String COL_LOCATION = "Location";
    static final String COL_CATEGORY = "Category"; // Sports, Music, etc.
    static final String COL_PRICE = "TicketPrice";
    static final String COL_DETAILS = "Details";

    // Doesn't seem to like this. (returning null)
    static final String QUERY = String.format(
            "CREATE TABLE %s (_id INTEGER PRIMARY KEY, %s TEXT,  %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
            TABLE_NAME, COL_NAME, COL_TYPE, COL_URL, COL_IMG_URL, COL_DATE, COL_STATUS, COL_LOCATION, COL_CATEGORY, COL_PRICE, COL_DETAILS);

    @Override
    public void onCreate(@NonNull SQLiteDatabase database) {
        database.rawQuery(QUERY, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int VERSION_NUMBER) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        database.rawQuery(QUERY, null);
    }
}