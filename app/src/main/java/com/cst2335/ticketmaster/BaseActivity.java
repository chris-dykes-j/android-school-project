package com.cst2335.ticketmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Sets the layout and adds a toolbar to the activity.
     * Make sure that your layout has: <include layout="@layout/toolbar" />
     * @param layoutId The layout with the toolbar. Needed otherwise toolbar will be null.
     */
    protected void setLayout(int layoutId) {
        setContentView(layoutId);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null; // Nifty test.

//        View view = findViewById(R.id.drawer);
//        view.bringToFront();

        // NavigationDrawer stuff... WIP
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nav = findViewById(R.id.navBar);
        nav.setNavigationItemSelectedListener(this);
//        nav.bringToFront();
//        nav.invalidate();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
                startActivity(new Intent(this, BrowseCate.class));
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

}