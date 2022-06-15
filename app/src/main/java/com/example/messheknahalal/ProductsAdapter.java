package com.example.messheknahalal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.models.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;
    private final List<Product> products;
    public DatabaseReference productsInCartRef;
    public final DatabaseReference productsRef;

    public ProductsAdapter(List<Product> products, Context context) {
        super();

        this.context = context;
        this.products = products;

        this.productsInCartRef = FirebaseDatabase.getInstance().getReference("Carts");

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String emailPath = Utils.emailToUserPath(email);

        this.productsInCartRef = productsInCartRef.child(emailPath).child("products");

        this.productsRef = FirebaseDatabase.getInstance().getReference("Product");
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        final TextView tv_full_name;
        final TextView tv_total_price;
        final TextView tv_amount;

        final ImageView products_rv_item_iv_pp;
        final ImageView products_rv_item_iv_pp_frame;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_full_name = itemView.findViewById(R.id.products_rv_item_tv_full_name);
            tv_amount = itemView.findViewById(R.id.products_rv_item_tv_amount_num);
            tv_total_price = itemView.findViewById(R.id.products_rv_item_tv_total_price);

            products_rv_item_iv_pp = itemView.findViewById(R.id.products_rv_item_iv_pp);
            products_rv_item_iv_pp_frame = itemView.findViewById(R.id.products_rv_item_iv_pp_frame);
        }

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_order_rv_item_user, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        if (product.getPicture() != null){
            Glide.with(context).load(product.getPicture()).centerCrop().into(holder.products_rv_item_iv_pp);
        }
        else {
            Glide.with(context).load(R.drawable.default_product_pic_rv).centerCrop().into(holder.products_rv_item_iv_pp);
        }

        holder.tv_full_name.setText(product.getName());
        holder.tv_amount.setText(""+product.getAmount());
        holder.tv_total_price.setText(product.getAmount() * product.getPrice() + " ₪");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}