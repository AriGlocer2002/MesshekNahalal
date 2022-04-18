package com.example.messheknahalal.Admin_screens;

import android.os.Bundle;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_admin_container, new HomeFragmentAdmin()).commit();


    }

    public BottomNavigationView.OnItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment fragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.home:
                            fragment = new HomeFragmentAdmin();
                            break;

                        case R.id.products:
                            fragment = new ProductsFragmentAdmin();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_admin_container, fragment).commit();
                    return true;
                }
            };

}