package com.example.messheknahalal.User_screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.messheknahalal.R;
import com.example.messheknahalal.loginScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class mainScreenUser extends AppCompatActivity {



    BottomNavigationView bottomNav;
    DrawerLayout drawerMenu;
    NavigationView nav_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);


        //bottom navigation bar
        bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        //Navigation menu
        drawerMenu = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerMenu, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerMenu.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        getSupportActionBar().setTitle("");
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.logOut_item:
                        Intent intent = new Intent(mainScreenUser.this, loginScreen.class);
                        startActivity(intent);
                }
                return false;
            }
        });


    }


    public BottomNavigationView.OnItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment fragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.home:
                            fragment = new HomeFragment();
                            break;

                        case R.id.products:
                            fragment = new ProductsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    return true;
                }
            };

    public void onBackPressed(){
        //if ddrawer is open close the drawer
        if(drawerMenu.isDrawerOpen(GravityCompat.START)){
            drawerMenu.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.shopping:
                Toast.makeText(this, "Shopping", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }
}



