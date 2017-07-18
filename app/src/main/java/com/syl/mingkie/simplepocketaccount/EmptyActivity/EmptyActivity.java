package com.syl.mingkie.simplepocketaccount.EmptyActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.syl.mingkie.simplepocketaccount.R;

/**
 * This class activity is empty activity. It's used to make back button
 * on action bar of summary activities to go back to previous activity.
 */
public class EmptyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
    }
}
