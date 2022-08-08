package com.cst2335.ticketmaster;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;


import com.google.android.material.navigation.NavigationView;

public class BrowseCateActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_cate);


        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        tBar.setTitle("Category");
        setSupportActionBar(tBar);


        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //For changing categories
        Button caBTN = findViewById(R.id.caButton);
        caBTN.setOnClickListener(click -> {

            BC_DetailsFragment aFragment = new BC_DetailsFragment();
            Bundle args = new Bundle();
            args.putString("countryCode", "CA");
            aFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, aFragment).commit();

        });

        Button usBTN = findViewById(R.id.usButton);
        usBTN.setOnClickListener(click -> {

            BC_DetailsFragment aFragment = new BC_DetailsFragment();
            Bundle args = new Bundle();
            args.putString("countryCode", "US");
            aFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, aFragment).commit();

        });

        Button musicBTN = findViewById(R.id.muscicButton);
        musicBTN.setOnClickListener(click -> {

            BC_DetailsFragment aFragment = new BC_DetailsFragment();
            Bundle args = new Bundle();
            args.putString("countryCode", "MUSIC");
            aFragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, aFragment).commit();

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        return true;

    }

    /**
     * This method is used to create two help buttons
     * @param item menu item
     * @return return help menu items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.help:

                AlertDialog.Builder builder = new AlertDialog.Builder( BrowseCateActivity.this );
                String alert_msg = getResources().getString(R.string.alert_message);

                builder.setTitle(R.string.dialog_title)
                        .setMessage(alert_msg)
                        .setNegativeButton(R.string.alert_cancel, (dialog, click1)->{ })
                        .setPositiveButton(R.string.alert_confirm, (dialog, click2)->{ })
                        .create().show();
                break;

            case R.id.star:

                Intent goToStar = new Intent(BrowseCateActivity.this, StarListActivity.class);
                startActivity(goToStar);

        }

        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {


        switch(item.getItemId())
        {
            case R.id.navSearch:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.navWish:
                startActivity(new Intent(this, WishList.class));
                break;
            case R.id.navCart:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.navCategories:
                startActivity(new Intent(this, BrowseCateActivity.class));
                break;
            case R.id.navHome:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.navAbout:
                aboutAlert();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    private void aboutAlert() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.dialog_title)
                .setMessage(R.string.helpMessage)
                .setNegativeButton(R.string.alert_cancel, (dialog, click1)->{ })
                .setPositiveButton(R.string.alert_confirm, (dialog, click2)->{ })
                .create().show();
    }



}//end of class






