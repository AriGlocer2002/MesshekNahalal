package com.example.messheknahalal.Admin_screens;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class mainScreenAdmin extends SuperActivityWithNavigationDrawer {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_admin);

        initializeNavigationDrawer(true);

        //bottom navigation bar
        bottomNav = findViewById(R.id.main_screen_admin_bottomNav);

        bottomNav.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_admin_container, new HomeFragmentAdmin(), HomeFragmentAdmin.TAG).commit();

        Log.d("ariel", getLocalClassName());
    }

    public BottomNavigationView.OnItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment fragment;
                    Fragment currentFragment;
                    String TAG;

                    switch (menuItem.getItemId()) {
                        case R.id.home:
                            TAG = HomeFragmentAdmin.TAG;
                            currentFragment = new HomeFragmentAdmin();
                            break;

                        case R.id.products:
                            TAG = ProductsFragmentAdmin.TAG;
                            currentFragment = new ProductsFragmentAdmin();
                            break;
                        default:
                            throw new IllegalStateException(
                                    "Unexpected value: " + menuItem.getItemId());
                    }

                    fragment = getSupportFragmentManager().findFragmentByTag(TAG);
                    if (fragment == null){
                        fragment = currentFragment;
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_admin_container, fragment, TAG).commit();
                    }

                    return true;
                }
            };

}