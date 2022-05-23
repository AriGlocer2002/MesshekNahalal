package com.example.messheknahalal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messheknahalal.Admin_screens.mainScreenAdmin;
import com.example.messheknahalal.models.Person;
import com.example.messheknahalal.User_screens.mainScreenUser;
import com.example.messheknahalal.Utils.DataBaseHelper;
import com.example.messheknahalal.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splashScreen extends AppCompatActivity {

    ImageView img_logo1;
    Animation logoAnim;
    Intent intent;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference personRef = FirebaseDatabase.getInstance().getReference().child("Person");
    private static final int LOGIN_SCREEN = 5500;

    DataBaseHelper user = new DataBaseHelper("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        SQLiteDatabase db = openOrCreateDatabase(Utils.DATABASE_NAME, MODE_PRIVATE, null);
        Utils.createAllTables(db);

        img_logo1 = findViewById(R.id.splash_screen_iv_main_logo);
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        img_logo1.setAnimation(logoAnim);


        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    intent = new Intent(splashScreen.this, loginScreen.class);
                startActivity(intent);
                finish();
                }
                else{
                    FirebaseUser person = auth.getCurrentUser();
                    String email = person.getEmail();
                    checkPersonType(email);
                }
            }
        }, LOGIN_SCREEN);
    }

    public void checkPersonType(@NonNull String email){
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
                    Toast.makeText(splashScreen.this, "checkPersonType: not found person", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }

    public void onBackPressed(){}
}