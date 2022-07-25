package com.cst2335.ticketmaster;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class BrowseCate extends AppCompatActivity {

    DetailsFragment aFragment = new DetailsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_cate);


        Button caBTN = findViewById(R.id.caButton);
        caBTN.setOnClickListener(click -> {

            DetailsFragment aFragment = new DetailsFragment();
            Bundle args = new Bundle();
            args.putString("countryCode", "CA");
            aFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, aFragment).commit();

        });

        Button usBTN = findViewById(R.id.usButton);
        usBTN.setOnClickListener(click -> {

            DetailsFragment aFragment = new DetailsFragment();
            Bundle args = new Bundle();
            args.putString("countryCode", "US");
            aFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, aFragment).commit();

        });


    }

}