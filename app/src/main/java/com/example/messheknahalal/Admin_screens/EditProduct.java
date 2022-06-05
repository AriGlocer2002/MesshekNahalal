package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.models.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class EditProduct extends CreateNewProductAdmin {

    DatabaseReference oldProductRef = FirebaseDatabase.getInstance().getReference("Product");
    Product oldProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent received = getIntent();
        oldProduct = (Product) received.getSerializableExtra("product");

        String oldProductPath = Utils.productNameToPath(oldProduct.getName());
        oldProductRef = oldProductRef.child(oldProduct.getType()).child(oldProductPath);

        setProductData(oldProduct);
    }

    private void setProductData(@NonNull Product product) {
        tie_name.setText(product.getName());
        tie_price.setText(product.getPrice() + "");
        tie_stock.setText(product.getStock() + "");
        cb_countable.setChecked(product.isCountable());

        btn_confirm.setText("Update");

        if (product.getPicture() != null && !product.getPicture().isEmpty()){
            Glide.with(this).load(product.getPicture()).centerCrop().into(product_img);
        }
        else{
            Glide.with(this).load(R.drawable.default_product_pic_rv).centerCrop().into(product_img);
        }

        List<String> types = Arrays.asList(products_types);

        int selection = types.indexOf(product.getType());

        spinner.setSelection(selection);

        TextView create_new_product_admin_title = findViewById(R.id.create_new_product_admin_title);
        create_new_product_admin_title.setText("Edit Product");
    }

    @Override
    public void uploadProduct(Uri imageUri, @NonNull Product product) {
        if(imageUri == null){
            product.setPicture(oldProduct.getPicture());
            uploadProductToFirebase(product);
        }
        else {
            uploadImageToFirebase(imageUri, product);
        }
    }

    @Override
    public void uploadProductToFirebase(@NonNull Product product) {
        String productPath = Utils.productNameToPath(product.getName());

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product")
                .child(product.getType()).child(productPath);

        oldProductRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                productRef.setValue(product).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "The product has been edited successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), ProductsRecycleViewScreenAdmin.class));
                                        finish();
                                    }
                                })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "The product editing failed", Toast.LENGTH_SHORT).show());
            }
        })
        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Editing product failed", Toast.LENGTH_SHORT).show());
    }
}
