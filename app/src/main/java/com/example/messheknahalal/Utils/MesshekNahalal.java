package com.example.messheknahalal.Utils;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MesshekNahalal extends Application {

    DatabaseReference productsInCartRef = FirebaseDatabase.getInstance().getReference("Carts");

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //checks if user is logged in
        if (firebaseUser != null){
            String email = firebaseUser.getEmail();
            String emailPath = Utils.emailToUserPath(email);

            productsInCartRef = productsInCartRef.child(emailPath);

            //clears user's cart
            productsInCartRef.removeValue();
        }

    }

}
