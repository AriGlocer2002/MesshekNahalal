package com.example.messheknahalal;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.messheknahalal.Objects.Person;
import com.example.messheknahalal.Objects.User;
import com.example.messheknahalal.User_screens.mainScreenUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myProfileScreen extends AppCompatActivity{

    DrawerLayout drawerMenu;
    NavigationView nav_view;
    Intent intent;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User"),
                      personRef = FirebaseDatabase.getInstance().getReference().child("Person");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button btn_update_data, btn_reset_pass;
    EditText et_name, et_last_name, et_email, et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        FirebaseUser user = auth.getCurrentUser();
        String userEmail = user.getEmail();
        String userPath = "User_"+userEmail.replace(".","-");
        String personPath = "Person_"+userEmail.replace(".","-");

        //show the data of the user
        showData(userEmail);

        btn_reset_pass = findViewById(R.id.my_profile_btn_reset_pass);
        btn_update_data =findViewById(R.id.my_profile_btn_update);

        btn_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                snackBar("Please verify your email inbox to reset your password");
                            }
                        }
                    });
            }
        });

        et_phone = findViewById(R.id.my_profile_et_phone_number);
        et_name = findViewById(R.id.my_profile_et_name);
        et_last_name = findViewById(R.id.my_profile_et_last_name);

        btn_update_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check fields
                   if (et_name.getText().toString().isEmpty() ||
                           et_last_name.getText().toString().isEmpty() ||
                           et_phone.getText().toString().isEmpty()) {

                        showAlertDialog("Empty fields", "Please finish to complete all the fields.");
                    }else {
                      String name = et_name.getText().toString();
                      String last_name = et_last_name.getText().toString();
                      String phone = et_phone.getText().toString();

                      name = capitalizeString(name);
                      last_name = capitalizeString(last_name);

                      userRef.child(userPath).child("name").setValue(name);
                      userRef.child(userPath).child("last_name").setValue(last_name);
                      userRef.child(userPath).child("phone").setValue(phone);

                      personRef.child(personPath).child("name").setValue(name);
                      personRef.child(personPath).child("last_name").setValue(last_name);
                      personRef.child(personPath).child("phone").setValue(phone);

                      intent = new Intent(myProfileScreen.this, mainScreenUser.class);
                      startActivity(intent);
                   }
            }
        });


        drawerMenu = findViewById(R.id.my_profile_drawer_layout);
        nav_view = findViewById(R.id.my_profile_nav_view);

        Toolbar toolbar = findViewById(R.id.my_profile_toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerMenu, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerMenu.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        getSupportActionBar().setTitle("");
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_item:
                        intent = new Intent(myProfileScreen.this, mainScreenUser.class);
                        startActivity(intent);
                        break;

                    case R.id.myOrders_item:

                        break;

                    case R.id.callUs_item:
                        intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:0506829187"));
                        startActivity(intent);
                        break;

                    case R.id.emailUs_item:
                        sendEmail("ariogl02@gmail.com", "", "");
                        break;

                    case R.id.findUs_item:
                        String addressString = "Agriculture and Sustainability Center WIZO Nahalal";
                        Uri geoLocation = Uri.parse("geo:0,0?q="+addressString);

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(geoLocation);

                        startActivity(intent);
                        break;

                    case R.id.logOut_item:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(myProfileScreen.this, loginScreen.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

    }

    public void sendEmail(String recipientsList, String subject, String text){
        String[] recipients = recipientsList.split(",");

        intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);

        intent.setType("message/rfc822");
        startActivity(intent);
    }

    public void onBackPressed(){
        //if drawer is open close the drawer
        if(drawerMenu.isDrawerOpen(GravityCompat.START)){
            drawerMenu.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void snackBar(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.my_profile_drawer_layout), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showData(String email){
        et_email = findViewById(R.id.my_profile_et_email_address);
        et_phone = findViewById(R.id.my_profile_et_phone_number);
        et_name = findViewById(R.id.my_profile_et_name);
        et_last_name = findViewById(R.id.my_profile_et_last_name);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                //if exists the dataSnapshot
                if(ds.exists()){
                    for(DataSnapshot d: ds.getChildren()){
                        User user = d.getValue(User.class);
                        if(user.getEmail().equals(email)){
                            String name = user.getName();
                            String last_name = user.getLast_name();
                            String phone = user.getPhone();

                            et_name.setText(name);
                            et_last_name.setText(last_name);
                            et_phone.setText(phone);
                            et_email.setText(email);
                            et_email.setEnabled(false);
                        }
                    }
                    //Toast.makeText(loginScreen.this, "checkPersonType: not found person", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("ERROR", dbe.getMessage());
            }
        });
    }

    private void showAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(myProfileScreen.this);
        builder.setTitle(title);
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create();
        dialog.setMessage(message);
        dialog.show();
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

}
