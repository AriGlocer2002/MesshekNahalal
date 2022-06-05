package com.example.messheknahalal.Admin_screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.models.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductsAdapterFirebase extends
        FirebaseRecyclerAdapter<Product, ProductsAdapterFirebase.ProductViewHolderFirebase> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;
    private OnAddToCartListener onAddToCartListener;
    private final int mode;
    public DatabaseReference productsInCartRef;
    public final DatabaseReference productsRef;

    public static final int RV_PRODUCTS_ADMIN = R.layout.products_rv_item_admin;
    public static final int RV_PRODUCTS_USER = R.layout.products_rv_item_user;
    public static final int RV_PRODUCTS_IN_CART = R.layout.products_saved_rv_item_users;

    public ProductsAdapterFirebase(@NonNull FirebaseRecyclerOptions<Product> options, Context context, boolean isAdmin, OnAddToCartListener onAddToCartListener) {
        super(options);
        this.context = context;
        this.onAddToCartListener = onAddToCartListener;

        this.productsInCartRef = FirebaseDatabase.getInstance().getReference("Carts");
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String emailPath = Utils.emailToUserPath(email);
        this.productsInCartRef = productsInCartRef.child(emailPath).child("products");

        this.productsRef = FirebaseDatabase.getInstance().getReference("Product");

        if (isAdmin){
            this.mode = RV_PRODUCTS_ADMIN;
        }
        else {
            this.mode = RV_PRODUCTS_USER;
        }
    }

    public ProductsAdapterFirebase(@NonNull FirebaseRecyclerOptions<Product> options, @NonNull Context context) {
        super(options);

        this.context = context;

        this.productsInCartRef = FirebaseDatabase.getInstance().getReference("Carts");

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String emailPath = Utils.emailToUserPath(email);

        this.productsInCartRef = productsInCartRef.child(emailPath).child("products");

        this.productsRef = FirebaseDatabase.getInstance().getReference("Product");

        this.mode = RV_PRODUCTS_IN_CART;
    }

    public class ProductViewHolderFirebase extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        final TextView tv_full_name;
        final TextView tv_price;
        final TextView tv_stock;

        MaterialButton btn_edit;
        MaterialButton btn_cart;

        ImageView iv_remove;
        TextView tv_amount;
        ImageView iv_add;

        final ImageView products_rv_item_iv_pp;
        final ImageView products_rv_item_iv_pp_frame;

        public ProductViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            tv_full_name = itemView.findViewById(R.id.products_rv_item_tv_full_name);
            tv_price = itemView.findViewById(R.id.products_rv_item_tv_price_num);
            tv_stock = itemView.findViewById(R.id.products_rv_item_tv_stock_num);

            switch (mode){
                case RV_PRODUCTS_ADMIN:
                    btn_edit = itemView.findViewById(R.id.products_rv_item_button_edit);
                    btn_edit.setOnClickListener(this);
                    itemView.setOnLongClickListener(this);

                    break;
                case RV_PRODUCTS_USER:
                    btn_cart = itemView.findViewById(R.id.products_rv_item_button_cart);
                    btn_cart.setOnClickListener(this);

                    break;
                case RV_PRODUCTS_IN_CART:

                    iv_remove = itemView.findViewById(R.id.iv_remove);
                    iv_remove.setOnClickListener(this);

                    tv_amount = itemView.findViewById(R.id.tv_amount);

                    iv_add = itemView.findViewById(R.id.iv_add);
                    iv_add.setOnClickListener(this);

                    break;
            }

            products_rv_item_iv_pp = itemView.findViewById(R.id.products_rv_item_iv_pp);
            products_rv_item_iv_pp_frame = itemView.findViewById(R.id.products_rv_item_iv_pp_frame);
        }

        @Override
        public void onClick(View view) {

            String productPath = Utils.productNameToPath(getItem(getBindingAdapterPosition()).getName());

            if (view == btn_edit) {
                Intent intent = new Intent(context, EditProduct.class);
                intent.putExtra("product", getItem(getBindingAdapterPosition()));
                context.startActivity(intent);
            }
            else if(view == btn_cart){
                onAddToCartListener.addToCart(getBindingAdapterPosition(), getItem(getBindingAdapterPosition()));
            }
            else if (view == iv_add){
                boolean countable = getItem(getBindingAdapterPosition()).isCountable();
                double amount = getItem(getBindingAdapterPosition()).getAmount();
                double by = countable ? 1 : 0.25;

                productsInCartRef.child(productPath).child("amount").setValue(amount + by);
            }
            else if (view == iv_remove) {
                boolean countable = getItem(getBindingAdapterPosition()).isCountable();
                double amount = getItem(getBindingAdapterPosition()).getAmount();
                double by = countable ? 1 : 0.25;

                if (amount > by) {
                    productsInCartRef.child(productPath).child("amount").setValue(amount - by);
                }
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
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

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
        String productPath = Utils.productNameToPath(model.getName());

        holder.tv_full_name.setText(model.getName());
        holder.tv_stock.setText(model.getStock() + "");

        switch (mode){
            case RV_PRODUCTS_ADMIN:
                holder.tv_price.setText(model.getPrice() + " ₪");
                break;
            case RV_PRODUCTS_USER:
                holder.tv_price.setText(model.getPrice() + " ₪");

                productsInCartRef.child(productPath).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                holder.btn_cart.setText(snapshot.exists() ? "Added" : "Add to cart");
                                holder.btn_cart.getBackground().setTint(snapshot.exists() ? Color.BLACK : Color.WHITE);
                                holder.btn_cart.setTextColor(snapshot.exists() ? Color.WHITE : Color.BLACK);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                break;
            case RV_PRODUCTS_IN_CART:
                holder.tv_price.setText(model.getAmount() * model.getPrice() + " ₪");
                holder.tv_amount.setText(model.getAmount() + "");

                productsRef.child(model.getType()).child(productPath).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()){
                                    productsInCartRef.child(productPath).removeValue();
                                    return;
                                }

                                Product newProduct = snapshot.getValue(Product.class);

                                if (!model.toString().equals(newProduct.toString())){
                                    productsInCartRef.child(productPath).child("price").setValue(newProduct.getPrice());
                                    productsInCartRef.child(productPath).child("stock").setValue(newProduct.getStock());
                                    productsInCartRef.child(productPath).child("type").setValue(newProduct.getType());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                break;
        }

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
        View view = LayoutInflater.from(parent.getContext()).inflate(mode, parent, false);

        return new ProductsAdapterFirebase.ProductViewHolderFirebase(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }

    public interface OnAddToCartListener{
        void addToCart(int position, Product product);
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