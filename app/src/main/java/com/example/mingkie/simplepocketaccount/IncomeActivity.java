package com.example.mingkie.simplepocketaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;

/**
 * Created by MingKie on 6/28/2017.
 */

public class IncomeActivity extends AppCompatActivity {

    @BindView(R.id.dayIncomeTextView)
    private TextView currentDay;
    @BindView(R.id.dateIncomeTextView)
    private TextView currentDate;
    @BindView(R.id.submitIncomeButton)
    private Button submitButton;
    @BindView(R.id.typeIncomeSpinner)
    private Spinner moneyTypeSpinner;
    @BindView(R.id.incomeBottomNavigation)
    private BottomNavigationView bottomNavigationView;
    private CurrentDayAndDate currentDayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        // Sets the title of the activity as 'Main'
        setTitle(R.string.title_activity_income);
        // Displays current day and date
        currentDayDate = new CurrentDayAndDate(currentDay, currentDate);
        currentDayDate.setCurrentDayDate();
        displayBottomBar();
    }

    // method: displayBottomBar()
    // purpose: Displays bottom navigation bar.
    public void displayBottomBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId())
                {
                    case R.id.action_add_expense:
                        Toast.makeText(IncomeActivity.this, "Action Add Transaction Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(IncomeActivity.this, ExpenseActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_add_income:
                        Toast.makeText(IncomeActivity.this, "Action Add Income Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_view_summary:
                        Toast.makeText(IncomeActivity.this, "Action View Summary Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(IncomeActivity.this, SummaryActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
}
