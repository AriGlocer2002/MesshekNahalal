package com.example.messheknahalal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.messheknahalal.User_screens.mainScreen;
import com.example.messheknahalal.Utils.DataBaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class signUpScreen extends AppCompatActivity {

    EditText et_name_signUp, et_last_name_signUp, et_id_number_signUp, et_phone_number_signUp,
            et_email_address_signUp, et_password_signUp, et_confirm_password_signUp, et_admin_code_signUp;
    Button btn_login2, btn_confirm;
    CheckBox cb_admin;
    ImageView iv_profile_pic;
    Intent intent;
    String uid;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        et_name_signUp = findViewById(R.id.et_name_signUp);
        et_last_name_signUp = findViewById(R.id.et_last_name_signUp);
        et_id_number_signUp = findViewById(R.id.et_id_number_signUp);
        et_phone_number_signUp = findViewById(R.id.et_phone_number_signUp);
        et_email_address_signUp = findViewById(R.id.et_email_address_signUp);
        et_password_signUp = findViewById(R.id.et_password_signUp);
        et_confirm_password_signUp = findViewById(R.id.et_confirm_password_signUp);
        et_admin_code_signUp = findViewById(R.id.et_admin_code_signUp);

        cb_admin = findViewById(R.id.cb_admin);

        btn_login2 = findViewById(R.id.btn_login2);
        btn_confirm = findViewById(R.id.btn_confirm);

        iv_profile_pic = findViewById(R.id.iv_profile_pic);

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


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String femail = et_email_address_signUp.getText().toString();
                Log.d("ariel", femail);
                String fpassword = et_password_signUp.getText().toString();
                Log.d("ariel", fpassword);


                firebaseAuth.createUserWithEmailAndPassword(femail, fpassword)
                        .addOnCompleteListener(signUpScreen.this, task -> {
                            if (task.isSuccessful()) {
                                Log.d("ariel", "success");
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                uid = user.getUid().toString();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("ariel", task.getException().toString());
                            }
                        });


                //check that the email is a real email
                //check if the id is in the db
                //check if the email is in the db
                //code is valid

                String name = et_name_signUp.getText().toString();
                String last_name = et_last_name_signUp.getText().toString();
                String email = et_email_address_signUp.getText().toString();
                String password = et_password_signUp.getText().toString();
                String confirm_password = et_confirm_password_signUp.getText().toString();
                String id = et_id_number_signUp.getText().toString();
                String phone = et_phone_number_signUp.getText().toString();
                String code = et_admin_code_signUp.getText().toString();


                //checking if the all the fields are filled with information
                if (emptyET()) {
                    Toast.makeText(signUpScreen.this, "Please complete all the fields", Toast.LENGTH_LONG).show();
                }

                //check if the password and the confirmed password are the same
                else if (!password.equals(confirm_password)) {
                    Toast.makeText(signUpScreen.this, "The passwords are different", Toast.LENGTH_LONG).show();
                }

                //create a new person in the db
                else {
                    DataBaseHelper dbp = new DataBaseHelper("Person");
                    DataBaseHelper db;

                    Person p = new Person(id, name, last_name, email, password, phone);

                    //String keyPerson = "Person_" + id;
                    String keyPerson = uid;
                    dbp.getRef().child(keyPerson).setValue(p);


                    if (cb_admin.isChecked()) {
                        //create a new admin in the db
                        db = new DataBaseHelper("Admin");

                        Admin admin = new Admin(id, name, last_name, email, password, phone, code);
                        //String keyAdmin = "Admin_" + id;
                        String keyAdmin = uid;
                        db.getRef().child(keyAdmin).child("code").setValue(admin.getCode());
                        db.getRef().child(keyAdmin).child("id").setValue(admin.getId());

                    } else {
                        //create a new user in the db
                        db = new DataBaseHelper("User");

                        User user = new User(id, name, last_name, email, password, phone, "");
                        //String keyUser = "User_" + id;
                        String keyUser = uid;
                        db.getRef().child(keyUser).child("id").setValue(user.getId());
                        db.getRef().child(keyUser).child("last_login").setValue(user.getLast_login());
                    }

                    intent = new Intent(signUpScreen.this, mainScreen.class);

                    startActivity(intent);
                }

            }
        });


    }

    public boolean emptyET() {

        String name = et_name_signUp.getText().toString();
        String last_name = et_last_name_signUp.getText().toString();
        String email = et_email_address_signUp.getText().toString();
        String password = et_password_signUp.getText().toString();
        String confirm_password = et_confirm_password_signUp.getText().toString();
        String id = et_id_number_signUp.getText().toString();
        String phone = et_phone_number_signUp.getText().toString();
        String admin_code;

        if (cb_admin.isChecked()) {
            admin_code = et_admin_code_signUp.getText().toString();
        } else {
            admin_code = "x";
        }

        return name.isEmpty() || last_name.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirm_password.isEmpty()
                || id.isEmpty() || phone.isEmpty() || admin_code.isEmpty();
    }
}
