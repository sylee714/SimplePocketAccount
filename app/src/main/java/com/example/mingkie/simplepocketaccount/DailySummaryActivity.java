package com.example.mingkie.simplepocketaccount;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by MingKie on 6/25/2017.
 */

public class DailySummaryActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_summary);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_daily_summary);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
