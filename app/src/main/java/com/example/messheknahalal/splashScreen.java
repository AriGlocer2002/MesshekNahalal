package com.example.messheknahalal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashScreen extends AppCompatActivity {

    ImageView img_logo1;
    Animation logoAnim;
    Intent intent;
    private static int LOGINSCREEN = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



        img_logo1 = findViewById(R.id.img_logo1);
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        img_logo1.setAnimation(logoAnim);

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                intent = new Intent(splashScreen.this, loginScreen.class);
                startActivity(intent);
                finish();
            }
        },LOGINSCREEN);

    }
}