package com.example.messheknahalal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.messheknahalal.Objects.User;
import com.example.messheknahalal.Utils.DataBaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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