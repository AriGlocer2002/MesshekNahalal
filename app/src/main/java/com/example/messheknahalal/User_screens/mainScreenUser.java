package com.example.messheknahalal.User_screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class mainScreenUser extends SuperActivityWithNavigationDrawer implements NavController.OnDestinationChangedListener {

    BottomNavigationView bottomNav;
    Intent intent;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_user);

        FirebaseUser user = auth.getCurrentUser();
        String userEmail = user.getEmail();
        String userPath = "User_" + userEmail.replace(".","-");

        //reset last login date
        String date = getCurrentDate();
        userRef.child(userPath).child("last_login").setValue(date);

        //bottom navigation bar
        bottomNav = findViewById(R.id.main_screen_user_bottomNav);

        NavController navController = Navigation.findNavController(this, R.id.main_screen_user_container);
        navController.addOnDestinationChangedListener(this);

        NavigationUI.setupWithNavController(bottomNav, navController);

        initializeNavigationDrawer(false);

        Log.d("ariel", getLocalClassName());
    }

    public void sendEmail(@NonNull String recipientsList, String subject, String text){
        String[] recipients = "ariogl02@gmail.com".split(",");

        intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);

        intent.setType("message/rfc822");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu_1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.shopping:
                Toast.makeText(this, "Shopping", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ShoppingScreen.class));
                return true;
        }
        return false;

    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

    }
}


