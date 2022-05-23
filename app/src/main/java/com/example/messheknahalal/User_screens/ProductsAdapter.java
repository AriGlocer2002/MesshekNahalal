package com.example.messheknahalal.User_screens;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.models.Product;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>{

    private final ArrayList<Product> products;
    private final Context context;
    private final SQLiteDatabase db;
    private final OnTotalPriceChangedListener onTotalPriceChangedListener;

    public ProductsAdapter(ArrayList<Product> products, @NonNull Context context, OnTotalPriceChangedListener onTotalPriceChangedListener) {
        super();

        this.products = products;
        this.context = context;
        this.db = context.openOrCreateDatabase(Utils.DATABASE_NAME, Context.MODE_PRIVATE, null);
        this.onTotalPriceChangedListener = onTotalPriceChangedListener;
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_saved_rv_item_users, parent, false);

        return new ProductsAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductViewHolder holder, int position) {
        Product product = products.get(position);

        holder.tv_full_name.setText(product.getName());
        holder.tv_price.setText(product.getAmount()*product.getPrice() + " ₪");
        holder.tv_stock.setText(product.getStock() + "");

        holder.tv_amount.setText(product.getAmount() + "");

        if (product.getPicture() != null){
            Glide.with(context).load(product.getPicture()).centerCrop().into(holder.products_rv_item_iv_pp);
        }
        else {
            Glide.with(context).load(R.drawable.default_product_pic_rv).centerCrop().into(holder.products_rv_item_iv_pp);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_full_name;
        TextView tv_price;
        TextView tv_stock;

        ImageView iv_remove;
        TextView tv_amount;
        ImageView iv_add;

        ImageView products_rv_item_iv_pp;
        ImageView products_rv_item_iv_pp_frame;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_full_name = itemView.findViewById(R.id.products_rv_item_tv_full_name);
            tv_price = itemView.findViewById(R.id.products_rv_item_tv_price_num);
            tv_stock = itemView.findViewById(R.id.products_rv_item_tv_stock_num);

            products_rv_item_iv_pp = itemView.findViewById(R.id.products_rv_item_iv_pp);
            products_rv_item_iv_pp_frame = itemView.findViewById(R.id.products_rv_item_iv_pp_frame);

            iv_remove = itemView.findViewById(R.id.iv_remove);
            iv_remove.setOnClickListener(this);

            tv_amount = itemView.findViewById(R.id.tv_amount);

            iv_add = itemView.findViewById(R.id.iv_add);
            iv_add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            double amount = products.get(getBindingAdapterPosition()).getAmount();

            double by = products.get(getBindingAdapterPosition()).isCountable() ? 1 : 0.25;

            if (v == iv_add){
                products.get(getBindingAdapterPosition()).setAmount(amount+by);
            }
            else if (v == iv_remove && amount > 0) {
                products.get(getBindingAdapterPosition()).setAmount(amount-by);
            }

            Log.d("ariel", products.toString());
            Utils.addProduct(db, products.get(getBindingAdapterPosition()));

            onTotalPriceChangedListener.onTotalPriceChanged(Utils.getTotalPrice(db));

            notifyItemChanged(getBindingAdapterPosition());
        }
    }

    public interface OnTotalPriceChangedListener{
        void onTotalPriceChanged(double totalPrice);
    }

}
