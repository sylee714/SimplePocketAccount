package com.example.mingkie.simplepocketaccount.SummaryActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.example.mingkie.simplepocketaccount.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MingKie on 6/30/2017.
 */

public class YearlySummaryActivity extends AppCompatActivity{

    @BindView(R.id.yearYearlySummary)
    TextView yearTextView;

    private int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_summary);

        ButterKnife.bind(this);

        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_yearly_summary);

        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        yearTextView.setText(year + "");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
