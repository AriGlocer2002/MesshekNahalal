package com.example.messheknahalal;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Utils.Utils;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {

    ArrayList<Integer> viewPagerItemArrayList;

    public VPAdapter(ArrayList<Integer> viewPagerItemArrayList) {
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

        int viewPagerItem = viewPagerItemArrayList.get(position);

        holder.picture.setImageResource(viewPagerItem);

    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements
            View.OnClickListener {

        ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.vp_home_iv_pic);
//            picture.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("ariel", "viewPager2 clicked");

            int itemPosition = getBindingAdapterPosition();
            int imageId = viewPagerItemArrayList.get(itemPosition);

            Uri imageUri = Utils.getUriToDrawable(itemView.getContext(), imageId);

            Log.d("ariel", "itemPosition = " + itemPosition);
            Log.d("ariel", "imageId = " + imageId);
            Log.d("ariel", "imageUri = " + imageUri);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(imageUri, "image/*");

            itemView.getContext().startActivity(intent);
        }
    }

}
