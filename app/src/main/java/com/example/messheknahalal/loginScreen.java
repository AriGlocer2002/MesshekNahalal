package com.example.messheknahalal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class loginScreen extends AppCompatActivity {

    EditText et_email_login, et_password_login;
    Button btn_login1;
    Intent intent;
    ImageView iv_newPassword;
    TextView tv_newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email_login = findViewById(R.id.et_email_login);
        et_password_login = findViewById(R.id.et_password_login);
        btn_login1 = findViewById(R.id.btn_login1);
        iv_newPassword = findViewById(R.id.iv_newPassword);
        tv_newPassword = findViewById(R.id.tv_newPassword);


        btn_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(loginScreen.this, signUpScreen.class);
                startActivity(intent);
            }
        });


    }

}
