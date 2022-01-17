package com.example.messheknahalal.User_screens;

import android.content.Intent;
import android.net.Uri;
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
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_user);

        //bottom navigation bar
        bottomNav = findViewById(R.id.main_screen_user_bottomNav);

        bottomNav.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_user_container, new HomeFragment()).commit();

        //Navigation menu
        drawerMenu = findViewById(R.id.main_screen_user_drawer_layout);
        nav_view = findViewById(R.id.main_screen_user_nav_view);

        Toolbar toolbar = findViewById(R.id.main_screen_user_toolbar);
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
                    case R.id.myProfile_item:
                        intent = new Intent(mainScreenUser.this, loginScreen.class);
                        startActivity(intent);
                        break;

                    case R.id.myOrders_item:

                        break;

                    case R.id.callUs_item:
                        intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:0506829187"));
                        startActivity(intent);
                        break;

                    case R.id.emailUs_item:
                        sendEmail("ariogl02@gmail.com", "", "");
                        break;

                    case R.id.findUs_item:
                        String addressString = "Agriculture and Sustainability Center WIZO Nahalal";
                        Uri geoLocation = Uri.parse("geo:0,0?q="+addressString);

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(geoLocation);

                        startActivity(intent);
                        break;

                    case R.id.logOut_item:
                        intent = new Intent(mainScreenUser.this, loginScreen.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });


    }

    public void sendEmail(String recipientsList, String subject, String text){
        String[] recipients = recipientsList.split(",");

        intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);

        intent.setType("message/rfc822");
        startActivity(intent);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_user_container, fragment).commit();
                    return true;
                }
            };

    public void onBackPressed(){
        //if drawer is open close the drawer
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



