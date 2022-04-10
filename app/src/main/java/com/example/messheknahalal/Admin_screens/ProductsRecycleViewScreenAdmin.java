package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.Objects.Product;
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

public class ProductsRecycleViewScreenAdmin extends AppCompatActivity implements View.OnClickListener{

    RecyclerView rv_products;
    Intent intent;
    StorageReference rStore;
    ProductsAdapterFirebase productAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Person");

    ArrayList<Product> products;
    DrawerLayout drawerMenu;
    ImageView nv_profile_img;
    TextView nd_tv_name, nd_tv_email;
    NavigationView nav_view;

    Button btn_vegetable, btn_fruit, btn_shelf, btn_other;
    String type = "vegetable";

    DatabaseReference vegetableRef;
    DatabaseReference fruitRef;
    DatabaseReference shelfRef;
    DatabaseReference otherRef;

    int currentPosition;
    int oldPosition;

    Button[] productTypes = new Button[4];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_rv_admin);

        rv_products = findViewById(R.id.products_rv_screen_rv);
        btn_vegetable = findViewById(R.id.products_rv_screen_btn_vegetable);
        btn_fruit = findViewById(R.id.products_rv_screen_btn_fruit);
        btn_shelf = findViewById(R.id.products_rv_screen_btn_shelf);
        btn_other = findViewById(R.id.products_rv_screen_btn_other);
        products = new ArrayList<>();

        vegetableRef = FirebaseDatabase.getInstance().getReference("Product").child("Vegetable");
        fruitRef = FirebaseDatabase.getInstance().getReference("Product").child("Fruit");
        shelfRef = FirebaseDatabase.getInstance().getReference("Product").child("Shelf");
        otherRef = FirebaseDatabase.getInstance().getReference("Product").child("Other");

        productTypes[0] = btn_vegetable;
        productTypes[1] = btn_fruit;
        productTypes[2] = btn_shelf;
        productTypes[3] = btn_other;

        btn_vegetable.setOnClickListener(this);

        btn_fruit.setOnClickListener(this);

        btn_shelf.setOnClickListener(this);

        btn_other.setOnClickListener(this);
/*
        Query query = FirebaseDatabase.getInstance().getReference("Product").child("Fruit");

        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .setLifecycleOwner(this)
                .build();

        productAdapter = new ProductsAdapterFirebase(options, this);

        rv_products.setAdapter(productAdapter);
        rv_products.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));*/

        btn_vegetable.callOnClick();


        //setting profile information in the navigation drawer
        FirebaseUser admin = auth.getCurrentUser();
        String adminEmail = admin.getEmail();
        String personPath = "Person_"+adminEmail.replace(".","-");
        rStore = FirebaseStorage.getInstance().getReference();

        NavigationView navigationView = findViewById(R.id.products_rv_screen_nav_view);
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
                Glide.with(ProductsRecycleViewScreenAdmin.this).load(uri).centerCrop().into(nv_profile_img);
            }
        });

        //Navigation menu
        nd_tv_name = findViewById(R.id.header_nd_tv_name);
        nd_tv_email = findViewById(R.id.header_nd_tv_email);

        drawerMenu = findViewById(R.id.products_rv_screen_drawer_layout);
        nav_view = findViewById(R.id.products_rv_screen_nav_view);

        Toolbar toolbar = findViewById(R.id.products_rv_screen_toolbar);
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
                        intent = new Intent(ProductsRecycleViewScreenAdmin.this, MyProfileScreenAdmin.class);
                        startActivity(intent);
                        break;

                    case R.id.home_item:
                        intent = new Intent(ProductsRecycleViewScreenAdmin.this, mainScreenAdmin.class);
                        startActivity(intent);
                        break;

//                    case R.id.orders_item:
//
//                        break;

                    case R.id.users_item:
                        intent = new Intent(ProductsRecycleViewScreenAdmin.this, UsersRecycleViewScreenAdmin.class);
                        startActivity(intent);
                        break;

                    case R.id.logOut_item:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(ProductsRecycleViewScreenAdmin.this, loginScreen.class);
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

                NavigationView navigationView = findViewById(R.id.products_rv_screen_nav_view);
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
    public void onClick(View view) {

        DatabaseReference currentRef = null;

        oldPosition = currentPosition;

        if (view == btn_vegetable) {
            type = "vegetable";

            currentPosition = 0;

            currentRef = vegetableRef;
        }
        if (view == btn_fruit) {
            type = "fruit";

            currentPosition = 1;

            currentRef = fruitRef;
        }
        if (view == btn_shelf) {
            type = "shelf";

            currentPosition = 2;

            currentRef = shelfRef;
        }
        if (view == btn_other) {
            type = "other";

            currentPosition = 3;

            currentRef = otherRef;
        }

        if (oldPosition != currentPosition){

            productTypes[oldPosition].setBackgroundResource(0);
            productTypes[currentPosition].setBackgroundResource(R.drawable.producttype_selected_background);

            FirebaseRecyclerOptions<Product> options
                    = new FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(currentRef, Product.class)
                    .setLifecycleOwner(ProductsRecycleViewScreenAdmin.this)
                    .build();

            productAdapter = new ProductsAdapterFirebase(options, ProductsRecycleViewScreenAdmin.this);

            rv_products.setAdapter(productAdapter);
            rv_products.setLayoutManager(new WrapContentLinearLayoutManager(ProductsRecycleViewScreenAdmin.this, LinearLayoutManager.VERTICAL,false));
        }

    }
}
