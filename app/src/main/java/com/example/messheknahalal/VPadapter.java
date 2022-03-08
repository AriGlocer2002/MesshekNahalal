package com.example.messheknahalal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VPadapter extends RecyclerView.Adapter<VPadapter.ViewHolder> {

    ArrayList<viewPagerItem> viewPagerItemArrayList;

    public VPadapter(ArrayList<viewPagerItem> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        viewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);

        holder.picture.setImageResource(viewPagerItem.imageId);

    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.vp_home_iv_pic);

        }
    }

}
