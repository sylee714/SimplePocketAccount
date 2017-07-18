package com.example.mingkie.simplepocketaccount.Chart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mingkie.simplepocketaccount.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * This class represents chart activity which displays 4 types of chart.
 */
public class ChartActivity extends AppCompatActivity {
    private final String[] incomeCategories = {"Salary", "Business/Profession", "Capital Gain",
            "House Property", "Gifts", "Others"};
    private final String[] expenseCategories = {"Housing", "Utilities", "Food", "Clothing",
            "Medical/Healthcare", "Donations/Gifts to Charity", "Savings and Insurance",
            "Entertainment and Recreation", "Transportation", "Personal/Debt Payments/Misc"};
    private final String[] paymentMethods = {"Cash", "Check", "Debit/Credit Card", "Others"};

    private String[] xData;
    private double[] yData;

    private double[] incomeAmounts;
    private double[] incomePaymentAmounts;
    private double[] expenseAmounts;
    private double[] expensePaymentAmounts;

    private String chartType;
    private boolean displayingPayment;
    private PieChart pieChart;
    private Spinner chartTypeSpinner;
    private ArrayAdapter<CharSequence> chartTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setTitle(R.string.title_activity_chart);
        Bundle bundle = this.getIntent().getExtras();
        incomeAmounts = bundle.getDoubleArray("incomeAmounts");
        incomePaymentAmounts = bundle.getDoubleArray("incomePaymentAmounts");
        expenseAmounts = bundle.getDoubleArray("expenseAmounts");
        expensePaymentAmounts = bundle.getDoubleArray("expensePaymentAmounts");

        for (int i = 0; i < incomeAmounts.length; ++i) {
            Log.i("Income Amount", incomeAmounts[i] + "");
        }
        for (int i = 0; i < incomePaymentAmounts.length; ++i) {
            Log.i("Income Payment", incomePaymentAmounts[i] + "");
        }
        for (int i = 0; i < expenseAmounts.length; ++i) {
            Log.i("Expense Amount", expenseAmounts[i] + "");
        }
        for (int i = 0; i < expensePaymentAmounts.length; ++i) {
            Log.i("Expense Payment", expensePaymentAmounts[i] + "");
        }

        chartTypeSpinner = (Spinner) findViewById(R.id.chartSpinner);

        chartTypeAdapter = ArrayAdapter.createFromResource(this, R.array.list_chart_type, R.layout.spinner_item);
        chartTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        chartTypeSpinner.setAdapter(chartTypeAdapter);
        chartTypeSpinnerSetOnItemSelected();

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleRadius(0f);
        pieChart.setDescription(null);

        // Display a toast upon a selection
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d("TEST", "valuedSelected");
                Log.d("TEST", "e" + e.toString());
                Log.d("TEST", "h" + h.toString());
                // Find the index of the amount
                int index = e.toString().indexOf("y: ");
                String amount = e.toString().substring(index + 3);
                // Find the corresponding name
                for (int i = 0; i < yData.length; ++i) {
                    if ((float) yData[i] == Float.parseFloat(amount)) {
                        index = i;
                        break;
                    }
                }
                String name = xData[index];
                if (displayingPayment) {
                    Toast.makeText(ChartActivity.this, "Payment Method: " + name + "\n" + "Amount: $" + amount, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChartActivity.this, "Category: " + name + "\n" + "Amount: $" + amount, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected() {
            }
        });
    }

    public void chartTypeSpinnerSetOnItemSelected() {
        chartTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chartType = (String) parent.getItemAtPosition(position);
                if (chartType.equals("Expense/Category")) {
                    Log.d("Selected", "Expense/Category");
                    displayingPayment = false;
                    addDataSet(expenseAmounts, expenseCategories, "Expense/Category");
                } else if (chartType.equals("Expense/Payment Method")) {
                    displayingPayment = true;
                    Log.d("Selected", "Expense/Payment Method");
                    addDataSet(expensePaymentAmounts, paymentMethods, "Expense/Payment Method");
                } else if (chartType.equals("Income/Category")) {
                    displayingPayment = false;
                    Log.d("Selected", "Income/Category");
                    addDataSet(incomeAmounts, incomeCategories, "Income/Category");
                } else if (chartType.equals("Income/Payment Method")) {
                    displayingPayment = true;
                    Log.d("Selected", "Income/Payment Method");
                    addDataSet(incomePaymentAmounts, paymentMethods, "Income/Payment Method");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addDataSet(double[] yData, String[] xData, String title) {
        Log.d("TEST", "addedDataSet");
        this.xData = xData;
        this.yData = yData;
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; ++i) {
            yEntrys.add(new PieEntry((float) yData[i], i));
        }

        for (int i = 0; i < xData.length; ++i) {
            xEntrys.add(xData[i]);
        }

        // Create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, title);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        // Add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.rgb(255, 204, 204));
        colors.add(Color.rgb(51, 102, 0));
        colors.add(Color.rgb(0, 102, 102));

        pieDataSet.setColors(colors);

        // Add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);

        // Create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}
