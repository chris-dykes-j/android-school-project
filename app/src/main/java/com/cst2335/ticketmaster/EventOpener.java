package com.cst2335.ticketmaster;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Creates a database, and Events table for the Application.
 */
public class EventOpener extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "TicketmasterEvents";
    static final int VERSION_NUMBER = 2;

    /**
     * Constructor for the Event Opener.
     * @param context Context, but who knows what this actually is really...
     */
    public EventOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    static final String TABLE_NAME = "Events";
    static final String COL_NAME = "Name";
    static final String COL_TYPE = "Type"; // Favourites, Cart
    static final String COL_URL = "Url";
    static final String COL_IMG_URL = "ImageUrl";
    static final String COL_DATE = "Date";
    static final String COL_STATUS = "Status";
    static final String COL_LOCATION = "Location";
    static final String COL_CATEGORY = "Category"; // Sports, Music, etc.
    static final String COL_PRICE = "TicketPrice";
    static final String COL_DETAILS = "Details";
    static final String COL_ISACTIVE = "isActive"; // Y : active N :deleted
    static final String COL_TICKETNUM = "TicketNum";

    private final String QUERY = String.format(
            "CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT,  %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s REAL, %s TEXT,%s TEXT, %s INTEGER )",
            TABLE_NAME, COL_NAME, COL_TYPE, COL_URL, COL_IMG_URL, COL_DATE, COL_STATUS, COL_LOCATION, COL_CATEGORY, COL_PRICE, COL_DETAILS, COL_ISACTIVE, COL_TICKETNUM);

    /**
     * When first started, will create an event table for the database..
     * @param database Database to be used for table creation.
     */
    @Override
    public void onCreate(@NonNull SQLiteDatabase database) {
        // Log.i("Query", QUERY);
        database.execSQL(QUERY);
    }

    /**
     * Upgrades event table when version number increases.
     * @param database The database to execute the queries.
     * @param oldVersion The old version of the event opener.
     * @param VERSION_NUMBER Current version of Event Opener
     */
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase database, int oldVersion, int VERSION_NUMBER) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        database.execSQL(QUERY);
    }


}