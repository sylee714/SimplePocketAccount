package com.example.mingkie.simplepocketaccount;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by MingKie on 6/25/2017.
 */

public class SummaryActivity extends AppCompatActivity {

    private Spinner summarySpinner;
    private ArrayAdapter<CharSequence> adapter;
    private String summarySelected;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        // Sets the title of the activity as 'Summary'
        setTitle(R.string.title_activity_summary);

        summarySpinner = (Spinner) findViewById(R.id.summarySpinner);
        submitButton = (Button) findViewById(R.id.submitButton);
        adapter = ArrayAdapter.createFromResource(this, R.array.list_summary_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        summarySpinner.setAdapter(adapter);

        summarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                summarySelected = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (summarySelected) {
                    case "Daily Summary":
                        intent = new Intent(SummaryActivity.this, ChooseDateActivity.class);
                        startActivity(intent);
                        break;
                    case "Weekly Summary":
                        intent = new Intent(SummaryActivity.this, ChooseWeekActivity.class);
                        startActivity(intent);
                        break;
                    case "Monthly Summary":
                        intent = new Intent(SummaryActivity.this, ChooseMonthActivity.class);
                        startActivity(intent);
                        break;
                    case "Yearly Summary":
                        intent = new Intent(SummaryActivity.this, ChooseYearActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId())
                {
                    case R.id.action_add_expense:
                        Toast.makeText(SummaryActivity.this, "Action Add Transaction Clicked", Toast.LENGTH_SHORT).show();
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
