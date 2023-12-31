package com.cst2335.ticketmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

/**
 * Extended from AppCompatActivity, but also adds a toolbar, NavigationDrawer, and the necessary methods.
 * Just more convenient.
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Sets the layout and adds a toolbar to the activity.
     * Make sure that your layout has the needed toolbar xml.
     * @param layoutId The layout with a toolbar. Need's to have a toolbar or toolbar will return null.
     */
    protected void setLayout(int layoutId) {
        setContentView(layoutId);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null; // Nifty test.

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                findViewById(R.id.include).setTranslationZ(5);
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                findViewById(R.id.include).setTranslationZ(-5);
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nav = findViewById(R.id.navBar);
        nav.setNavigationItemSelectedListener(this);
    }

    /**
     * Creates a menu for the toolbar
     * @param menu Menu to be added.
     * @return Returns true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Initiates activities for the menu items.
     * @param item Menu item selected.
     * @return Returns true.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
                startActivity(new Intent(this, BrowseCateActivity.class));
                break;
        }
        return true;
    }

    /**
     * Initiates activities for the Navigation Items.
     * @param item Navigation item selected.
     * @return Returns false;
     */
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

    /**
     * Creates an alert displaying all of the features in our beautiful application.
     */
    private void aboutAlert() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(R.string.dialog_title)
            .setMessage(R.string.helpMessage)
            .setNegativeButton(R.string.alert_cancel, (dialog, click1)->{ })
            .setPositiveButton(R.string.alert_confirm, (dialog, click2)->{ })
            .create().show();
    }
}