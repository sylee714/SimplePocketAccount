package com.example.mingkie.simplepocketaccount.SelectActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mingkie.simplepocketaccount.R;

/**
 * Created by MingKie on 6/30/2017.
 */

public class SelectWeekActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_week);
        // Sets the title of the activity as 'Choose a week'
        setTitle(R.string.title_activity_select_week);

    }
}
