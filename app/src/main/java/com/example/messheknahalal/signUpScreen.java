package com.example.messheknahalal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Admin_screens.mainScreenAdmin;
import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.Objects.User;
import com.example.messheknahalal.User_screens.mainScreenUser;
import com.example.messheknahalal.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class signUpScreen extends AppCompatActivity {

    EditText et_name_signUp, et_last_name_signUp, et_phone_number_signUp,
            et_email_address_signUp, et_password_signUp, et_confirm_password_signUp, et_admin_code_signUp;

    Button btn_login2, btn_confirm;
    CheckBox cb_admin;
    ImageView iv_profile_pic;
    Intent intent;
    FirebaseFirestore fStore;
    StorageReference rStore;
    FirebaseAuth auth;
    DatabaseReference userRef, adminRef, personRef, adminCodeRef;

    boolean confirmation = true;   //todo - turns to true if user enter a code that sent to phone when register, otherwise cannot upload a pic

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();

        adminCodeRef = FirebaseDatabase.getInstance().getReference("AdminCode");
        userRef = FirebaseDatabase.getInstance().getReference("User");
        adminRef = FirebaseDatabase.getInstance().getReference("Admin");
        personRef = FirebaseDatabase.getInstance().getReference("Person");

        et_name_signUp = findViewById(R.id.sign_up_screen_et_name);
        et_last_name_signUp = findViewById(R.id.sign_up_screen_et_last_name);
        et_phone_number_signUp = findViewById(R.id.sign_up_screen_et_phone_number);
        et_email_address_signUp = findViewById(R.id.sign_up_screen_et_email_address);
        et_password_signUp = findViewById(R.id.sign_up_screen_et_password);
        et_confirm_password_signUp = findViewById(R.id.sign_up_screen_et_confirm_password);
        et_admin_code_signUp = findViewById(R.id.sign_up_screen_et_admin_code);
        cb_admin = findViewById(R.id.sign_up_screen_cb_admin);
        btn_login2 = findViewById(R.id.sign_up_screen_btn_login);
        btn_confirm = findViewById(R.id.sign_up_screen_btn_confirm);
        iv_profile_pic = findViewById(R.id.sign_up_screen_iv_pp);
        et_admin_code_signUp.setVisibility(View.INVISIBLE);


        fStore = FirebaseFirestore.getInstance();
        rStore = FirebaseStorage.getInstance().getReference();

        //to pick a profile picture
        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                //the email is not linked to any account
                String email = et_email_address_signUp.getText().toString();
                if(email.isEmpty()){
                    Utils.showAlertDialog("Error", "You can't pick a profile picture without an email", signUpScreen.this);
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Utils.showAlertDialog("Error", "This email address is not valid and you can't save a profile picture", signUpScreen.this);
                } else {
                    String emailPath = "Person_"+email.replace(".", "-");
                    personRef.child(emailPath).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            //if exists the dataSnapshot
                            if (ds.exists()) {
                                Utils.showAlertDialog("Error", "This email is already linked to another account\nIf you want to set a profile picture to it you can do it from the 'My Profile' screen", signUpScreen.this);
                            } else {
                                //open gallery
                                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(openGalleryIntent, 1000);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError dbe) {
                            Log.d("ERROR", dbe.getMessage());
                        }
                    });
                }
            }
        });

        cb_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the cell box is true or false
                if (cb_admin.isChecked()) {
                    et_admin_code_signUp.setVisibility(View.VISIBLE);
                } else {
                    et_admin_code_signUp.setVisibility(View.INVISIBLE);
                }
            }
        });

        btn_login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(signUpScreen.this, loginScreen.class);
                startActivity(intent);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ariel", "button clicked");

                String email = et_email_address_signUp.getText().toString();
                String password = et_password_signUp.getText().toString();
                String confirm_password = et_confirm_password_signUp.getText().toString();
                String code = et_admin_code_signUp.getText().toString();

                //checking if the all the fields are filled with information
                if (emptyET()) {
                    Utils.showAlertDialog("Empty fields", "Please finish to complete all the fields.", signUpScreen.this);
                }
                //validate email address
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Utils.showAlertDialog("Error", "This email address is not valid", signUpScreen.this);
                }
                //check that the password has at least 8 characters
                else if(password.length() < 6){
                    Utils.showAlertDialog("Error", "The password is too short!\nPlease choose a new password", signUpScreen.this);
                }
                //check if the password and the confirmed password are the same
                else if (!password.equals(confirm_password)) {
                    Utils.showAlertDialog("Error", "The passwords are different", signUpScreen.this);
                }
//                //check if the admin code is the same that in the db
//                else if(cb_admin.isChecked() && !code.equals("12345")){
//                    //the admin code is 12345
//                    Utils.showAlertDialog("Error", "The admin code is not valid", signUpScreen.this);
//                }
                //create a new person in the db
                else {
                    //if (cb_admin.isChecked()) {
                    //create a new admin in the db
                    //    createPerson("admin", email, password);
                    //} else {
                    //create a new user in the db
                    Log.d("ariel", "all inputs are right");

                    createPerson(code, email, password);

                }
            }

        });
    }

    @Override
    public void onBackPressed(){}

    private void createPerson(String code, String email, String password){

        adminCodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                //if exists the dataSnapshot
                if (ds.exists()) {

                    Log.d("ariel", "ds exists");


                    String codeDB = ds.getValue().toString();

                    if (cb_admin.isChecked() && codeDB.equals(code)) {

                        Log.d("ariel", "creating admin");

                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signUpScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    createPerson(true);
                                }
                                else {
                                    Utils.showAlertDialog("Error", "This email address is already linked to another account", signUpScreen.this);
                                }
                            }
                        });

                    }
                    else if (cb_admin.isChecked() && !codeDB.equals(code)) {
                        Utils.showAlertDialog("Error", "The admin code is not valid", signUpScreen.this);
                    }
                    else if(!cb_admin.isChecked()){

                        Log.d("ariel", "creating user");

                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signUpScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    createPerson(false);
                                }
                                else {
                                    Utils.showAlertDialog("Error", "This email address is already linked to another account", signUpScreen.this);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }

    public void createPerson(boolean isAdmin) {
//        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Log.d("ariel", "createPerson(isADmin)");

        String name = et_name_signUp.getText().toString();
        String last_name = et_last_name_signUp.getText().toString();
        String email = et_email_address_signUp.getText().toString();
        String phone = et_phone_number_signUp.getText().toString();

        name = Utils.capitalizeString(name);
        last_name = Utils.capitalizeString(last_name);

        Person person = new Person(name, last_name, email, phone, "");

        FirebaseMessaging.getInstance().subscribeToTopic("Notification_to_" + Utils.emailForFCM(email))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("ariel", "Subscribing to topic succeeded");

                        Uri imageUri = (Uri) iv_profile_pic.getTag();

                        if (imageUri == null) {
                            uploadPerson(person, isAdmin);
                        }
                        else {
                            uploadImageToFirebase(person, imageUri, isAdmin);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(signUpScreen.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
        });
    }

    public void uploadPerson(Person person, boolean isAdmin){

        if (isAdmin){
            person.setType("admin");

            Admin admin = new Admin(person.getName(), person.getLast_name(), person.getEmail(), person.getPhone(), person.getType(), et_admin_code_signUp.getText().toString());

            uploadAdmin(person, admin);
        }
        else {
            person.setType("user");

            User user = new User(person.getName(), person.getLast_name(), person.getEmail(), person.getPhone(), person.getType());

            uploadUser(person, user);
        }

    }

    public void uploadUser(@NonNull Person person, User user){
        String email = person.getEmail();

        personRef.child(Utils.emailToPersonPath(email)).setValue(person).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userRef.child(Utils.emailToUserPath(email)).setValue(user)
                                .addOnSuccessListener(
                                        unused1 -> snackBar("Account successfully created"))
                                .addOnFailureListener(
                                        e -> Toast.makeText(signUpScreen.this, "Registration failed", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(
                        e -> Toast.makeText(signUpScreen.this, "Registration failed", Toast.LENGTH_SHORT).show());
    }

    public void uploadAdmin(@NonNull Person person, Admin admin){
        String email = person.getEmail();

        personRef.child(Utils.emailToPersonPath(email)).setValue(person).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        adminRef.child(Utils.emailToAdminPath(email)).setValue(admin)
                                .addOnSuccessListener(
                                        unused1 -> snackBar("Account successfully created"))
                                .addOnFailureListener(
                                        e -> Toast.makeText(signUpScreen.this, "Registration failed", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(
                        e -> Toast.makeText(signUpScreen.this, "Registration failed", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                iv_profile_pic.setTag(imageUri);
                Glide.with(this).load(imageUri).centerCrop().into(iv_profile_pic);
            }
        }
    }

    private void uploadImageToFirebase(Person person, Uri imageUri, boolean isAdmin) {

        String emailProfile = Utils.emailToPath(et_email_address_signUp.getText().toString());

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
                        Glide.with(signUpScreen.this).load(uri).centerCrop().into(iv_profile_pic);
                        person.setPicture(uri.toString());
                        uploadPerson(person, isAdmin);
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
                Toast.makeText(signUpScreen.this, "FAILED!!!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void snackBar(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.sign_up_screen), message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Continue", new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(signUpScreen.this, cb_admin.isChecked() ? mainScreenAdmin.class : mainScreenUser.class);
                        Log.d("ariel", "cb_admin is " + cb_admin.isChecked());
                        startActivity(intent);
                    }
                });
        snackbar.show();
    }

    private boolean emptyET() {
        String name = et_name_signUp.getText().toString();
        String last_name = et_last_name_signUp.getText().toString();
        String email = et_email_address_signUp.getText().toString();
        String password = et_password_signUp.getText().toString();
        String confirm_password = et_confirm_password_signUp.getText().toString();
        String phone = et_phone_number_signUp.getText().toString();
        String admin_code;

        if (cb_admin.isChecked()) {
            admin_code = et_admin_code_signUp.getText().toString();
        } else {
            admin_code = "x";
        }
        return name.isEmpty() || last_name.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirm_password.isEmpty()
                || phone.isEmpty() || admin_code.isEmpty();
    }

}