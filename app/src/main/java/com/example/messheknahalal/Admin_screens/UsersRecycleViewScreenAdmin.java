package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Objects.User;
import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.example.messheknahalal.Utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class UsersRecycleViewScreenAdmin extends SuperActivityWithNavigationDrawer {

    RecyclerView rv_users;
    Intent intent;
    UsersAdapterFirebase userAdapter;

    ArrayList<User> users;
    EditText et_adminListAccess;
    Button btn_login;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_rv_admin);

        et_adminListAccess = findViewById(R.id.users_rv_screen_et_admin_list_access);
        btn_login = findViewById(R.id.users_rv_screen_btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo - cambiar luego passAccess
                if(et_adminListAccess.getText().toString().equals("12345")){
                    intent = new Intent(UsersRecycleViewScreenAdmin.this, AdminsRecycleViewScreenAdmin.class);
                    startActivity(intent);
                }else{
                    Utils.showAlertDialog("Access denied", "Wrong access password", UsersRecycleViewScreenAdmin.this);
                }
            }
        });


        rv_users = findViewById(R.id.users_rv_screen_rv);
        users = new ArrayList<>();


        Query query = FirebaseDatabase.getInstance().getReference("User");

        FirebaseRecyclerOptions<User> options
                = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();

        userAdapter = new UsersAdapterFirebase(options, this);

        rv_users.setAdapter(userAdapter);
        rv_users.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        initializeNavigationDrawer(true);
    }
}