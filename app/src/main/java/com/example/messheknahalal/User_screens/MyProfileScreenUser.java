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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.Person;
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

public class MyProfileScreenUser extends SuperActivityWithNavigationDrawer {

    Intent intent;
    StorageReference rStore;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User"),
            personRef = FirebaseDatabase.getInstance().getReference().child("Person");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    ImageView screen_profile_img;
    Button btn_update_data, btn_reset_pass;
    EditText et_name, et_last_name, et_email, et_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_user);

        FirebaseUser user = auth.getCurrentUser();
        String userEmail = user.getEmail();
        String userPath = "User_"+userEmail.replace(".","-");
        String personPath = "Person_"+userEmail.replace(".","-");
        rStore = FirebaseStorage.getInstance().getReference();

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
                }else {
                    String name = et_name.getText().toString();
                    String last_name = et_last_name.getText().toString();
                    String phone = et_phone.getText().toString();

                    name = Utils.capitalizeString(name);
                    last_name = Utils.capitalizeString(last_name);

                    userRef.child(userPath).child("name").setValue(name);
                    userRef.child(userPath).child("last_name").setValue(last_name);
                    userRef.child(userPath).child("phone").setValue(phone);

                    personRef.child(personPath).child("name").setValue(name);
                    personRef.child(personPath).child("last_name").setValue(last_name);
                    personRef.child(personPath).child("phone").setValue(phone);

                    intent = new Intent(MyProfileScreenUser.this, mainScreenUser.class);
                    startActivity(intent);
                }
            }
        });

        initializeNavigationDrawer(false);
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
        String emailProfile = et_email.getText().toString().replace(".","-");

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.loading_dialog);
        d.show();

        StorageReference fileRef = rStore.child("profiles/pp_"+emailProfile+".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).centerCrop().into(screen_profile_img);
                        Glide.with(getApplicationContext()).load(uri).centerCrop().into(nv_profile_img);
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

                    et_name.setText(name);
                    et_last_name.setText(last_name);
                    et_phone.setText(phone);
                    et_email.setText(email);
                    et_email.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }

}