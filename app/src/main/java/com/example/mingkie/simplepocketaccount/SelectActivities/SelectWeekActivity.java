package com.example.mingkie.simplepocketaccount.SelectActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import com.example.mingkie.simplepocketaccount.R;
import com.example.mingkie.simplepocketaccount.SummaryActivities.DailySummaryActivity;
import com.example.mingkie.simplepocketaccount.SummaryActivities.WeeklySummaryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MingKie on 6/30/2017.
 */

public class SelectWeekActivity extends AppCompatActivity {

    @BindView(R.id.weeklySummaryCalendar)
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_week);
        // Sets the title of the activity as 'Choose a week'
        setTitle(R.string.title_activity_select_week);

        ButterKnife.bind(this);


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Moves to Daily Summary Activity
                Intent intent = new Intent(SelectWeekActivity.this, WeeklySummaryActivity.class);
                // Passes year, month, and day of month
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);

            }
        });

    }
}
