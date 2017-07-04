package com.example.mingkie.simplepocketaccount;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by MingKie on 6/30/2017.
 */

public class YearlySummaryActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_summary);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_yearly_summary);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
