package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Button btn1,btn2,btn3,btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn1 = findViewById(R.id.search);
        btn2 = findViewById(R.id.wishList);
        btn3 = findViewById(R.id.myEvent);
        btn4 = findViewById(R.id.searchCategories);

        // 1. Search events and display list (Chris)
        btn1.setOnClickListener(view -> {
                Intent goToSearch = new Intent(this, SearchActivity.class);
                startActivity(goToSearch);
            });

        // 2. Display favourites events (button to delete favorite city) = wish list (Jeongmi)
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add activity.
                Intent intent = new Intent(MainActivity.this,WishList.class);
                startActivity(intent);
            }
        });
        // 3. Save the event like a shopping cart (Taeyeon)
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);

            }
        });

        // 4. Browse Categories (Di)
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add activity.
                Intent intent = new Intent(MainActivity.this, BrowseCate.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.first_item:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.second_item:
                startActivity(new Intent(this, WishList.class));
                break;
            case R.id.third_item:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.fourth_item:
                startActivity(new Intent(this, BrowseCate.class));
                break;
        }
        return true;
    }
}