package com.example.messheknahalal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.User_screens.mainScreenUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginScreen extends AppCompatActivity{

    EditText et_email_login, et_password_login;
    Button btn_login1, btn_sign_up;
    Intent intent;
    ImageView iv_newPassword;
    TextView tv_newPassword;
    ProgressDialog progressDialog;

    FirebaseAuth auth;
    DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Person");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        et_email_login = findViewById(R.id.et_email_login);
        et_password_login = findViewById(R.id.et_password_login);
        btn_login1 = findViewById(R.id.btn_login1);
        btn_sign_up = findViewById(R.id.btn_signUp);
        iv_newPassword = findViewById(R.id.iv_newPassword);
        tv_newPassword = findViewById(R.id.tv_newPassword);

        progressDialog = new ProgressDialog(this);

        btn_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email_login.getText().toString();
                String password = et_password_login.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showAlertDialog("Empty fields", "Please finish to complete all the fields.");
                } else {
                    progressDialog.setMessage("Login Please Wait...");
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(loginScreen.this, new OnCompleteListener<AuthResult>() {
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

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(loginScreen.this, signUpScreen.class);
                startActivity(intent);
            }
        });
    }

    private void showAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(loginScreen.this);
        builder.setTitle(title);
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create();
        dialog.setMessage(message);
        dialog.show();
    }

    private void checkPersonType(String email){
        personRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                //if exists the dataSnapshot
                if(ds.exists()){
                    for(DataSnapshot d: ds.getChildren()){
                        Person p = d.getValue(Person.class);
                        if(p.getEmail().equals(email)){
                            String type = p.getType();
                            if(type.equals("user")){
                                intent = new Intent(getApplicationContext(), mainScreenUser.class);
                            }else{
                                //change later
                                //intent = new Intent(getApplicationContext(), mainScreenAdmin.class);
                            }
                            startActivity(intent);
                        }
                    }
                    Toast.makeText(loginScreen.this, "checkPersonType: not found person", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }


//    private void isValidPassword(String email, String password) {
//        personRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                personList = new ArrayList<Person>();
//                for (DataSnapshot data: snapshot.getChildren()) {
//                    Person p = data.getValue(Person.class);
//                    if(p.getEmail().equals(email) && p.getPassword().equals(password)) {
//                        personList.add(p);
//                        Toast.makeText(loginScreen.this, "found "+p.getName(), Toast.LENGTH_LONG).show();
//                        String type = p.getType();
//                        intent = new Intent(loginScreen.this, mainScreenUser.class);
//                        startActivity(intent);
//                    }
//                }
//                    Toast.makeText(loginScreen.this, "Sorry! the email address or the password are incorrect. Please try again!", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError dbe) {
//                 Log.d("ERROR", dbe.getMessage());
//            }
//        });
//    }




}
