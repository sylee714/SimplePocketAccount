package com.example.mingkie.simplepocketaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MingKie on 6/30/2017.
 */

public class MonthlySummaryActivity extends AppCompatActivity{

    @BindView(R.id.monthYearMonthlySummary)
    TextView monthYear;

    private int year;
    private int month;
    private String[] months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_summary);

        ButterKnife.bind(this);

        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_monthly_summary);
        months = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December" };
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);

        monthYear.setText(months[month] + " " + year);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
