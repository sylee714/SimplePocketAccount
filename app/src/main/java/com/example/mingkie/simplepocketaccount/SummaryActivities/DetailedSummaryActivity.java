package com.example.mingkie.simplepocketaccount.SummaryActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mingkie.simplepocketaccount.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class activity displays the specific information about the selected transaction.
 */
public class DetailedSummaryActivity extends AppCompatActivity {
    @BindView(R.id.dateDetailedSummary)
    TextView dateTextView;
    @BindView(R.id.categoryDetailedSummary)
    TextView categoryTextView;
    @BindView(R.id.paymentDetailedSummary)
    TextView paymentTextView;
    @BindView(R.id.amountDetailedSummary)
    TextView amoutTextView;
    @BindView(R.id.notesDetailedSummary)
    TextView notesTextView;

    private int year;
    private int month;
    private int day;
    private String category;
    private String payment;
    private String notes;
    private double amount;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_summary);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        setTitle(title);
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("dayOfMonth", 0);
        category = intent.getStringExtra("category");
        payment = intent.getStringExtra("payment");
        notes = intent.getStringExtra("notes");
        amount = intent.getDoubleExtra("amount", 0);
        Double truncatedAmount = BigDecimal.valueOf(amount).setScale(4, RoundingMode.HALF_UP).doubleValue();
        dateTextView.setText((month + 1) + "/" + day + "/" + year);
        categoryTextView.setText(category);
        paymentTextView.setText(payment);
        notesTextView.setText(notes);
        amoutTextView.setText(truncatedAmount + "");
    }
}
