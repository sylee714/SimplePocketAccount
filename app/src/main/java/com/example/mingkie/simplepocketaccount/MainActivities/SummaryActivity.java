package com.example.mingkie.simplepocketaccount.MainActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mingkie.simplepocketaccount.Dialogs.MonthYearDialog;
import com.example.mingkie.simplepocketaccount.R;
import com.example.mingkie.simplepocketaccount.SelectActivities.SelectDateActivity;
import com.example.mingkie.simplepocketaccount.SelectActivities.SelectMonthYearActivity;
import com.example.mingkie.simplepocketaccount.SelectActivities.SelectWeekActivity;
import com.example.mingkie.simplepocketaccount.SelectActivities.SelectYearActivity;
import com.example.mingkie.simplepocketaccount.SummaryActivities.DailySummaryActivity;
import com.example.mingkie.simplepocketaccount.SummaryActivities.MonthlySummaryActivity;
import com.example.mingkie.simplepocketaccount.SummaryActivities.WeeklySummaryActivity;
import com.example.mingkie.simplepocketaccount.SummaryActivities.YearlySummaryActivity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MingKie on 6/25/2017.
 */

public class SummaryActivity extends AppCompatActivity {

    @BindView(R.id.summarySpinner)
    Spinner summarySpinner;
    @BindView(R.id.submitSummaryButton)
    Button submitButton;
    @BindView(R.id.summaryBottomNavigation)
    BottomNavigationView bottomNavigationView;

    private Calendar calendar;
    private int year;
    private int month;
    private int dayOfMonth;

    private ArrayAdapter<CharSequence> adapter;
    private String summarySelected;

    private MonthYearDialog monthYearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        ButterKnife.bind(this);
        // Sets the title of the activity as 'Summary'
        setTitle(R.string.title_activity_summary);
        displayBottomBar();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        adapter = ArrayAdapter.createFromResource(this, R.array.list_summary_type, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        summarySpinner.setAdapter(adapter);
        summarySpinnerSetOnItemSelected();

    }


    public void summarySpinnerSetOnItemSelected() {
        summarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                summarySelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // method: submitButtonOnClick()
    // purpose: Moves on to selected summary activity when submit button is clicked.
    @OnClick(R.id.submitSummaryButton)
    public void submitButtonOnClick() {
        Intent intent;
        switch (summarySelected) {
            case "Daily Summary":
                Log.i("Test", "Daily Summary");
                intent = new Intent(SummaryActivity.this, DailySummaryActivity.class);
                intent.putExtra("year", year);
                Log.i("Year", year +"");
                intent.putExtra("month", month);
                Log.i("Month", month +"");
                intent.putExtra("dayOfMonth", dayOfMonth);
                Log.i("Day", dayOfMonth +"");
                startActivity(intent);
                break;
            case "Weekly Summary":
                intent = new Intent(SummaryActivity.this, WeeklySummaryActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
                break;
            case "Monthly Summary":
                intent = new Intent(SummaryActivity.this, MonthlySummaryActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
                break;
            case "Yearly Summary":
                intent = new Intent(SummaryActivity.this, YearlySummaryActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "Select one.", Toast.LENGTH_SHORT).show();
                break;
        }
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
                        Toast.makeText(SummaryActivity.this, "Action Add Action Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(SummaryActivity.this, ExpenseActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_add_income:
                        Toast.makeText(SummaryActivity.this, "Action Add Income Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(SummaryActivity.this, IncomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_view_summary:
                        Toast.makeText(SummaryActivity.this, "Action View Summary Clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

}
