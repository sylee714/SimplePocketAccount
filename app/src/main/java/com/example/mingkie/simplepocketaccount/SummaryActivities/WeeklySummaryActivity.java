package com.example.mingkie.simplepocketaccount.SummaryActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mingkie.simplepocketaccount.ArrayAdapters.WeeklySummaryAdapter;
import com.example.mingkie.simplepocketaccount.Chart.ChartActivity;
import com.example.mingkie.simplepocketaccount.Data.Action;
import com.example.mingkie.simplepocketaccount.Data.ActionContract;
import com.example.mingkie.simplepocketaccount.Data.ActionDBHelper;
import com.example.mingkie.simplepocketaccount.Data.Day;
import com.example.mingkie.simplepocketaccount.Data.Expense;
import com.example.mingkie.simplepocketaccount.Data.Income;
import com.example.mingkie.simplepocketaccount.MainActivities.SummaryActivity;
import com.example.mingkie.simplepocketaccount.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MingKie on 6/30/2017.
 */

public class WeeklySummaryActivity extends AppCompatActivity {
    @BindView(R.id.weekWeeklySummary)
    TextView week;
    @BindView(R.id.incomeAmountWeeklySummary)
    TextView incomeAmountTextView;
    @BindView(R.id.expenseAmountWeeklySummary)
    TextView expenseAmountTextView;
    @BindView(R.id.netAmountWeeklySummary)
    TextView netAmountTextView;
    @BindView(R.id.listViewWeeklySummary)
    ListView listView;

    private int[] lastDates = new int[]{31,28,31,30,
            31,30,31,31,
            30,31,30,31,};
    private int weekOfYear;

    private Income totalIncome;
    private Expense totalExpense;

    private final String INCOME = "Income";
    private final String EXPENSE = "Expense";

    private int selectedYear;
    private int selectedMonth;
    private int selectedDayOfMonth;
    private final int NUM_DAYS = 7;
    private final String[] DAYS = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};

    private double incomeTotal;
    private double expenseTotal;
    private double netTotal;

    private Calendar calendar;

    private SQLiteDatabase db;
    private ActionDBHelper actionDBHelper;

    private WeeklySummaryAdapter weeklySummaryAdapter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private List<Day> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_summary);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_weekly_summary);

        ButterKnife.bind(this);

        days = new ArrayList<Day>();
        calendar = Calendar.getInstance();

        actionDBHelper = new ActionDBHelper(this);
        db = actionDBHelper.getReadableDatabase();

        incomeTotal = 0;
        expenseTotal = 0;
        netTotal = 0;

        totalIncome = new Income();
        totalExpense = new Expense();


        Intent intent = getIntent();
        selectedYear = intent.getIntExtra("year", 0);
        selectedMonth = intent.getIntExtra("month", 0);
        selectedDayOfMonth = intent.getIntExtra("dayOfMonth", 0);


        calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        if (selectedMonth == 11 && selectedDayOfMonth > 7 && weekOfYear == 1) {
            Log.i("TEST", "12/31");
            calendar.set(Calendar.YEAR, selectedYear + 1);
            calendar.set(Calendar.MONTH, 1);
        }

        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        initializeDays();
        addDay();
        loadView();

        // When mDateSetListener is called
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.i("TEST", "Picked another date");
                selectedMonth = month;
                Log.i("Month", selectedMonth + "");
                selectedDayOfMonth = dayOfMonth;
                Log.i("Day of Month", selectedDayOfMonth + "");
                selectedYear = year;
                Log.i("Year", selectedYear + "");

                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

                // When a day of December is selected and it's greater than 7 and week of
                // year is 1. (Ex. 12/31/2017)
                if (selectedMonth == 11 && selectedDayOfMonth > 7 && weekOfYear == 1) {
                    Log.i("TEST", "12/31");
                    calendar.set(Calendar.YEAR, selectedYear + 1);
                    calendar.set(Calendar.MONTH, 1);
                }

                calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
                incomeTotal = 0;
                expenseTotal = 0;
                netTotal = 0;

                totalIncome = new Income();
                totalExpense = new Expense();
                days.clear();
                initializeDays();
                addDay();
                loadView();

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TEST", "Weekly Summary item clicked");
                Intent intent = new Intent(WeeklySummaryActivity.this, DailySummaryActivity.class);
                intent.putExtra("year",weeklySummaryAdapter.getItem(position).getYear());
                intent.putExtra("month",weeklySummaryAdapter.getItem(position).getMonth());
                intent.putExtra("dayOfMonth", weeklySummaryAdapter.getItem(position).getDayOfMonth());
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.weekWeeklySummary)
    public void weekClicked() {
        DatePickerDialog dialog = new DatePickerDialog(WeeklySummaryActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener,
                selectedYear, selectedMonth, selectedDayOfMonth);
        dialog.show();
    }

    private void initializeDays() {
        Log.i("Initialize Days", "Week of Year: " + weekOfYear);
        for (int i = 1; i <= NUM_DAYS; ++i) {
            Day day = new Day();

            calendar.set(Calendar.DAY_OF_WEEK, i);
            Log.i("Year", calendar.get(Calendar.YEAR) + "");
            day.setYear(calendar.get(Calendar.YEAR));
            Log.i("Month", calendar.get(Calendar.MONTH) + "");
            day.setMonth(calendar.get(Calendar.MONTH));
            Log.i("Day", calendar.get(Calendar.DAY_OF_MONTH) + "");
            day.setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            day.setDay(DAYS[i-1]);
            day.setWeekOfMonth(0);

            days.add(day);
        }
    }

    private void addDay() {
        for (int i = 0; i < days.size(); ++i) {
            int chosenYear = days.get(i).getYear();
            int chosenMonth = days.get(i).getMonth();
            int chosenDay = days.get(i).getDayOfMonth();
            days.get(i).setIncome(loadIncome(chosenYear, chosenMonth, chosenDay));
            days.get(i).setExpense(loadExpense(chosenYear, chosenMonth, chosenDay));
        }
    }

    private Income loadIncome(int chosenYear, int chosenMonth, int chosenDay) {

        Income income = new Income();

        String[] projection = {
                ActionContract.ActionEntry._ID,
                ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE,
                ActionContract.ActionEntry.COLUMN_NAME_YEAR,
                ActionContract.ActionEntry.COLUMN_NAME_MONTH,
                ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH,
                ActionContract.ActionEntry.COLUMN_NAME_DAYOFWEEK,
                ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH,
                ActionContract.ActionEntry.COLUMN_NAME_WEEKOFYEAR,
                ActionContract.ActionEntry.COLUMN_NAME_CATEGORY,
                ActionContract.ActionEntry.COLUMN_NAME_PAYMENT,
                ActionContract.ActionEntry.COLUMN_NAME_NOTES,
                ActionContract.ActionEntry.COLUMN_NAME_AMOUNT
        };

        String selection =
                ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE + " = ?" + " and " +
                        ActionContract.ActionEntry.COLUMN_NAME_YEAR + " = ?" + " and " +
                        ActionContract.ActionEntry.COLUMN_NAME_MONTH + " = ?" + " and " +
                        ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH + " = ?";
        String[] selectionArgs = {INCOME, chosenYear + "", chosenMonth + "", chosenDay + ""};

        Cursor cursor = db.query(
                ActionContract.ActionEntry.TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        while(cursor.moveToNext()) {

            String actionType = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_CATEGORY));
            String payment = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_PAYMENT));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_NOTES));
            int dayOfWeek = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_DAYOFWEEK));
            int weekOfMonth = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH));
            int weekOfYear = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFYEAR));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_AMOUNT));

            Action action = new Action();
            action.setActionType(actionType);
            action.setYear(chosenYear);
            action.setMonth(chosenMonth);
            action.setDayOfMonth(chosenDay);
            action.setDayOfWeek(dayOfWeek);
            action.setWeekOfMonth(weekOfMonth);
            action.setWeekOfYear(weekOfYear);
            action.setCategory(category);
            action.setPayment(payment);
            action.setNotes(notes);
            action.setAmount(amount);

            income.add(category, payment, amount);
            totalIncome.add(category, payment, amount);
            Log.i("type: ", action.getCategory() + "");
            Log.i("amount: ", action.getAmount() + "");

        }
        cursor.close();
        return income;
    }

    private Expense loadExpense(int chosenYear, int chosenMonth, int chosenDay) {
        Expense expense = new Expense();


        String[] projection = {
                ActionContract.ActionEntry._ID,
                ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE,
                ActionContract.ActionEntry.COLUMN_NAME_YEAR,
                ActionContract.ActionEntry.COLUMN_NAME_MONTH,
                ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH,
                ActionContract.ActionEntry.COLUMN_NAME_DAYOFWEEK,
                ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH,
                ActionContract.ActionEntry.COLUMN_NAME_WEEKOFYEAR,
                ActionContract.ActionEntry.COLUMN_NAME_CATEGORY,
                ActionContract.ActionEntry.COLUMN_NAME_PAYMENT,
                ActionContract.ActionEntry.COLUMN_NAME_NOTES,
                ActionContract.ActionEntry.COLUMN_NAME_AMOUNT
        };

        String selection =
                ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE + " = ?" + " and " +
                        ActionContract.ActionEntry.COLUMN_NAME_YEAR + " = ?" + " and " +
                        ActionContract.ActionEntry.COLUMN_NAME_MONTH + " = ?" + " and " +
                        ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH + " = ?";
        String[] selectionArgs = {EXPENSE, chosenYear + "", chosenMonth + "", chosenDay + ""};

        Cursor cursor = db.query(
                ActionContract.ActionEntry.TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        while(cursor.moveToNext()) {

            String actionType = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_CATEGORY));
            String payment = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_PAYMENT));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_NOTES));
            //int selectedDayOfMonth = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH));
            int dayOfWeek = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_DAYOFWEEK));
            int weekOfMonth = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH));
            int weekOfYear = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFYEAR));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_AMOUNT));

            Action action = new Action();
            action.setActionType(actionType);
            action.setYear(chosenYear);
            action.setMonth(chosenMonth);
            action.setDayOfMonth(chosenDay);
            action.setDayOfWeek(dayOfWeek);
            action.setWeekOfMonth(weekOfMonth);
            action.setWeekOfYear(weekOfYear);
            action.setCategory(category);
            action.setPayment(payment);
            action.setNotes(notes);
            action.setAmount(amount);

            expense.add(category, payment, amount);
            totalExpense.add(category, payment, amount);
            Log.i("type: ", action.getCategory() + "");
            Log.i("amount: ", action.getAmount() + "");
        }
        cursor.close();
        return expense;
    }

    private void loadView() {
        Log.i("TEST", "Load view");
        // Income total
        for (int i = 0; i < days.size(); ++i) {
            incomeTotal = incomeTotal + days.get(i).getIncome().getTotalAmount();
        }
        // Expense total
        for (int i = 0; i < days.size(); ++i) {
            expenseTotal = expenseTotal + days.get(i).getExpense().getTotalAmount();
        }

        netTotal = incomeTotal - expenseTotal;
        Log.i("NET TOTAL", netTotal + "");

        Double truncatedExpenseTotal = BigDecimal.valueOf(expenseTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedIncomeTotal = BigDecimal.valueOf(incomeTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedNetTotal = BigDecimal.valueOf(netTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();

        week.setText((days.get(0).getMonth() + 1) + "/" + days.get(0).getDayOfMonth() + "/" + days.get(0).getYear() + " - " +
                (days.get(NUM_DAYS - 1).getMonth() + 1) + "/" + days.get(NUM_DAYS - 1).getDayOfMonth() + "/" + days.get(NUM_DAYS - 1).getYear());
        expenseAmountTextView.setText(truncatedExpenseTotal + "");
        incomeAmountTextView.setText(truncatedIncomeTotal + "");
        netAmountTextView.setText(truncatedNetTotal + "");

        weeklySummaryAdapter = new WeeklySummaryAdapter(this, R.layout.listview_monthly_summary, days);
        listView.setAdapter(weeklySummaryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.summary_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_summary:
                startActivity(new Intent(this, SummaryActivity.class));
                return true;
            case R.id.view_chart:
                Intent intent = new Intent(WeeklySummaryActivity.this, ChartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDoubleArray("incomeAmounts", totalIncome.getCategories());
                bundle.putDoubleArray("incomePaymentAmounts", totalIncome.getPayments());
                bundle.putDoubleArray("expenseAmounts", totalExpense.getCategories());
                bundle.putDoubleArray("expensePaymentAmounts", totalExpense.getPayments());
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
