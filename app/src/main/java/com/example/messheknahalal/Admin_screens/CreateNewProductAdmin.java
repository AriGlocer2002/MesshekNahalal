package com.example.messheknahalal.Admin_screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.Objects.Product;
import com.example.messheknahalal.Objects.User;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateNewProductAdmin extends AppCompatActivity {

    Spinner spinner;
    ImageView product_img;
    Button btn_back, btn_confirm;
    TextInputEditText tie_name, tie_stock, tie_price;

    Intent intent;
    DatabaseReference productRef;
    StorageReference rStore = FirebaseStorage.getInstance().getReference();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_product_admin);

        spinner = findViewById(R.id.create_new_product_admin_spinner_type);

        tie_name = findViewById(R.id.create_new_product_admin_tie_product_name);
        tie_stock = findViewById(R.id.create_new_product_admin_tie_product_stock_amount);
        tie_price = findViewById(R.id.create_new_product_admin_tie_product_price);

        btn_back = findViewById(R.id.create_new_product_admin_btn_back);
        btn_confirm = findViewById(R.id.create_new_product_admin_btn_confirm);

        product_img = findViewById(R.id.create_new_product_admin_iv_upload_image);


        //setting button back
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //changing product picture
        product_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                String productName = tie_name.getText().toString();
                if (productName.isEmpty()) {
                    Utils.showAlertDialog("Upload Image", "Please insert a product name before uploading image", CreateNewProductAdmin.this);
                }
                String productType = (String)spinner.getSelectedItem();
                if(spinner != null && spinner.getSelectedItem() !=null && !productType.isEmpty()) {
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(openGalleryIntent, 1000);
                } else {
                    Utils.showAlertDialog("Upload Image", "Please select a product type before uploading image", CreateNewProductAdmin.this);
                }
            }
        });

        //setting spinner information
        String[] products_types = {"","Vegetable","Fruit","Shelf","Other"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.background_spinner1, products_types);
        adapter.setDropDownViewResource(R.layout.background_dropdown_spinner_items);

        spinner.setAdapter(adapter);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productType = (String)spinner.getSelectedItem();
                String productName = tie_name.getText().toString();
                String productStock = tie_stock.getText().toString();
                String productPrice = tie_price.getText().toString();
                if (productName.isEmpty()) {
                    Utils.showAlertDialog("Create Product", "Please insert the name of the product", CreateNewProductAdmin.this);
                }
                if(productStock.isEmpty()) {
                    Utils.showAlertDialog("Create Product", "Please insert an amount in stock for the product", CreateNewProductAdmin.this);
                }
                if(productPrice.isEmpty()){
                    Utils.showAlertDialog("Create Product", "Please insert a price for the product", CreateNewProductAdmin.this);
                }
                if(spinner != null && spinner.getSelectedItem() !=null && !productType.isEmpty()) {
                    createProduct();
                } else {
                    Utils.showAlertDialog("Create Product", "Please select a product type", CreateNewProductAdmin.this);
                }
            }
        });

    }

    private void createProduct(){
        spinner = findViewById(R.id.create_new_product_admin_spinner_type);
        tie_name = findViewById(R.id.create_new_product_admin_tie_product_name);
        tie_stock = findViewById(R.id.create_new_product_admin_tie_product_stock_amount);
        tie_price = findViewById(R.id.create_new_product_admin_tie_product_price);
        product_img = findViewById(R.id.create_new_product_admin_iv_upload_image);

        String productType = (String)spinner.getSelectedItem();
        String productName = tie_name.getText().toString();
        String productStock = tie_stock.getText().toString();
        String productPrice = tie_price.getText().toString();

        Product p = new Product(productType, productName, productStock, productPrice);

        productName = productName.replace(" ", "-");
        productName = productName.replace(".", "-");

        productRef = FirebaseDatabase.getInstance().getReference().child("Product").child(productType);
        productRef.child("product_"+productName).setValue(p);

        Toast.makeText(CreateNewProductAdmin.this, "The product has been created successfully", Toast.LENGTH_SHORT).show();

        tie_name.setText("");
        tie_stock.setText("");
        tie_price.setText("");
        spinner.setSelection(0);

        product_img.setImageResource(R.drawable.upload_image_img);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //check size of img
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //Checking product name format
        String productName = tie_name.getText().toString().replace(" ","-");
        productName = productName.replace(".","-");

        String productType = (String)spinner.getSelectedItem();

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.loading_dialog);
        d.show();

        StorageReference fileRef = rStore.child("products/"+productType+"/imgProduct_"+productName+".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).centerCrop().into(product_img);
                        d.dismiss();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());

                ProgressBar progressBar = d.findViewById(R.id.progressBar_loading_dialog);
                TextView textViewProgress = d.findViewById(R.id.textPercent_loading_dialog);
                progressBar.setProgress((int)progress);
                textViewProgress.setText(progress+"%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNewProductAdmin.this, "FAILED!!!!", Toast.LENGTH_LONG).show();

            }
        });
    }

}
