package com.example.mingkie.simplepocketaccount;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mingkie.simplepocketaccount.Data.TransactionDBHelper;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@TargetApi(Build.VERSION_CODES.N)
public class ExpenseActivity extends AppCompatActivity {

    @BindView(R.id.dateExpenseTextView)
    TextView date;
    @BindView(R.id.submitExpenseButton)
    Button submitButton;
    @BindView(R.id.typeExpenseSpinner)
    Spinner typeSpinner;
    @BindView(R.id.expenseBottomNavigation)
    BottomNavigationView bottomNavigationView;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TransactionDBHelper transactionDBHelper;
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
        setContentView(R.layout.activity_expense);
        // Lets use Butterknife
        ButterKnife.bind(this);
        // Instantiate to access the database
        transactionDBHelper = new TransactionDBHelper(this);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_expense);
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
    @OnClick(R.id.dateExpenseTextView)
    public void currentDateClicked() {
        DatePickerDialog dialog = new DatePickerDialog(ExpenseActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                year, month, dayOfMonth);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @OnClick(R.id.submitExpenseButton)
    public void submitButtonClicked() {

    }

    public void insertExpenseTransaction(String transactionType) {

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
