package com.example.messheknahalal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messheknahalal.Admin_screens.mainScreenAdmin;
import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.User_screens.mainScreenUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class loginScreen extends AppCompatActivity{

    EditText et_email_login, et_password_login, et_email_dialog;
    Button btn_login, btn_sign_up, btn_confirm_dialog, btn_back_dialog;
    Intent intent;
    ImageView iv_forgot_password;
    TextView tv_forgot_password;
    ProgressDialog progressDialog;
    Dialog d;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Person");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_confirm_dialog = findViewById(R.id.dialog_rp_btn_confirm);
        btn_back_dialog = findViewById(R.id.dialog_rp_btn_back);
        et_email_dialog = findViewById(R.id.dialog_rp_et_email);

        et_email_login = findViewById(R.id.login_screen_et_email);
        et_password_login = findViewById(R.id.login_screen_et_password);
        btn_login = findViewById(R.id.login_screen_btn_login);
        btn_sign_up = findViewById(R.id.login_screen_btn_sign_up);
        iv_forgot_password = findViewById(R.id.login_screen_iv_forgot_password);
        tv_forgot_password = findViewById(R.id.login_screen_tv_forgot_password);

        progressDialog = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email_login.getText().toString();
                String password = et_password_login.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showAlertDialog("Empty fields", "Please finish to complete all the fields.");
                } else {
                    progressDialog.setMessage("Login Please Wait...");
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(loginScreen.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //check if the person is admin or user
                                        checkPersonType(email);
                                    } else {
                                        showAlertDialog("Error", "Wrong email or password!\nPlease try again!");
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }
            }
        });

        iv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createForgotPassDialog();
            }
        });


        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(loginScreen.this, signUpScreen.class);
                startActivity(intent);
            }
        });
    }

    public void showAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(loginScreen.this);
        builder.setTitle(title);
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create();
        dialog.setMessage(message);
        dialog.show();
    }

    public void createForgotPassDialog() {
        d = new Dialog(this);
        d.setContentView(R.layout.reset_password_dialog);
        d.setTitle("Reset Password");
        d.setCancelable(true);
        et_email_dialog = d.findViewById(R.id.dialog_rp_et_email);
        btn_confirm_dialog = d.findViewById(R.id.dialog_rp_btn_confirm);
        btn_back_dialog = d.findViewById(R.id.dialog_rp_btn_back);
        d.show();

        btn_back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        btn_confirm_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email_dialog.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                if (email.isEmpty()) {
                    Toast.makeText(loginScreen.this, "Please fill the field", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(loginScreen.this, "This email address is not valid", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                snackBar("Please verify your email inbox to reset your password");
                                d.dismiss();
                            } else {
                                Toast.makeText(loginScreen.this, "Sorry, this email address is not linked to an account.\n Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    public void snackBar(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.activity_login), message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK",new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                    }
                });
        snackbar.show();
    }

    public void checkPersonType(String email){
        String personPath = "Person_"+email.replace(".","-");
        personRef.child(personPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                //if exists the dataSnapshot
                if(ds.exists()){
                    String type = ds.getValue(Person.class).getType().toString();
                    if(type.equals("user")){
                        intent = new Intent(getApplicationContext(), mainScreenUser.class);
                    }else{
                        intent = new Intent(getApplicationContext(), mainScreenAdmin.class);
                    }
                    startActivity(intent);
                }else {
                    Toast.makeText(loginScreen.this, "checkPersonType: not found person", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }
}
