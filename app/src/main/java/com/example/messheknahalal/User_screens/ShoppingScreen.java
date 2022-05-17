package com.example.messheknahalal.User_screens;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Admin_screens.WrapContentLinearLayoutManager;
import com.example.messheknahalal.Objects.Product;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShoppingScreen extends AppCompatActivity implements View.OnClickListener, ProductsAdapter.OnTotalPriceChangedListener {

    RecyclerView rv_saved_products;
    ProductsAdapter productsAdapter;
    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Product");

    Toolbar toolbar;

    ArrayList<Product> products;

    TextView tv_total_price;

    SQLiteDatabase db;
    MaterialButton btn_confirm, btn_back, btn_clear_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_screen);

        toolbar = findViewById(R.id.toolbar);
        rv_saved_products = findViewById(R.id.rv_saved_products);

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

/*        btn_clear_cart = findViewById(R.id.btn_clear_cart);
        btn_clear_cart.setOnClickListener(this);*/

        tv_total_price = findViewById(R.id.tv_total_price);

        db = openOrCreateDatabase(Utils.DATABASE_NAME, MODE_PRIVATE, null);
        products = Utils.getSavedProducts(db);

        productsAdapter = new ProductsAdapter(products, this, this);

        rv_saved_products.setAdapter(productsAdapter);
        rv_saved_products.setLayoutManager(new WrapContentLinearLayoutManager(this));

        onTotalPriceChanged(Utils.getTotalPrice(db));
    }

    @Override
    public void onClick(View v) {
        if (v == btn_confirm) {

        }
        else if (v == btn_back) {
            onBackPressed();
        }
        else if(v == btn_clear_cart){
            int size = Utils.getAmountOfProductInCart(db);
            Utils.deleteTable(db);
            productsAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public void onTotalPriceChanged(double totalPrice) {
        tv_total_price.setText("total price: " + totalPrice + "₪");
    }
}