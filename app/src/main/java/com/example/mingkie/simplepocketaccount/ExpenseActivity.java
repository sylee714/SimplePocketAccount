package com.example.mingkie.simplepocketaccount;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.DateFormatSymbols;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.N)
public class ExpenseActivity extends AppCompatActivity {

    private TextView currentDay;
    private TextView currentDate;
    private Button submitButton;
    private Spinner spinnerCategory;
    private CurrentDayAndDate currentDayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_expense);
        displayBottomBar();
        currentDay = (TextView) findViewById(R.id.dayTextView);
        currentDate = (TextView) findViewById(R.id.dateTextView);
        currentDayDate = new CurrentDayAndDate(currentDay, currentDate);
        submitButton = (Button) findViewById(R.id.submitButton);
        spinnerCategory = (Spinner) findViewById(R.id.categorySpinner);
        currentDayDate.setCurrentDayDate();

    }

    public void displayBottomBar() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId())
                {
                    case R.id.action_add_expense:
                        Toast.makeText(ExpenseActivity.this, "Action Add Transaction Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_add_income:
                        Toast.makeText(ExpenseActivity.this, "Action Add Income Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(ExpenseActivity.this, IncomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_view_summary:
                        Toast.makeText(ExpenseActivity.this, "Action View Summary Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(ExpenseActivity.this, SummaryActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

}
