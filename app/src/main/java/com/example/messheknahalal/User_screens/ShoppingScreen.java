package com.example.messheknahalal.User_screens;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Admin_screens.WrapContentLinearLayoutManager;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.models.Cart;
import com.example.messheknahalal.models.Person;
import com.example.messheknahalal.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShoppingScreen extends AppCompatActivity implements View.OnClickListener, ProductsAdapter.OnTotalPriceChangedListener {

    RecyclerView rv_saved_products;
    ProductsAdapter productsAdapter;

    DatabaseReference notDeliveredOrders = FirebaseDatabase.getInstance().getReference("Orders");

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
            createCart();
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

    private void createCart() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        double finalPrice = Utils.getTotalPrice(db);
        ArrayList<String> productNames = Utils.getSavedProductsNames(db);

        if (productNames.isEmpty()){
            Toast.makeText(this, "There are no products in cart", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailPath = Utils.emailToPersonPath(email);

        FirebaseDatabase.getInstance().getReference("Person").child(emailPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person person = snapshot.getValue(Person.class);
                String personName = person.getFullName();

                Cart cart = new Cart(email, personName, false, finalPrice, productNames, -System.currentTimeMillis());

                notDeliveredOrders.child(String.valueOf(cart.getDate())).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ShoppingScreen.this, "Cart successfully added", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ShoppingScreen.this, "Cart adding failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onTotalPriceChanged(double totalPrice) {
        tv_total_price.setText("total price: " + totalPrice + "₪");
    }
}