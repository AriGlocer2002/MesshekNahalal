package com.example.messheknahalal.User_screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.models.Person;
import com.example.messheknahalal.models.User;
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

public class MyProfileScreenUser extends SuperActivityWithNavigationDrawer {

    final StorageReference rStore = FirebaseStorage.getInstance().getReference();
    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
    final DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Person");

    final FirebaseAuth auth = FirebaseAuth.getInstance();
    RoundedImageView screen_profile_img;
    Button btn_update_data, btn_reset_pass;
    EditText et_name, et_last_name, et_email, et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_user);

        FirebaseUser user = auth.getCurrentUser();
        String userEmail = user.getEmail();

        //setting profile image in the screen
        screen_profile_img = findViewById(R.id.my_profile_user_iv_pp);

        //changing profile picture
        screen_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        screen_profile_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                screen_profile_img.setImageResource(R.drawable.sample_profile);
                screen_profile_img.setTag(null);
                return false;
            }
        });

        //show the data of the user
        showData(userEmail);

        btn_reset_pass = findViewById(R.id.my_profile_user_btn_reset_pass);
        btn_update_data =findViewById(R.id.my_profile_user_btn_update);

        btn_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            snackBar("Please verify your email inbox to reset your password");
                        }
                    }
                });
            }
        });

        et_phone = findViewById(R.id.my_profile_user_et_phone_number);
        et_name = findViewById(R.id.my_profile_user_et_name);
        et_last_name = findViewById(R.id.my_profile_user_et_last_name);

        btn_update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check fields
                if (et_name.getText().toString().isEmpty() ||
                        et_last_name.getText().toString().isEmpty() ||
                        et_phone.getText().toString().isEmpty()) {

                    Utils.showAlertDialog("Empty fields", "Please finish to complete all the fields.", MyProfileScreenUser.this);
                }
                else {
                    String name = et_name.getText().toString();
                    String last_name = et_last_name.getText().toString();
                    String phone = et_phone.getText().toString();

                    name = Utils.capitalizeString(name);
                    last_name = Utils.capitalizeString(last_name);

                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    User updatedUser = new User(name,last_name , email, phone, "user");
                    updateUser(updatedUser);
                }
            }
        });

        initializeNavigationDrawer(false);
    }

    public void updateUser(@NonNull User updatedUser) {
        Uri imageUri = (Uri) screen_profile_img.getTag();
        if (imageUri == null){
            uploadUserToFirebase(updatedUser);
        }
        else {
            uploadImageToFirebase(imageUri, updatedUser);
        }
    }

    public void uploadUserToFirebase(@NonNull User user){
        String userPath = Utils.emailToUserPath(user.getEmail());
        String personPath = Utils.emailToPersonPath(user.getEmail());

        personRef.child(personPath).setValue(user).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userRef.child(userPath).setValue(user).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(MyProfileScreenUser.this, "User's data was successfully updated", Toast.LENGTH_SHORT).show();
                                        Log.d("ariel", user.getType());
                                        startActivity(new Intent(MyProfileScreenUser.this, mainScreenUser.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MyProfileScreenUser.this, "Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyProfileScreenUser.this, "Failed",
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
                Glide.with(this).load(imageUri).centerCrop().into(screen_profile_img);
                screen_profile_img.setTag(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri, User updatedUser) {
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
                        Glide.with(MyProfileScreenUser.this).load(uri).centerCrop().into(screen_profile_img);

                        String picture = uri.toString();
                        updatedUser.setPicture(picture);
                        uploadUserToFirebase(updatedUser);

                        d.dismiss();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (double) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());

                ProgressBar progressBar = d.findViewById(R.id.progressBar_loading_dialog);
                TextView textViewProgress = d.findViewById(R.id.textPercent_loading_dialog);
                progressBar.setProgress((int)progress);
                textViewProgress.setText(progress+"%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyProfileScreenUser.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void snackBar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showData(@NonNull String email){
        et_email = findViewById(R.id.my_profile_user_et_email_address);
        et_phone = findViewById(R.id.my_profile_user_et_phone_number);
        et_name = findViewById(R.id.my_profile_user_et_name);
        et_last_name = findViewById(R.id.my_profile_user_et_last_name);
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