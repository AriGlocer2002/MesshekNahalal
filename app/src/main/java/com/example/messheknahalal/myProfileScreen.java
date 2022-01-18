package com.example.messheknahalal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.messheknahalal.User_screens.mainScreenUser;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class myProfileScreen extends AppCompatActivity{

    DrawerLayout drawerMenu;
    NavigationView nav_view;
    Intent intent;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        FirebaseUser user = auth.getCurrentUser();
        String userEmail = user.getEmail();



        drawerMenu = findViewById(R.id.my_profile_drawer_layout);
        nav_view = findViewById(R.id.my_profile_nav_view);

        Toolbar toolbar = findViewById(R.id.my_profile_toolbar);
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
                    case R.id.home_item:
                        intent = new Intent(myProfileScreen.this, mainScreenUser.class);
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
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(myProfileScreen.this, loginScreen.class);
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

    public void onBackPressed(){
        //if drawer is open close the drawer
        if(drawerMenu.isDrawerOpen(GravityCompat.START)){
            drawerMenu.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
