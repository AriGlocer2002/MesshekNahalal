package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Admin_screens.HomeFragmentAdmin;
import com.example.messheknahalal.User_screens.MyProfileScreenUser;
import com.example.messheknahalal.User_screens.mainScreenUser;
import com.example.messheknahalal.loginScreen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class mainScreenAdmin extends AppCompatActivity {

    Intent intent;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    StorageReference rStore;
    DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin"),
            personRef = FirebaseDatabase.getInstance().getReference().child("Person");

    BottomNavigationView bottomNav;
    DrawerLayout drawerMenu;
    ImageView nv_profile_img;
    TextView nd_tv_name, nd_tv_email;
    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_admin);


        FirebaseUser admin = auth.getCurrentUser();
        String adminEmail = admin.getEmail();
        String adminPath = "Admin_"+adminEmail.replace(".","-");
        String personPath = "Person_"+adminEmail.replace(".","-");
        rStore = FirebaseStorage.getInstance().getReference();

        //setting profile information in the navigation drawer
        NavigationView navigationView = findViewById(R.id.main_screen_admin_nav_view);
        View headerView = navigationView.getHeaderView(0);

        loadDrawerNameAndLastName(personPath);

        TextView email = headerView.findViewById(R.id.header_nd_tv_email);
        email.setText(admin.getEmail());

        //profile image
        nv_profile_img = headerView.findViewById(R.id.header_nd_iv_pp);
        StorageReference profileRef = rStore.child("profiles/pp_"+adminEmail.replace(".","-")+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mainScreenAdmin.this).load(uri).centerCrop().into(nv_profile_img);
            }
        });

        //bottom navigation bar
        bottomNav = findViewById(R.id.main_screen_admin_bottomNav);

        bottomNav.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_admin_container, new HomeFragmentAdmin()).commit();


        //Navigation menu
        nd_tv_name = findViewById(R.id.header_nd_tv_name);
        nd_tv_email = findViewById(R.id.header_nd_tv_email);

        drawerMenu = findViewById(R.id.main_screen_admin_drawer_layout);
        nav_view = findViewById(R.id.main_screen_admin_nav_view);

        Toolbar toolbar = findViewById(R.id.main_screen_admin_toolbar);
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
                        intent = new Intent(mainScreenAdmin.this, MyProfileScreenAdmin.class);
                        startActivity(intent);
                        break;

//                    case R.id.orders_item:
//
//                        break;
//
//                    case R.id.users_item:
//
//                        break;
//
//                    case R.id.products_item:
//
//                        break;

                    case R.id.logOut_item:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(mainScreenAdmin.this, loginScreen.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    public void loadDrawerNameAndLastName(String path){
        personRef.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person p= snapshot.getValue(Person.class);
                String name = p.getName();
                String last_name = p.getLast_name();
                String fullName = name+" "+last_name;

                NavigationView navigationView = findViewById(R.id.main_screen_admin_nav_view);
                View headerView = navigationView.getHeaderView(0);

                TextView navAdminName = headerView.findViewById(R.id.header_nd_tv_name);
                navAdminName.setText(fullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
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

    public void onBackPressed(){
        //if drawer is open close the drawer
        if(drawerMenu.isDrawerOpen(GravityCompat.START)){
            drawerMenu.closeDrawer(GravityCompat.START);
        }
    }
}
