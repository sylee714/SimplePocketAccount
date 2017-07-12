package com.example.mingkie.simplepocketaccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MingKie on 6/30/2017.
 */

public class SelectYearActivity extends AppCompatActivity {

    TextView yearTextView;
    @BindView(R.id.selectYearSubmitButton)
    Button submitButton;

    private Calendar calendar;
    private int year;

    private YearDialog yearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Shows activity_expense
        setContentView(R.layout.activity_select_year);
        // Lets use Butterknife
        ButterKnife.bind(this);
        // Sets the title of the activity as 'Choose an Year'
        setTitle(R.string.title_activity_select_year);

        yearTextView = (TextView) findViewById(R.id.yearSelectYear);
        // Sets the current year
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        yearTextView.setText(year + "");

        yearDialog = new YearDialog(this);
        yearDialog.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedYear = yearDialog.getSelectedYear();
                yearTextView.setText(selectedYear + "");
                year = selectedYear;
            }
        }, null);
    }

    @OnClick(R.id.yearSelectYear)
    public void setSelectMonthYearClicked() {
        yearDialog.show();
    }

    @OnClick(R.id.selectYearSubmitButton)
    public void submitButtonClicked() {
        Intent intent = new Intent(SelectYearActivity.this, YearlySummaryActivity.class);
        intent.putExtra("year", year);
        startActivity(intent);
    }
}
