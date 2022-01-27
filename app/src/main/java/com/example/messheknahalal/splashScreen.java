package com.example.messheknahalal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.messheknahalal.User_screens.mainScreenUser;
import com.example.messheknahalal.Utils.DataBaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class splashScreen extends AppCompatActivity {

    ImageView img_logo1;
    Animation logoAnim;
    Intent intent;
    private static int LOGINSCREEN = 5500;

    DataBaseHelper user = new DataBaseHelper("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        img_logo1 = findViewById(R.id.splash_screen_iv_main_logo);
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        img_logo1.setAnimation(logoAnim);


        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() == null)
                    intent = new Intent(splashScreen.this, loginScreen.class);
                else
                    intent = new Intent(splashScreen.this, mainScreenUser.class);
                startActivity(intent);
                finish();
            }
        },LOGINSCREEN);


    }






}