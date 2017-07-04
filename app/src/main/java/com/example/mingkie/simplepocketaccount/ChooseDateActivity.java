package com.example.mingkie.simplepocketaccount;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by MingKie on 6/30/2017.
 */

public class ChooseDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);
        // Sets the title of the activity as 'Choose a Date'
        setTitle(R.string.title_activity_choose_date);
    }
}
