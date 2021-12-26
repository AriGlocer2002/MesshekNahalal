package com.example.messheknahalal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class signUpScreen extends AppCompatActivity {

    EditText et_name_signUp, et_last_name_signUp, et_id_number_signUp, et_phone_number_signUp,
            et_email_address_signUp,  et_password_signUp, et_confirm_password_signUp, et_admin_code_signUp;
    Button btn_login2, btn_signUp2;
    CheckBox cb_admin;
    ImageView iv_profile_pic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        btn_signUp2 = findViewById(R.id.btn_signUp2);

        iv_profile_pic = findViewById(R.id.iv_profile_pic);
    }
}
