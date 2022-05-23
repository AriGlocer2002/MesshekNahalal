package com.example.messheknahalal.User_screens;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
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

public class ShoppingScreen extends AppCompatActivity implements ProductsAdapter.OnTotalPriceChangedListener {

    RecyclerView rv_saved_products;
    ProductsAdapter productsAdapter;

    DatabaseReference notDeliveredOrders = FirebaseDatabase.getInstance().getReference("Orders");

    Toolbar toolbar;

    ArrayList<Product> products;

    TextView tv_total_price;

    SQLiteDatabase db;
    MaterialButton btn_confirm, btn_back, btn_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_screen);

        toolbar = findViewById(R.id.toolbar);
        rv_saved_products = findViewById(R.id.rv_saved_products);

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(v -> createCart());

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> onBackPressed());

        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(v -> clearCart());

        tv_total_price = findViewById(R.id.tv_total_price);

        db = openOrCreateDatabase(Utils.DATABASE_NAME, MODE_PRIVATE, null);
        products = Utils.getSavedProducts(db);

        productsAdapter = new ProductsAdapter(products, this, this);

        rv_saved_products.setAdapter(productsAdapter);
        rv_saved_products.setLayoutManager(new WrapContentLinearLayoutManager(this));

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_saved_products);

        onTotalPriceChanged(Utils.getTotalPrice(db));
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
                        checkProducts();
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

    private void checkProducts() {
        boolean inStock = true;
        ArrayList<Product> savedProducts = Utils.getSavedProducts(db);

        final int[] countOfProductsInStock = {0};

        for (int i = 0; i < savedProducts.size(); i++) {
            Product product = savedProducts.get(i);

            boolean productInStock = true;

            if (product.getAmount() == 0) {
                Utils.deleteProduct(db, product.getName());
                productInStock = false;
            }
            else {
                checkIfProductIsInStock(product, new OnCheckStockListener() {
                    @Override
                    public void onCheckStock(Product product) {
                        countOfProductsInStock[0]++;
                        if (countOfProductsInStock[0] == savedProducts.size()) {
                            createCart();
                        }
                    }
                });
            }
            inStock = productInStock;
        }
    }

    private void checkIfProductIsInStock(@NonNull Product product, OnCheckStockListener onCheckStockListener) {
       DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product")
               .child(product.getType()).child(Utils.productNameToPath(product.getName())).child("stock");

       productRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                   @Override
                   public void onSuccess(DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()){
                           double stock = dataSnapshot.getValue(double.class);
                           if (stock == 0){
                               Utils.showAlertDialog("Product not in stock", "The product " + product.getName()
                                       + " is not in stock anymore", ShoppingScreen.this);
                           }
                           else if (product.getAmount() > stock){
                               Utils.showAlertDialog("Not enough in stock", "There is not enough of product " + product.getName()
                                       + " in stock", ShoppingScreen.this);
                           }
                           else {
                               onCheckStockListener.onCheckStock(product);
                           }
                       }
                   }
               });
    }

    public interface OnCheckStockListener{
        void onCheckStock(Product product);
    }

    public void clearCart(){
        int amount = Utils.getAmountOfProductInCart(db);
        Utils.clearTable(db);
        productsAdapter.notifyItemRangeRemoved(0, amount);
        productsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTotalPriceChanged(double totalPrice) {
        tv_total_price.setText("total price: " + totalPrice + "₪");
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.START | ItemTouchHelper.END) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Utils.deleteProduct(db, products.get(viewHolder.getAbsoluteAdapterPosition()).getName());
            productsAdapter.notifyDataSetChanged();
        }
    };
}