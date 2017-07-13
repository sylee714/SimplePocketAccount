package com.example.mingkie.simplepocketaccount.SummaryActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.mingkie.simplepocketaccount.R;

/**
 * Created by MingKie on 6/30/2017.
 */

public class WeeklySummaryActivity extends AppCompatActivity{

    private int[] lastDates;
    private int weekOfYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_summary);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_weekly_summary);

        lastDates = new int[]{31,28,31,30,
                31,30,31,31,
                30,31,30,31,};
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
