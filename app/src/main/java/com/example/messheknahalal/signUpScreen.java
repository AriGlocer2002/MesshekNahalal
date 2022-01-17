package com.example.messheknahalal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUpScreen extends AppCompatActivity {

    EditText et_name_signUp, et_last_name_signUp, et_phone_number_signUp,
            et_email_address_signUp, et_password_signUp, et_confirm_password_signUp, et_admin_code_signUp;
    Button btn_login2, btn_confirm;
    CheckBox cb_admin;
    ImageView iv_profile_pic;
    Intent intent;
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
                else if(password.length()<8){
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

//    private void createPerson(String type, String email, String password){
//        personRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot ds) {
//                if(ds.exists()) {
//                    Boolean notExist = true;
//                    for (DataSnapshot d : ds.getChildren()) {
//                        Person p = d.getValue(Person.class);
//                        if (p.getEmail().equals(email)) {
//                            showAlertDialog("Error", "This email address is already connected to another account");
//                            notExist = false;
//                        }
//                    }
//                    if (notExist == true) {
//                        //create an account
//                        if (type.equals("admin")) {
//                            createAdmin();
//                        } else {
//                            createUser();
//                        }
//                        //FireBaseAuth
//                        auth.createUserWithEmailAndPassword(email, password).
//                                addOnCompleteListener(signUpScreen.this, new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(Task<AuthResult> task) {
//                                        if (task.isSuccessful()) {
//                                            snackBar("Account successfully created");
//                                            Toast.makeText(signUpScreen.this, "Shopping 1", Toast.LENGTH_SHORT).show();
//                                            intent = new Intent(getApplicationContext(), loginScreen.class);
//                                            startActivity(intent);
//                                        } else {
//                                            Toast.makeText(signUpScreen.this, "Firebase Auth error", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError dbe) {
//                Log.d("ERROR", dbe.getMessage());
//            }
//        });
//    }

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
                            Toast.makeText(signUpScreen.this, "Shopping 1", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getApplicationContext(), loginScreen.class);
                            startActivity(intent);
                        } else {
                            showAlertDialog("Error", "This email address is already connected to another account");
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

        User user = new User(name, last_name, email, phone, "user", "");
        String date = user.getCurrentDate();
        user.setLast_login(date);
        Person person = new Person(name, last_name, email, phone, "user");
        email = email.replace(".","-");
        userRef.child("User_"+email).setValue(user);
        personRef.child("Person_"+email).setValue(person);
    }

    public void snackBar(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.sign_up_screen), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public boolean emptyET() {
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
}
