package com.example.messheknahalal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    ViewPager2 viewPager2;
    ArrayList<viewPagerItem> imageList;
    VPadapter adapter;
    WormDotsIndicator dotsIndicator;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.viewPagerHome);
        dotsIndicator = view.findViewById(R.id.dots);


        imageList = new ArrayList<>();
        imageList.add(new viewPagerItem(R.drawable.image_nahalal));
        imageList.add(new viewPagerItem(R.drawable.bees_pic));
        imageList.add(new viewPagerItem(R.drawable.gan_yarak));
        imageList.add(new viewPagerItem(R.drawable.lettuce_img));
        adapter = new VPadapter(imageList);

        viewPager2.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager2);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}