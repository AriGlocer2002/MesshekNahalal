package com.example.messheknahalal.Admin_screens;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.example.messheknahalal.models.Order;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrdersRecyclerViewScreenAdmin extends SuperActivityWithNavigationDrawer implements
        OrdersAdapterFirebase.onOrderClickListener {

    RecyclerView rv_orders;
    OrdersAdapterFirebase ordersAdapter;

    final DatabaseReference notDeliveredOrders = FirebaseDatabase.getInstance().getReference("Orders");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_recycler_view_screen_admin);

        rv_orders = findViewById(R.id.orders_rv_screen_rv);

        Query query = notDeliveredOrders.orderByChild("delivered").equalTo(false);

        FirebaseRecyclerOptions<Order> options
                = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .setLifecycleOwner(this)
                .build();

        ordersAdapter = new OrdersAdapterFirebase(options, this, true, this);

        rv_orders.setAdapter(ordersAdapter);
        rv_orders.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        initializeNavigationDrawer(true);
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