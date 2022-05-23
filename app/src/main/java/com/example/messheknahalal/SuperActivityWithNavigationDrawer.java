package com.example.messheknahalal;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Admin_screens.MyProfileScreenAdmin;
import com.example.messheknahalal.Admin_screens.OrdersRecyclerViewScreenAdmin;
import com.example.messheknahalal.Admin_screens.ProductsRecycleViewScreenAdmin;
import com.example.messheknahalal.Admin_screens.UsersRecycleViewScreenAdmin;
import com.example.messheknahalal.Admin_screens.mainScreenAdmin;
import com.example.messheknahalal.models.Person;
import com.example.messheknahalal.User_screens.MyProfileScreenUser;
import com.example.messheknahalal.User_screens.mainScreenUser;
import com.example.messheknahalal.Utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

public class SuperActivityWithNavigationDrawer extends AppCompatActivity {

    protected Intent navigationIntent;

    protected DrawerLayout drawerMenu;
    protected NavigationView nav_view;

    protected Toolbar toolbar;

    protected void initializeNavigationDrawer(boolean isAdmin){

        Log.d("ariel", "isAdmin is " + isAdmin);

        drawerMenu = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerMenu, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        headerView = nav_view.getHeaderView(0);

        drawerMenu.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        getSupportActionBar().setTitle("");
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_item:
                        navigationIntent = new Intent(getApplicationContext(), isAdmin ? mainScreenAdmin.class : mainScreenUser.class);
                        startActivity(navigationIntent);
                        break;

                    case R.id.orders_item:
                        navigationIntent = new Intent(getApplicationContext(), OrdersRecyclerViewScreenAdmin.class);
                        startActivity(navigationIntent);
                        break;

                    case R.id.myProfile_item:
                        navigationIntent = new Intent(getApplicationContext(), isAdmin ? MyProfileScreenAdmin.class : MyProfileScreenUser.class);
                        startActivity(navigationIntent);
                        break;

                    case R.id.users_item:
                        navigationIntent = new Intent(getApplicationContext(), UsersRecycleViewScreenAdmin.class);
                        startActivity(navigationIntent);
                        break;

                    case R.id.products_item:
                        navigationIntent = new Intent(getApplicationContext(), ProductsRecycleViewScreenAdmin.class);
                        startActivity(navigationIntent);
                        break;

                    case R.id.logOut_item:
                        FirebaseAuth.getInstance().signOut();
                        navigationIntent = new Intent(getApplicationContext(), loginScreen.class);
                        startActivity(navigationIntent);
                        break;

                    case R.id.myOrders_item:

                        break;

                    case R.id.callUs_item:
                        navigationIntent = new Intent(Intent.ACTION_DIAL);
                        navigationIntent.setData(Uri.parse("tel:0506829187"));
                        startActivity(navigationIntent);
                        break;

                    case R.id.emailUs_item:
                        sendEmail("ariogl02@gmail.com", "", "");
                        break;

                    case R.id.findUs_item:
                        String addressString = "Agriculture and Sustainability Center WIZO Nahalal";
                        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

                        navigationIntent = new Intent(Intent.ACTION_VIEW);
                        navigationIntent.setData(geoLocation);

                        startActivity(navigationIntent);
                        break;
                }

                return false;
            }
        });

        loadDrawerData();
    }

    protected void loadDrawerData(){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        String personPath = Utils.emailToPersonPath(email);

        DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Person");

        personRef.child(personPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Person p = snapshot.getValue(Person.class);
                    loadDrawerNameAndLastName(p.getName(), p.getLast_name());
                    loadDrawerEmail(p.getEmail());
                    loadDrawerImage(p.getPicture());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }

    protected void loadDrawerNameAndLastName(String name, String last_name){
        String fullName = name + " " + last_name;

        TextView navAdminName = headerView.findViewById(R.id.header_nd_tv_name);
        navAdminName.setText(fullName);
    }

    protected View headerView;

    protected void loadDrawerEmail(String email){
        TextView navEmail = headerView.findViewById(R.id.header_nd_tv_email);
        navEmail.setText(email);
    }

    protected RoundedImageView nv_profile_img;

    protected void loadDrawerImage(String picture){
        nv_profile_img = headerView.findViewById(R.id.header_nd_iv_pp);
        if (picture != null && !picture.isEmpty()){
            Glide.with(getApplicationContext()).load(picture).centerCrop().into(nv_profile_img);
        }
        else {
            Glide.with(getApplicationContext()).load(R.drawable.sample_profile).centerCrop().into(nv_profile_img);
        }
    }

    protected void sendEmail(@NonNull String recipientsList, String subject, String text){
        String[] recipients = "ariogl02@gmail.com".split(",");

        navigationIntent = new Intent(Intent.ACTION_SEND);
        navigationIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        navigationIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        navigationIntent.putExtra(Intent.EXTRA_TEXT,text);

        navigationIntent.setType("message/rfc822");
        startActivity(navigationIntent);
    }

    @Override
    public void onBackPressed(){
        //if drawer is open close the drawer
        if(drawerMenu.isDrawerOpen(GravityCompat.START)){
            drawerMenu.closeDrawer(GravityCompat.START);
        }
    }
}