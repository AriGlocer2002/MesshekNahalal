package com.example.messheknahalal.Admin_screens;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messheknahalal.R;

public class CreateNewProductAdmin extends AppCompatActivity {

    Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_product_admin);

        String[] products_types = {"","Vegetables","Fruits","Shelf","Others"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.background_spinner1, products_types);
        adapter.setDropDownViewResource(R.layout.background_dropdown_spinner_items);

        spinner = findViewById(R.id.create_new_product_admin_spinner_type);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreateNewProductAdmin.this,parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
