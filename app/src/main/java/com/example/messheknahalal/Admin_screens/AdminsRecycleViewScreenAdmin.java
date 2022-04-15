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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.R;
import com.example.messheknahalal.loginScreen;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminsRecycleViewScreenAdmin extends AppCompatActivity {

    RecyclerView rv_admins;
    Intent intent;
    StorageReference rStore;
    AdminsAdapterFirebase adminAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Person");

    ArrayList<Admin> admins;
    DrawerLayout drawerMenu;
    ImageView nv_profile_img;
    TextView nd_tv_name, nd_tv_email;
    NavigationView nav_view;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_rv_admin);

        rv_admins = findViewById(R.id.admins_rv_screen_rv);
        admins = new ArrayList<>();


        Query query = FirebaseDatabase.getInstance().getReference("Admin");

        FirebaseRecyclerOptions<Admin> options
                = new FirebaseRecyclerOptions.Builder<Admin>()
                .setQuery(query, Admin.class)
                .setLifecycleOwner(this)
                .build();

        adminAdapter = new AdminsAdapterFirebase(options, this);

        rv_admins.setAdapter(adminAdapter);
        rv_admins.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


        //setting profile information in the navigation drawer
        FirebaseUser admin = auth.getCurrentUser();
        String adminEmail = admin.getEmail();
        String personPath = "Person_"+adminEmail.replace(".","-");
        rStore = FirebaseStorage.getInstance().getReference();

        NavigationView navigationView = findViewById(R.id.admins_rv_screen_nav_view);
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
                Glide.with(AdminsRecycleViewScreenAdmin.this).load(uri).centerCrop().into(nv_profile_img);
            }
        });

        //Navigation menu
        nd_tv_name = findViewById(R.id.header_nd_tv_name);
        nd_tv_email = findViewById(R.id.header_nd_tv_email);

        drawerMenu = findViewById(R.id.admins_rv_screen_drawer_layout);
        nav_view = findViewById(R.id.admins_rv_screen_nav_view);

        Toolbar toolbar = findViewById(R.id.admins_rv_screen_toolbar);
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
                        intent = new Intent(AdminsRecycleViewScreenAdmin.this, MyProfileScreenAdmin.class);
                        startActivity(intent);
                        break;

                    case R.id.home_item:
                        intent = new Intent(AdminsRecycleViewScreenAdmin.this, mainScreenAdmin.class);
                        startActivity(intent);
                        break;

//                    case R.id.orders_item:
//
//                        break;

                    case R.id.products_item:
                        intent = new Intent(AdminsRecycleViewScreenAdmin.this, ProductsRecycleViewScreenAdmin.class);
                        startActivity(intent);
                        break;

                    case R.id.logOut_item:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(AdminsRecycleViewScreenAdmin.this, loginScreen.class);
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

                NavigationView navigationView = findViewById(R.id.admins_rv_screen_nav_view);
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

    public void onBackPressed(){
        //if drawer is open close the drawer
        if(drawerMenu.isDrawerOpen(GravityCompat.START)){
            drawerMenu.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu_2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.return_ic:
                intent = new Intent(this, UsersRecycleViewScreenAdmin.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}

