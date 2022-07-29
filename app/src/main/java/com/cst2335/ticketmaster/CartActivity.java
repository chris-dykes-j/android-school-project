package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity  {

    ListView listView;
    TextView textView;

    EventOpener dbHelper;
    SQLiteDatabase db = null;
    Cursor cursor;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView = findViewById(R.id.cartList);
        textView = findViewById(R.id.cartText);

        dbHelper = new EventOpener(this);
        db = dbHelper.getWritableDatabase();

    }

    public void listUpdate(View v){

        cursor = db.rawQuery("SELECT * FROM Events", null);
        startManagingCursor(cursor);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_activated_1);

        while (cursor.moveToNext()) {
            adapter.add(cursor.getString(0));
        }

        listView.setAdapter(adapter);
    }


}
