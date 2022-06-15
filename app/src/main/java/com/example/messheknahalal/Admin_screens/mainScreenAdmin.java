package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.viewpager2.widget.ViewPager2;

import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.example.messheknahalal.VPAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class mainScreenAdmin extends SuperActivityWithNavigationDrawer {

    ViewPager2 viewPager2;
    ArrayList<Integer> imageList;
    VPAdapter adapter;
    WormDotsIndicator dotsIndicator;
    Button btn_my_profile, btn_orders, btn_users, btn_products, btn_create_new_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_admin);

        initializeNavigationDrawer(true);

        Log.d("ariel", getLocalClassName());

        viewPager2 = findViewById(R.id.fragment_home_admin_viewPager);
        dotsIndicator = findViewById(R.id.fragment_home_admin_dots);

        imageList = new ArrayList<>();
        imageList.add(R.drawable.image_nahalal);
        imageList.add(R.drawable.bees_pic);
        imageList.add(R.drawable.gan_yarak);
        imageList.add(R.drawable.lettuce_img);
        adapter = new VPAdapter(imageList);

        viewPager2.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager2);

        btn_create_new_product = findViewById(R.id.fragment_products_admin_btn_new_product);
        btn_create_new_product.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CreateNewProductAdmin.class)));

        btn_my_profile = findViewById(R.id.fragment_home_admin_btn_my_profile);
        btn_orders = findViewById(R.id.fragment_home_admin_btn_orders);
        btn_users = findViewById(R.id.fragment_home_admin_btn_users);
        btn_products = findViewById(R.id.fragment_home_admin_btn_products);

        btn_my_profile.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MyProfileScreenAdmin.class)));
        btn_orders.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OrdersRecyclerViewScreenAdmin.class)));
        btn_users.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UsersRecycleViewScreenAdmin.class)));
        btn_products.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ProductsRecycleViewScreenAdmin.class)));
    }

}