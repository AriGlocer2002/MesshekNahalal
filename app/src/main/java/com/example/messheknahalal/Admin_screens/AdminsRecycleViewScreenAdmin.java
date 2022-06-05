package com.example.messheknahalal.Admin_screens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.R;
import com.example.messheknahalal.SuperActivityWithNavigationDrawer;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.models.Admin;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminsRecycleViewScreenAdmin extends SuperActivityWithNavigationDrawer {

    RecyclerView rv_admins;
    AdminsAdapterFirebase adminAdapter;

    MaterialButton btn_choose_date;

    Button admins_rv_screen_btn_reset_code;
    EditText admins_rv_screen_et_new_admin_code;

    final DatabaseReference startDateRef = FirebaseDatabase.getInstance().getReference("Start Date");
    final DatabaseReference endDateRef = FirebaseDatabase.getInstance().getReference("End Date");

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

        btn_choose_date = findViewById(R.id.btn_choose_date);
        btn_choose_date.setOnClickListener(v -> showDatePickerDialog());
        btn_choose_date.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startDateRef.removeValue();
                endDateRef.removeValue();
                return true;
            }
        });

        startDateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    btn_choose_date.setText("Choose date");
                    return;
                }

                endDateRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            btn_choose_date.setText("Choose date");
                            return;
                        }

                        long startTime = snapshot.getValue(long.class);
                        long endTime = dataSnapshot.getValue(long.class);

                        Date startDate = new Date(startTime);
                        Date endDate = new Date(endTime);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");

                        String date = simpleDateFormat.format(startDate);
                        String startTimeText = simpleTimeFormat.format(startDate);
                        String endTimeText = simpleTimeFormat.format(endDate);

                        btn_choose_date.setText(date + "\n" + startTimeText + " - " + endTimeText);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener(
                (view, year, month, dayOfMonth) -> showStartTimePickerDialog(year, month+1, dayOfMonth));

        DatePicker datePicker = datePickerDialog.getDatePicker();

        datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
        datePicker.setFirstDayOfWeek(Calendar.SUNDAY);

        datePickerDialog.updateDate(datePicker.getYear(), datePicker.getMonth()-1, datePicker.getDayOfMonth());
        datePickerDialog.show();
    }

    private void showStartTimePickerDialog(int year, int month, int dayOfMonth) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> showEndTimePickerDialog(year, month, dayOfMonth, hourOfDay, minute),
                9, 0, true);

        timePickerDialog.show();
    }

    private void showEndTimePickerDialog(int year, int month, int dayOfMonth, int startHour, int startMinute) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date startDate = new Date(year - 1900, month, dayOfMonth, startHour, startMinute);
                        Date endDate = new Date(year - 1900, month, dayOfMonth, hourOfDay, minute);

                        if (startDate.after(endDate)){
                            Utils.showAlertDialog("Error", "End time can't be before start time",
                                    AdminsRecycleViewScreenAdmin.this);
                        }
                        else {
                            startDateRef.setValue(startDate.getTime());
                            endDateRef.setValue(endDate.getTime());
                        }

                    }
                }, 10, 0, true);

        timePickerDialog.show();
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
        FirebaseDatabase.getInstance().getReference("Admin Code").setValue(newCode)
                .addOnSuccessListener(
                        unused -> Utils.showAlertDialog("Success", "Admin's code was successfully reset", AdminsRecycleViewScreenAdmin.this))
                .addOnFailureListener(
                        e -> Toast.makeText(AdminsRecycleViewScreenAdmin.this, "Failed!", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu_2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.return_ic) {
            startActivity(new Intent(this, UsersRecycleViewScreenAdmin.class));
            return true;
        }
        return false;
    }
}
