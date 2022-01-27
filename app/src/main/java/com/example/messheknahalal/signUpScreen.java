package com.example.messheknahalal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.Objects.User;
import com.example.messheknahalal.User_screens.mainScreenUser;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
    DatabaseReference userRef, adminRef, personRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();

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
                    showAlertDialog("Error", "You can't pick a profile picture without an email");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showAlertDialog("Error", "This email address is not valid and you can't save a profile picture");
                } else {
                    String emailPath = "Person_"+email.replace(".", "-");
                    personRef.child(emailPath).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            //if exists the dataSnapshot
                            if (ds.exists()) {
                                showAlertDialog("Error", "This email is already linked to another account\nIf you want to set a profile picture to it you can do it from the 'My Profile' screen");
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
                String email = et_email_address_signUp.getText().toString();
                String password = et_password_signUp.getText().toString();
                String confirm_password = et_confirm_password_signUp.getText().toString();
                String code = et_admin_code_signUp.getText().toString();

                //checking if the all the fields are filled with information
                if (emptyET()) {
                    showAlertDialog("Empty fields", "Please finish to complete all the fields.");
                }
                //validate email address
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    showAlertDialog("Error", "This email address is not valid");
                }
                //check that the password has at least 8 characters
                else if(password.length()<6){
                    showAlertDialog("Error", "The password is too short!\nPlease choose a new password");
                }
                //check if the password and the confirmed password are the same
                else if (!password.equals(confirm_password)) {
                    showAlertDialog("Error", "The passwords are different");
                }
                //check if the admin code is the same that in the db
                else if(cb_admin.isChecked() && !code.equals("12345")){
                    //the admin code is 12345
                    showAlertDialog("Error", "The admin code is not valid");
                }
                //create a new person in the db
                else {
                    if (cb_admin.isChecked()) {
                        //create a new admin in the db
                        createPerson("admin", email, password);
                    } else {
                        //create a new user in the db
                        createPerson("user", email, password);

                    }
                }
            }
        });
    }


    private void createPerson(String type, String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signUpScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (type.equals("admin")) {
                                createAdmin();
                            } else {
                                createUser();
                            }
                            snackBar("Account successfully created");
                        } else {
                            showAlertDialog("Error", "This email address is already linked to another account");
                        }
                    }
                });
    }

    private void createAdmin(){
        String name = et_name_signUp.getText().toString();
        String last_name = et_last_name_signUp.getText().toString();
        String email = et_email_address_signUp.getText().toString();
        String phone = et_phone_number_signUp.getText().toString();
        String code = et_admin_code_signUp.getText().toString();

        name = capitalizeString(name);
        last_name = capitalizeString(last_name);

        Admin admin = new Admin(name, last_name, email, phone, "admin", code);
        Person person = new Person(name, last_name, email, phone, "admin");
        email = email.replace(".","-");
        adminRef.child("Admin_"+email).setValue(admin);
        personRef.child("Person_"+email).setValue(person);
    }

    private void createUser(){
        String name = et_name_signUp.getText().toString();
        String last_name = et_last_name_signUp.getText().toString();
        String email = et_email_address_signUp.getText().toString();
        String phone = et_phone_number_signUp.getText().toString();

        name = capitalizeString(name);
        last_name = capitalizeString(last_name);

        User user = new User(name, last_name, email, phone, "user", "");
        Person person = new Person(name, last_name, email, phone, "user");
        email = email.replace(".","-");
        userRef.child("User_"+email).setValue(user);
        personRef.child("Person_"+email).setValue(person);
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

        String emailProfile = et_email_address_signUp.getText().toString().replace(".","-");

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
                        Glide.with(getApplicationContext()).load(uri).centerCrop().into(iv_profile_pic);
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
        .setAction("Continue",new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(signUpScreen.this, mainScreenUser.class);
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

    private void showAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(signUpScreen.this);
        builder.setTitle(title);
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create();
        dialog.setMessage(message);
        dialog.show();
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
