package com.example.messheknahalal.Admin_screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.example.messheknahalal.Utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AdminsRecycleViewScreenAdmin extends SuperActivityWithNavigationDrawer {

    RecyclerView rv_admins;
    Intent intent;
    AdminsAdapterFirebase adminAdapter;

    Button admins_rv_screen_btn_reset_code;
    EditText admins_rv_screen_et_new_admin_code;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_rv_admin);

        rv_admins = findViewById(R.id.admins_rv_screen_rv);

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

        admins_rv_screen_et_new_admin_code = findViewById(R.id.admins_rv_screen_et_new_admin_code);

        admins_rv_screen_btn_reset_code = findViewById(R.id.admins_rv_screen_btn_reset_code);
        admins_rv_screen_btn_reset_code.setOnClickListener(v -> checkField());
    }

    private void checkField() {
        String code = admins_rv_screen_et_new_admin_code.getText().toString();

        if (code.isEmpty()){
            Utils.showAlertDialog("Empty field", "Please complete the field with a valuable code", this);
        }
        else if(code.length() < 5){
            Utils.showAlertDialog("Invalid new code", "The code has to be at least 5 characters long", this);
        }
        else {
            resetAdminCode(code);
        }

    }

    private void resetAdminCode(String code) {
        int newCode = Integer.parseInt(code);
        FirebaseDatabase.getInstance().getReference("AdminCode").setValue(newCode).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Utils.showAlertDialog("Success", "Admin's code was successfully reset", AdminsRecycleViewScreenAdmin.this);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminsRecycleViewScreenAdmin.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
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
