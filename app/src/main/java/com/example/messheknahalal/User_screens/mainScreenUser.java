package com.example.messheknahalal.User_screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.R;
import com.example.messheknahalal.loginScreen;
import com.example.messheknahalal.myProfileScreen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class mainScreenUser extends AppCompatActivity {

    BottomNavigationView bottomNav;
    DrawerLayout drawerMenu;
    NavigationView nav_view;
    Intent intent;
    TextView nd_tv_name, nd_tv_email;
    ImageView nv_profile_img;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
    StorageReference rStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_user);

        FirebaseUser user = auth.getCurrentUser();
        String userEmail = user.getEmail();
        String userPath = "User_"+userEmail.replace(".","-");
        rStore = FirebaseStorage.getInstance().getReference();

        //reset last login date
        String date = getCurrentDate();
        userRef.child(userPath).child("last_login").setValue(date);

        //setting profile information in the navigation drawer
        NavigationView navigationView = findViewById(R.id.main_screen_user_nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView navUserName = headerView.findViewById(R.id.header_nd_tv_name);
        TextView email = headerView.findViewById(R.id.header_nd_tv_email);
        email.setText(user.getEmail());

        //profile image
        nv_profile_img = headerView.findViewById(R.id.header_nd_iv_pp);
        StorageReference profileRef = rStore.child("profiles/pp_"+userEmail.replace(".","-")+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mainScreenUser.this).load(uri).centerCrop().into(nv_profile_img);
            }
        });



        //bottom navigation bar
        bottomNav = findViewById(R.id.main_screen_user_bottomNav);

        bottomNav.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_user_container, new HomeFragment()).commit();

        //Navigation menu
        nd_tv_name = findViewById(R.id.header_nd_tv_name);
        nd_tv_email = findViewById(R.id.header_nd_tv_email);



        drawerMenu = findViewById(R.id.main_screen_user_drawer_layout);
        nav_view = findViewById(R.id.main_screen_user_nav_view);

        Toolbar toolbar = findViewById(R.id.main_screen_user_toolbar);
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
                    case R.id.myProfile_item:
                        intent = new Intent(mainScreenUser.this, myProfileScreen.class);
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
                        intent = new Intent(mainScreenUser.this, loginScreen.class);
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

    public BottomNavigationView.OnItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment fragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.home:
                            fragment = new HomeFragment();
                            break;

                        case R.id.products:
                            fragment = new ProductsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_screen_user_container, fragment).commit();
                    return true;
                }
            };

    public void onBackPressed(){
        //if drawer is open close the drawer
        if(drawerMenu.isDrawerOpen(GravityCompat.START)){
            drawerMenu.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.shopping:
                Toast.makeText(this, "Shopping", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }
}



