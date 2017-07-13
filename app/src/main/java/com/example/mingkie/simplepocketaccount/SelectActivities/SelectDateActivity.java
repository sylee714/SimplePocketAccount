package com.example.mingkie.simplepocketaccount.SelectActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import com.example.mingkie.simplepocketaccount.SummaryActivities.DailySummaryActivity;
import com.example.mingkie.simplepocketaccount.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MingKie on 6/30/2017.
 */

public class SelectDateActivity extends AppCompatActivity {

    @BindView(R.id.dailySummaryCalendar)
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        ButterKnife.bind(this);
        // Sets the title of the activity as 'Select date'
        setTitle(R.string.title_activity_select_date);


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Moves to Daily Summary Activity
                Intent intent = new Intent(SelectDateActivity.this, DailySummaryActivity.class);
                // Passes year, month, and day of month
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);

            }
        });

    }
}
