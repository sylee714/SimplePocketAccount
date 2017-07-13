package com.example.mingkie.simplepocketaccount.SelectActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.mingkie.simplepocketaccount.Dialogs.MonthYearDialog;
import com.example.mingkie.simplepocketaccount.SummaryActivities.MonthlySummaryActivity;
import com.example.mingkie.simplepocketaccount.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MingKie on 6/30/2017.
 */

public class SelectMonthYearActivity extends AppCompatActivity {

    @BindView(R.id.selectMonthYear)
    TextView selectMonthYear;
    @BindView(R.id.selectMonthYearSubmitButton)
    Button submitButton;

    private Calendar calendar;
    private String[] months;
    private int year;
    private int month;

    private MonthYearDialog monthYearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Shows activity_expense
        setContentView(R.layout.activity_select_month_year);
        // Lets use Butterknife
        ButterKnife.bind(this);
        // Sets the title of the activity as 'Choose a Month'
        setTitle(R.string.title_activity_select_month_year);
        // Sets the current month and year
        months = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December" };
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        selectMonthYear.setText(months[month] + " " + year);


        monthYearDialog = new MonthYearDialog(this);
        monthYearDialog.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedMonthString = monthYearDialog.getSelectedMonthName();
                int selectedMonthInt = getMonthIndex(selectedMonthString);
                int selectedYear = monthYearDialog.getSelectedYear();
                selectMonthYear.setText(selectedMonthString + " " + selectedYear);
                month = selectedMonthInt;
                year = selectedYear;
            }
        }, null);
    }

    @OnClick(R.id.selectMonthYear)
    public void setSelectMonthYearClicked() {
        monthYearDialog.show();
    }

    @OnClick(R.id.selectMonthYearSubmitButton)
    public void submitButtonClicked() {
        Intent intent = new Intent(SelectMonthYearActivity.this, MonthlySummaryActivity.class);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        startActivity(intent);
    }

    public int getMonthIndex(String month) {
        int index = 0;
        for (int i = 0; i < months.length; ++i) {
            if (months[i].equals(month)) {
                index = i;
            }
        }
        return index;
    }


}
