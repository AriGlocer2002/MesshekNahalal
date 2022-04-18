package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class AdminsRecycleViewScreenAdmin extends SuperActivityWithNavigationDrawer {

    RecyclerView rv_admins;
    Intent intent;
    AdminsAdapterFirebase adminAdapter;

    ArrayList<Admin> admins;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_rv_admin);

        rv_admins = findViewById(R.id.admins_rv_screen_rv);
        admins = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("Admin");

        FirebaseRecyclerOptions<Admin> options
                = new FirebaseRecyclerOptions.Builder<Admin>()
                .setQuery(query, Admin.class)
                .setLifecycleOwner(this)
                .build();

        adminAdapter = new AdminsAdapterFirebase(options, this);

        rv_admins.setAdapter(adminAdapter);
        rv_admins.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        initializeNavigationDrawer(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu_2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.return_ic:
                intent = new Intent(this, UsersRecycleViewScreenAdmin.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
