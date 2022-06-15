package com.example.messheknahalal;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ChooseDayOfWeekDialog extends Dialog {

        RadioGroup radioGroup;

        RadioButton tb_Sunday;
        RadioButton tb_Monday;
        RadioButton tb_Tuesday;
        RadioButton tb_Wednesday;
        RadioButton tb_Thursday;
        RadioButton tb_Friday;
        RadioButton tb_Saturday;

        ArrayList<Integer> daysOfWeek;

        OnDayOfWeekSelectedListener onDayOfWeekSelectedListener;

    public ChooseDayOfWeekDialog(@NonNull Context context, OnDayOfWeekSelectedListener onDayOfWeekSelectedListener) {
            super(context);
            this.onDayOfWeekSelectedListener = onDayOfWeekSelectedListener;

            getWindow().setBackgroundDrawableResource(R.drawable.all_rv_rl_background);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.dialog_choose_day_of_week);

            radioGroup = findViewById(R.id.chip_group);

            tb_Sunday = findViewById(R.id.tb_Sunday);
            tb_Monday = findViewById(R.id.tb_Monday);
            tb_Tuesday = findViewById(R.id.tb_Tuesday);
            tb_Wednesday = findViewById(R.id.tb_Wednesday);
            tb_Thursday = findViewById(R.id.tb_Thursday);
            tb_Friday = findViewById(R.id.tb_Friday);
            tb_Saturday = findViewById(R.id.tb_Saturday);

            daysOfWeek = new ArrayList<>();
            daysOfWeek.add(tb_Monday.getId());
            daysOfWeek.add(tb_Tuesday.getId());
            daysOfWeek.add(tb_Wednesday.getId());
            daysOfWeek.add(tb_Thursday.getId());
            daysOfWeek.add(tb_Friday.getId());
            daysOfWeek.add(tb_Saturday.getId());
            daysOfWeek.add(tb_Sunday.getId());

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int position = daysOfWeek.indexOf(checkedId) + 1;
                    onDayOfWeekSelectedListener.onDayOfWeekSelected(position);
                    dismiss();
                }
            });
    }

    public interface OnDayOfWeekSelectedListener{
        void onDayOfWeekSelected(int dayOfWeekPosition);
    }
}
