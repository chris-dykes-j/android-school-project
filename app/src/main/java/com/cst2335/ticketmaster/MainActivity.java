package com.cst2335.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn1,btn2,btn3,btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}