package com.example.mingkie.simplepocketaccount;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MingKie on 6/28/2017.
 */

public class IncomeActivity extends AppCompatActivity {

    @BindView(R.id.dateIncomeTextView)
    TextView date;
    @BindView(R.id.submitIncomeButton)
    Button submitButton;
    @BindView(R.id.typeIncomeSpinner)
    Spinner moneyTypeSpinner;
    @BindView(R.id.incomeBottomNavigation)
    BottomNavigationView bottomNavigationView;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private Calendar calendar;
    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    private int weekOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Shows activity_expense
        setContentView(R.layout.activity_income);
        // Lets use Butterknife
        ButterKnife.bind(this);
        // Sets the title of the activity as 'Main'
        setTitle(R.string.title_activity_income);
        // Displays current date
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        // Adds 1 to month since it counts from 0 - 11, not 1 - 12
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        // Sets date to current date which is the default value
        date.setText(month + 1 + "/" + dayOfMonth + "/" + year);
        // Displays the bottom navigation bar.
        displayBottomBar();
        // When mDateSetListener is called
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(month + 1 + "/" + dayOfMonth + "/" + year);
            }
        };
    }

    // method: currentDateClicked()
    // purpose: Sets the date as the chosen date.
    @OnClick(R.id.dateIncomeTextView)
    public void currentDateClicked() {
        DatePickerDialog dialog = new DatePickerDialog(IncomeActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                year, month, dayOfMonth);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
