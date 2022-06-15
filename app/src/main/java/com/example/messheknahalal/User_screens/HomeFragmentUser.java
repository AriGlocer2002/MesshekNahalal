package com.example.messheknahalal.User_screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.messheknahalal.R;
import com.example.messheknahalal.VPAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;


public class HomeFragmentUser extends Fragment {

    public HomeFragmentUser() {
        // Required empty public constructor
    }

    ViewPager2 viewPager2;
    ArrayList<Integer> imageList;
    VPAdapter adapter;
    WormDotsIndicator dotsIndicator;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.fragment_home_user_viewPager);
        dotsIndicator = view.findViewById(R.id.fragment_home_user_dots);

        imageList = new ArrayList<>();
        imageList.add(R.drawable.image_nahalal);
        imageList.add(R.drawable.bees_pic);
        imageList.add(R.drawable.gan_yarak);
        imageList.add(R.drawable.lettuce_img);
        adapter = new VPAdapter(imageList);

        viewPager2.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager2);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_user, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dotsIndicator.setViewPager2(viewPager2);
    }

}