package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.messheknahalal.R;
import com.example.messheknahalal.VPadapter;
import com.example.messheknahalal.viewPagerItem;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class HomeFragmentAdmin extends Fragment {

    public HomeFragmentAdmin() {
        // Required empty public constructor
    }

    Intent intent;
    ViewPager2 viewPager2;
    ArrayList<viewPagerItem> imageList;
    VPadapter adapter;
    WormDotsIndicator dotsIndicator;
    Button btn_my_profile, btn_orders, btn_users, btn_products;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.fragment_home_admin_viewPager);
        dotsIndicator = view.findViewById(R.id.fragment_home_admin_dots);

        imageList = new ArrayList<>();
        imageList.add(new viewPagerItem(R.drawable.image_nahalal));
        imageList.add(new viewPagerItem(R.drawable.bees_pic));
        imageList.add(new viewPagerItem(R.drawable.gan_yarak));
        imageList.add(new viewPagerItem(R.drawable.lettuce_img));
        adapter = new VPadapter(imageList);

        viewPager2.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager2);

        btn_my_profile = view.findViewById(R.id.fragment_home_admin_btn_my_profile);
        btn_orders = view.findViewById(R.id.fragment_home_admin_btn_orders);
        btn_users = view.findViewById(R.id.fragment_home_admin_btn_users);
        btn_products = view.findViewById(R.id.fragment_home_admin_btn_products);

        btn_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), MyProfileScreenAdmin.class);
                startActivity(intent);
            }
        });

        /*
        btn_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), .class);
                startActivity(intent);
            }
        });
         */

        btn_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), UsersRecycleViewScreenAdmin.class);
                startActivity(intent);
            }
        });

        btn_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), ProductsRecycleViewScreenAdmin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_admin, container, false);
    }
}