package com.example.messheknahalal.Admin_screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Objects.User;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.signUpScreen;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersListViewScreenAdmin extends AppCompatActivity {

    RecyclerView rv_users;
    UsersAdapterRecyclerView adapter;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");

    ArrayList<User> users;

    private UsersAdapterFirebase userAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_lv);

        rv_users = findViewById(R.id.users_lv_screen_lv);
        users = new ArrayList<>();


        Query query = FirebaseDatabase.getInstance().getReference("User");

        FirebaseRecyclerOptions<User> options
                = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();

        userAdapter = new UsersAdapterFirebase(options, this);

        rv_users.setAdapter(userAdapter);
        Log.d("murad", "rv_events.getChildCount() = " + rv_users.getChildCount());
        rv_users.setLayoutManager(new LinearLayoutManager(this));


        /*adapter = new UsersAdapterRecyclerView(users, UsersListViewScreenAdmin.this);
        //get all the users from data base
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        User u = ds.getValue(User.class);
                        Log.d("ariel", u.toString());
                        users.add(u);
                        int position = users.indexOf(u);
                        adapter.notifyItemInserted(position);
                        adapter.notifyItemRangeChanged(position, 1);
//                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dbe) {
                Log.d("Error",dbe.getMessage());
            }
        });

        rv_users.setAdapter(adapter);
        rv_users.setLayoutManager(new LinearLayoutManager(this));*/


    }

}
