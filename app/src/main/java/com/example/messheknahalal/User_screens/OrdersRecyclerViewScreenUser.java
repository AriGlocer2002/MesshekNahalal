package com.example.messheknahalal.User_screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Admin_screens.OrdersAdapterFirebase;
import com.example.messheknahalal.Admin_screens.WrapContentLinearLayoutManager;
import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.example.messheknahalal.models.Order;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrdersRecyclerViewScreenUser extends SuperActivityWithNavigationDrawer implements
        OrdersAdapterFirebase.onOrderClickListener {

    RecyclerView rv_orders;
    OrdersAdapterFirebase ordersAdapter;

    final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
    final DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference("Date");

    MaterialButton btn_choose_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_recycler_view_screen_user);

        rv_orders = findViewById(R.id.orders_rv_screen_rv);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Query query = ordersRef.orderByChild("userEmail").equalTo(email);

        FirebaseRecyclerOptions<Order> options
                = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .setLifecycleOwner(this)
                .build();

        ordersAdapter = new OrdersAdapterFirebase(options, this, false, this);

        rv_orders.setAdapter(ordersAdapter);
        rv_orders.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        initializeNavigationDrawer(false);

        btn_choose_date = findViewById(R.id.orders_rv_screen_btn_choose_date);
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    btn_choose_date.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onOrderClick(int oldPosition, int position) {
        OrdersAdapterFirebase.OrderViewHolderFirebase viewHolder
                = (OrdersAdapterFirebase.OrderViewHolderFirebase) rv_orders.findViewHolderForAdapterPosition(oldPosition);

        if (viewHolder != null) {
            viewHolder.collapse();
            rv_orders.scrollToPosition(position);
        }

    }
}