package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.models.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UsersRecycleViewScreenAdmin extends SuperActivityWithNavigationDrawer {

    RecyclerView rv_users;
    UsersAdapterFirebase userAdapter;

    EditText et_adminListAccess;
    Button btn_login;

    final DatabaseReference adminCodeRef = FirebaseDatabase.getInstance().getReference("Admin Code");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_rv_admin);

        et_adminListAccess = findViewById(R.id.users_rv_screen_et_admin_list_access);
        btn_login = findViewById(R.id.users_rv_screen_btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo - cambiar luego passAccess

                adminCodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {
                        //if exists the dataSnapshot
                        if (ds.exists()) {
                            Log.d("ariel", "ds exists");

                            String codeDB = ds.getValue().toString();

                            if (et_adminListAccess.getText().toString().equals(codeDB)) {
                                startActivity(new Intent(UsersRecycleViewScreenAdmin.this, AdminsRecycleViewScreenAdmin.class));
                            }
                            else {
                                Utils.showAlertDialog("Access denied", "Wrong access password", UsersRecycleViewScreenAdmin.this);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError dbe) {
                        Log.d("ERROR", dbe.getMessage());
                    }
                });
            }
        });

        rv_users = findViewById(R.id.users_rv_screen_rv);

        Query query = FirebaseDatabase.getInstance().getReference("User");

        FirebaseRecyclerOptions<User> options
                = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();

        userAdapter = new UsersAdapterFirebase(options, this);

        rv_users.setAdapter(userAdapter);
        rv_users.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        initializeNavigationDrawer(true);
    }
}