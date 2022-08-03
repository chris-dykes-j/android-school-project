package com.cst2335.ticketmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BC_MyOpenHelper extends SQLiteOpenHelper {

    public static final String filename = "BrowserCateDB";
    public static final int version = 1;
    public static final String COL_ID = "_id";
    public static final String TABLE_NAME = "MyStar";
    public static final String COL_EVENT_NAME = "eventName";
    public static final String COL_EVENT_STATUS = "eventStatus";
    public static final String COL_START_DATE = "startDate";
    public static final String COL_EVENT_CITY = "eventCity";
    public static final String COL_MIN_PRICE = "minPrice";
    public static final String COL_MAX_PRICE = "maxPrice";
    public static final String COL_IMG_URL = "imgUrl";

    private final String statement = String.format(
            "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT,  %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT )",
            TABLE_NAME, COL_ID, COL_EVENT_NAME, COL_EVENT_STATUS, COL_START_DATE, COL_EVENT_CITY, COL_MIN_PRICE, COL_MAX_PRICE, COL_IMG_URL);


    public BC_MyOpenHelper(Context context) { super(context, filename, null, version); }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String result = String.format(" %s %s %s", "FirstString" , "10", "10.0" );
        db.execSQL(statement);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "Drop table if exists " + TABLE_NAME ); //deletes the current data
        //create a new table:

        this.onCreate(db); //calls function on line 26
    }
}
