package com.example.messheknahalal.Admin_screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Admin_screens.MyProfileScreenAdmin;
import com.example.messheknahalal.Admin_screens.mainScreenAdmin;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.loginScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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

public class MyProfileScreenAdmin extends AppCompatActivity {

    Intent intent;
    StorageReference rStore;
    DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("Admin"),
            personRef = FirebaseDatabase.getInstance().getReference().child("Person");
    FirebaseAuth auth = FirebaseAuth.getInstance();

    TextView nd_tv_name, nd_tv_email;
    ImageView nv_profile_img, screen_profile_img;
    DrawerLayout drawerMenu;
    NavigationView nav_view;
    Button btn_update_data, btn_reset_pass;
    EditText et_name, et_last_name, et_email, et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_admin);


        FirebaseUser user = auth.getCurrentUser();
        String adminEmail = user.getEmail();
        String adminPath = "Admin_"+adminEmail.replace(".","-");
        String personPath = "Person_"+adminEmail.replace(".","-");
        rStore = FirebaseStorage.getInstance().getReference();

        //setting profile information in the navigation drawer
        NavigationView navigationView = findViewById(R.id.my_profile_admin_nav_view);
        View headerView = navigationView.getHeaderView(0);

        loadDrawerNameAndLastName(personPath);

        TextView navAdminName = headerView.findViewById(R.id.header_nd_tv_name);
        TextView email = headerView.findViewById(R.id.header_nd_tv_email);
        email.setText(user.getEmail());

        //setting profile image in the navigation drawer
        //setting profile image in the screen
        screen_profile_img = findViewById(R.id.my_profile_admin_iv_pp);
        nv_profile_img = headerView.findViewById(R.id.header_nd_iv_pp);
        StorageReference profileRef = rStore.child("profiles/pp_"+adminEmail.replace(".","-")+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MyProfileScreenAdmin.this).load(uri).centerCrop().into(screen_profile_img);
                Glide.with(MyProfileScreenAdmin.this).load(uri).centerCrop().into(nv_profile_img);
            }
        });

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

                    adminRef.child(adminPath).child("name").setValue(name);
                    adminRef.child(adminPath).child("last_name").setValue(last_name);
                    adminRef.child(adminPath).child("phone").setValue(phone);

                    personRef.child(personPath).child("name").setValue(name);
                    personRef.child(personPath).child("last_name").setValue(last_name);
                    personRef.child(personPath).child("phone").setValue(phone);

                    intent = new Intent(MyProfileScreenAdmin.this, mainScreenAdmin.class);
                    startActivity(intent);
                }
            }
        });

        //Navigation menu
        nd_tv_name = findViewById(R.id.header_nd_tv_name);
        nd_tv_email = findViewById(R.id.header_nd_tv_email);

        drawerMenu = findViewById(R.id.my_profile_admin_drawer_layout);
        nav_view = findViewById(R.id.my_profile_admin_nav_view);

        Toolbar toolbar = findViewById(R.id.my_profile_admin_toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerMenu, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerMenu.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        getSupportActionBar().setTitle("");
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_item:
                        intent = new Intent(MyProfileScreenAdmin.this, mainScreenAdmin.class);
                        startActivity(intent);
                        break;

//                    case R.id.orders_item:
//
//                        break;
//
//                    case R.id.users_item:
//
//                        break;
//
//                    case R.id.products_item:
//
//                        break;

                    case R.id.logOut_item:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(MyProfileScreenAdmin.this, loginScreen.class);
                        startActivity(intent);
                        break;
                }
                return false;
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
                Toast.makeText(MyProfileScreenAdmin.this, "FAILED!!!!", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void snackBar(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.my_profile_admin_drawer_layout), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    public void showData(String email){
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

    public void loadDrawerNameAndLastName(String path){
        personRef.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person p= snapshot.getValue(Person.class);
                String name = p.getName();
                String last_name = p.getLast_name();
                String fullName = name+" "+last_name;

                NavigationView navigationView = findViewById(R.id.my_profile_admin_nav_view);
                View headerView = navigationView.getHeaderView(0);

                TextView navAdminName = headerView.findViewById(R.id.header_nd_tv_name);
                navAdminName.setText(fullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }
}
