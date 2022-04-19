package com.example.messheknahalal.Admin_screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Objects.Product;
import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductsRecycleViewScreenAdmin extends SuperActivityWithNavigationDrawer
        implements View.OnClickListener {

    RecyclerView rv_products;
    ProductsAdapterFirebase productAdapter;

    ImageView btn_vegetable, btn_fruit, btn_shelf, btn_other;
    String type = "Vegetable";

    int currentPosition = -1;
    int oldPosition = -1;

    ImageView[] productTypes = new ImageView[4];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_rv_admin);

        rv_products = findViewById(R.id.products_rv_screen_rv);
        btn_vegetable = findViewById(R.id.products_rv_screen_btn_vegetable);
        btn_fruit = findViewById(R.id.products_rv_screen_btn_fruit);
        btn_shelf = findViewById(R.id.products_rv_screen_btn_shelf);
        btn_other = findViewById(R.id.products_rv_screen_btn_other);

        productTypes[0] = btn_vegetable;
        productTypes[1] = btn_fruit;
        productTypes[2] = btn_shelf;
        productTypes[3] = btn_other;

        btn_vegetable.setOnClickListener(this);

        btn_fruit.setOnClickListener(this);

        btn_shelf.setOnClickListener(this);

        btn_other.setOnClickListener(this);

        btn_vegetable.callOnClick();

        initializeNavigationDrawer(true);
    }

    @Override
    public void onClick(View view) {

        oldPosition = currentPosition;

        if (view == btn_vegetable) {
            type = "Vegetable";
            currentPosition = 0;
        }
        if (view == btn_fruit) {
            type = "Fruit";
            currentPosition = 1;
        }
        if (view == btn_shelf) {
            type = "Shelf";
            currentPosition = 2;
        }
        if (view == btn_other) {
            type = "Other";
            currentPosition = 3;
        }

        Log.d("ariel", "oldPosition = " + oldPosition);
        Log.d("ariel", "currentPosition = " + currentPosition);

        DatabaseReference currentRef = FirebaseDatabase.getInstance().getReference("Product").child(type);

        if (oldPosition != currentPosition){

            if (oldPosition > -1){
                productTypes[oldPosition].setBackgroundResource(0);
            }

            productTypes[currentPosition].setBackgroundResource(R.drawable.producttype_selected_background);

            FirebaseRecyclerOptions<Product> options
                    = new FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(currentRef, Product.class)
                    .setLifecycleOwner(ProductsRecycleViewScreenAdmin.this)
                    .build();

            productAdapter = new ProductsAdapterFirebase(options, ProductsRecycleViewScreenAdmin.this);

            Log.d("ariel", "product recycling started");

            rv_products.setAdapter(productAdapter);
            rv_products.setLayoutManager(new WrapContentLinearLayoutManager(this));
        }

    }
}