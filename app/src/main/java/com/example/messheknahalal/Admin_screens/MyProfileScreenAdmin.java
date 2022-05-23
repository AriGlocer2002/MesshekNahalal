package com.example.messheknahalal.Admin_screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.models.Admin;
import com.example.messheknahalal.models.Person;
import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.example.messheknahalal.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

public class MyProfileScreenAdmin extends SuperActivityWithNavigationDrawer {

    Intent intent;
    StorageReference rStore;
    DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("Admin"),
            personRef = FirebaseDatabase.getInstance().getReference().child("Person");

    FirebaseAuth auth = FirebaseAuth.getInstance();

    RoundedImageView screen_profile_img;
    Button btn_update_data, btn_reset_pass;
    EditText et_name, et_last_name, et_email, et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_admin);


        FirebaseUser user = auth.getCurrentUser();
        String adminEmail = user.getEmail();
        rStore = FirebaseStorage.getInstance().getReference();

        //setting profile image in the screen
        screen_profile_img = findViewById(R.id.my_profile_admin_iv_pp);

        //changing profile picture
        screen_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        //show the data of the Admin
        showData(adminEmail);

        btn_reset_pass = findViewById(R.id.my_profile_admin_btn_reset_pass);
        btn_update_data =findViewById(R.id.my_profile_admin_btn_update);

        btn_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(adminEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            snackBar("Please verify your email inbox to reset your password");
                        }
                    }
                });
            }
        });

        et_phone = findViewById(R.id.my_profile_admin_et_phone_number);
        et_name = findViewById(R.id.my_profile_admin_et_name);
        et_last_name = findViewById(R.id.my_profile_admin_et_last_name);

        btn_update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check fields
                if (et_name.getText().toString().isEmpty() ||
                        et_last_name.getText().toString().isEmpty() ||
                        et_phone.getText().toString().isEmpty()) {

                    Utils.showAlertDialog("Empty fields", "Please finish to complete all the fields.", MyProfileScreenAdmin.this);
                }else {
                    String name = et_name.getText().toString();
                    String last_name = et_last_name.getText().toString();
                    String phone = et_phone.getText().toString();

                    name = Utils.capitalizeString(name);
                    last_name = Utils.capitalizeString(last_name);

                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    Admin updatedAdmin = new Admin(name,last_name , email, phone, "admin", "");
                    updateAmin(updatedAdmin);
                }
            }
        });

        initializeNavigationDrawer(true);

    }

    public void updateAmin(Admin updatedAdmin) {
        Uri imageUri = (Uri) screen_profile_img.getTag();
        if (imageUri == null){
            uploadAdminToFirebase(updatedAdmin);
        }
        else {
            uploadImageToFirebase(imageUri, updatedAdmin);
        }
    }

    public void uploadAdminToFirebase(@NonNull Admin admin){
        String adminPath = Utils.emailToAdminPath(admin.getEmail());
        String personPath = Utils.emailToPersonPath(admin.getEmail());

        personRef.child(personPath).setValue(admin).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        adminRef.child(adminPath).setValue(admin).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MyProfileScreenAdmin.this,
                                                "Admin's data was successfully updated",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MyProfileScreenAdmin.this, mainScreenAdmin.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MyProfileScreenAdmin.this, "Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyProfileScreenAdmin.this, "Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //check size of img
                Glide.with(this).load(imageUri).centerCrop().into(screen_profile_img);
                screen_profile_img.setTag(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri, Admin updatedAdmin) {
        String emailProfile = et_email.getText().toString().replace(".","-");

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.loading_dialog);
d.setCanceledOnTouchOutside(false);
        d.show();

        StorageReference fileRef = rStore.child("profiles/pp_"+emailProfile+".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).centerCrop().into(screen_profile_img);

                        String picture = uri.toString();
                        updatedAdmin.setPicture(picture);
                        uploadAdminToFirebase(updatedAdmin);

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
                Toast.makeText(MyProfileScreenAdmin.this, "FAILED!!!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void snackBar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showData(@NonNull String email){
        et_email = findViewById(R.id.my_profile_admin_et_email_address);
        et_phone = findViewById(R.id.my_profile_admin_et_phone_number);
        et_name = findViewById(R.id.my_profile_admin_et_name);
        et_last_name = findViewById(R.id.my_profile_admin_et_last_name);
        String personPath = "Person_"+email.replace(".","-");

        personRef.child(personPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                //if exists the dataSnapshot
                if (ds.exists()) {
                    Person p = ds.getValue(Person.class);
                    String name = p.getName();
                    String last_name = p.getLast_name();
                    String phone = p.getPhone();
                    String picture = p.getPicture();

                    et_name.setText(name);
                    et_last_name.setText(last_name);
                    et_phone.setText(phone);
                    et_email.setText(email);
                    et_email.setEnabled(false);

                    if (picture != null && !picture.isEmpty()){
                        Glide.with(getApplicationContext()).load(picture).centerCrop().into(screen_profile_img);
                    }
                    else {
                        Glide.with(getApplicationContext()).load(R.drawable.sample_profile).centerCrop().into(screen_profile_img);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }
}