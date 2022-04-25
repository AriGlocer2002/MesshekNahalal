package com.example.messheknahalal.Admin_screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.Product;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ProductsAdapterFirebase extends
        FirebaseRecyclerAdapter<Product, ProductsAdapterFirebase.ProductViewHolderFirebase> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;

    public ProductsAdapterFirebase(@NonNull FirebaseRecyclerOptions<Product> options, Context context) {
        super(options);
        this.context = context;
    }

    public class ProductViewHolderFirebase extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        RelativeLayout rl_product_item;

        TextView tv_full_name;
        TextView tv_price;
        TextView tv_stock;

        Button btn_edit;

        ImageView products_rv_item_iv_pp;
        ImageView products_rv_item_iv_pp_frame;

        public ProductViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            rl_product_item = itemView.findViewById(R.id.rl_product_item);

            tv_full_name = itemView.findViewById(R.id.products_rv_item_tv_full_name);
            tv_price = itemView.findViewById(R.id.products_rv_item_tv_price_num);
            tv_stock = itemView.findViewById(R.id.products_rv_item_tv_stock_num);

            btn_edit = itemView.findViewById(R.id.products_rv_item_button_edit);
            btn_edit.setOnClickListener(this);

            products_rv_item_iv_pp = itemView.findViewById(R.id.products_rv_item_iv_pp);
            products_rv_item_iv_pp_frame = itemView.findViewById(R.id.products_rv_item_iv_pp_frame);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == btn_edit) {
                Intent intent = new Intent(context, EditProduct.class);
                intent.putExtra("product", getItem(getBindingAdapterPosition()));
                context.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (view == itemView){

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm delete of product");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = getItem(getBindingAdapterPosition()).getName();
                        String product_type = getItem(getBindingAdapterPosition()).getType();
                        deleteProduct(name, product_type);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setMessage("Do you really want to delete this product?");
                dialog.show();
            }

            return true;
        }

        public void deleteProduct(String name, @NonNull String type){
            String productPath = Utils.productNameToPath(name);
            FirebaseDatabase.getInstance().getReference("Product").child(type).child(productPath).removeValue();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapterFirebase.ProductViewHolderFirebase holder, int position, @NonNull Product model) {

        holder.tv_full_name.setText(model.getName());
        holder.tv_price.setText(model.getPrice() + " ₪");
        holder.tv_stock.setText(model.getStock());

        String type = "Vegetable";

        if (model.getPicture() != null){
            Glide.with(context).load(model.getPicture()).centerCrop().into(holder.products_rv_item_iv_pp);
        }
        else {
            Glide.with(context).load(R.drawable.default_product_pic_rv).centerCrop().into(holder.products_rv_item_iv_pp);
        }

    }

    @NonNull
    @Override
    public ProductsAdapterFirebase.ProductViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_rv_item, parent, false);

        return new ProductsAdapterFirebase.ProductViewHolderFirebase(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }
}