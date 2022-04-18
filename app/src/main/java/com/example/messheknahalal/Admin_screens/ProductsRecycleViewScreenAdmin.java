package com.example.messheknahalal.Admin_screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Objects.Product;
import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductsRecycleViewScreenAdmin extends SuperActivityWithNavigationDrawer implements View.OnClickListener {

    RecyclerView rv_products;
    ProductsAdapterFirebase productAdapter;

    ArrayList<Product> products;

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

        initializeNavigationDrawer(true);
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