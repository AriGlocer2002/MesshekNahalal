package com.example.messheknahalal.User_screens;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Admin_screens.ProductsAdapterFirebase;
import com.example.messheknahalal.Admin_screens.WrapContentLinearLayoutManager;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.models.Order;
import com.example.messheknahalal.models.Person;
import com.example.messheknahalal.models.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShoppingScreen extends AppCompatActivity {

    RecyclerView rv_saved_products;
    ProductsAdapterFirebase productsAdapterFirebase;

    final DatabaseReference notDeliveredOrders = FirebaseDatabase.getInstance().getReference("Orders");
    final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Product");

    Toolbar toolbar;

    TextView tv_empty_cart;
    TextView tv_total_price;

    MaterialButton btn_confirm, btn_back, btn_clear;

    DatabaseReference productsInCartRef = FirebaseDatabase.getInstance().getReference("Carts");

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

        tv_empty_cart = findViewById(R.id.tv_empty_cart);
        tv_total_price = findViewById(R.id.tv_total_price);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String emailPath = Utils.emailToUserPath(email);

        productsInCartRef = productsInCartRef.child(emailPath).child("products");

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(this)
                .setQuery(productsInCartRef, Product.class)
                .build();

        productsInCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_empty_cart.setVisibility(snapshot.exists() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productsAdapterFirebase = new ProductsAdapterFirebase(options,this);

        rv_saved_products.setAdapter(productsAdapterFirebase);
        rv_saved_products.setLayoutManager(new WrapContentLinearLayoutManager(this));

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_saved_products);

        checkTotalPrice();
    }

    private void checkTotalPrice() {
        productsInCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double totalPrice = 0;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        double price = product.getPrice();
                        double amount = product.getAmount();

                        totalPrice += price * amount;
                    }

                    tv_total_price.setText("Total price: " + totalPrice + "₪");
                    tv_total_price.setTag(totalPrice);
                }
                else {
                    tv_total_price.setText("Total price: 0.0 ₪");
                    tv_total_price.setTag(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createCart() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String emailPath = Utils.emailToUserPath(email);

        double finalPrice = (double) tv_total_price.getTag();
        ArrayList<Product> products = new ArrayList<>(productsAdapterFirebase.getSnapshots());

        if (products.isEmpty()){
            Toast.makeText(this, "There are no products in cart", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase.getInstance().getReference("User").child(emailPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person person = snapshot.getValue(Person.class);
                String personName = person.getFullName();

                Order cart = new Order(email, personName, false, finalPrice, products, System.currentTimeMillis());

                if (checkProducts(products)){
                    notDeliveredOrders.child(String.valueOf(cart.getDate())).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    clearCart();
                                    changeStock(products);
                                    Toast.makeText(ShoppingScreen.this, "Order successfully added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ShoppingScreen.this, "Order adding failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changeStock(@NonNull ArrayList<Product> products) {
        for (Product product : products){
            String productPath = Utils.productNameToPath(product.getName());
            productsRef.child(product.getType()).child(productPath)
                    .child("stock").setValue(ServerValue.increment(-product.getAmount()));
        }
    }

    private boolean checkProducts(@NonNull ArrayList<Product> products) {
        ArrayList<String> notInStock = new ArrayList<>();
        ArrayList<String> notEnoughInStock = new ArrayList<>();

        boolean inStock = true;

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getStock() == 0){
                notInStock.add(product.getName());
                inStock = false;
            }
            else if (product.getAmount() > product.getStock()){
                notEnoughInStock.add(product.getName());
                inStock = false;
            }
        }

        if (!inStock){

            if (!notInStock.isEmpty()){
                String notInStockMessage = "These products are not in stock anymore: \n";
                for (String productName : notInStock){
                    notInStockMessage += ", " + productName + "\n";
                }

                notInStockMessage = notInStockMessage.replaceFirst(",", "");
                Utils.showAlertDialog("Product not in stock", notInStockMessage, ShoppingScreen.this);
            }

            if (!notEnoughInStock.isEmpty()){
                String notEnoughInStockMessage = "There is not enough of this products: \n";
                for (String productName : notEnoughInStock){
                    notEnoughInStockMessage += ", " + productName + "\n";
                }

                notEnoughInStockMessage = notEnoughInStockMessage.replaceFirst(",", "");
                Utils.showAlertDialog("Not enough in stock", notEnoughInStockMessage, ShoppingScreen.this);
            }

        }

        return inStock;
    }

    public void clearCart(){
        productsInCartRef.removeValue();
    }

    final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.START | ItemTouchHelper.END) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Product product = productsAdapterFirebase.getItem(viewHolder.getBindingAdapterPosition());
            String productPath = Utils.productNameToPath(product.getName());
            productsInCartRef.child(productPath).removeValue();
        }
    };
}