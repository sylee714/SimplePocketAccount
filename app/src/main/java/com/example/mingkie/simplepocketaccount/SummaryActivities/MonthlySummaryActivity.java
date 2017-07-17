package com.example.mingkie.simplepocketaccount.SummaryActivities;

import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.mingkie.simplepocketaccount.ArrayAdapters.MonthlySummaryAdapter;
import com.example.mingkie.simplepocketaccount.ArrayAdapters.WeeklySummaryAdapter;
import com.example.mingkie.simplepocketaccount.Chart.ChartActivity;
import com.example.mingkie.simplepocketaccount.Data.Action;
import com.example.mingkie.simplepocketaccount.Data.ActionContract;
import com.example.mingkie.simplepocketaccount.Data.ActionDBHelper;
import com.example.mingkie.simplepocketaccount.Data.Day;
import com.example.mingkie.simplepocketaccount.Data.Expense;
import com.example.mingkie.simplepocketaccount.Data.Income;
import com.example.mingkie.simplepocketaccount.Data.Week;
import com.example.mingkie.simplepocketaccount.Dialogs.MonthYearDialog;
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

public class MonthlySummaryActivity extends AppCompatActivity {
    @BindView(R.id.monthYearMonthlySummary)
    TextView monthYear;
    @BindView(R.id.incomeAmountMonthlySummary)
    TextView incomeAmountTextView;
    @BindView(R.id.expenseAmountMonthlySummary)
    TextView expenseAmountTextView;
    @BindView(R.id.netAmountMonthlySummary)
    TextView netAmountTextView;
    @BindView(R.id.weekDayMonthlySummary)
    TextView weekOrDay;
    @BindView(R.id.listViewMonthlySummary)
    ListView listView;

    private final String INCOME = "Income";
    private final String EXPENSE = "Expense";

    private Income totalIncome;
    private Expense totalExpense;

    private Calendar calendar;

    private boolean displayingWeek;

    private int numberOfWeeks;
    private int year;
    private int month;
    private final String[] MONTHS  = new String[]{"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December" };
    private final String[] DAYS = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};
    private List<Week> weeks;
    private List<Day> days;

    private double incomeTotal;
    private double expenseTotal;
    private double netTotal;

    private SQLiteDatabase db;
    private ActionDBHelper actionDBHelper;

    private MonthlySummaryAdapter weekMonthlySummaryAdapter;
    private WeeklySummaryAdapter dayMonthlySummaryAdapter;

    private MonthYearDialog monthYearDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_summary);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_monthly_summary);

        ButterKnife.bind(this);

        days = new ArrayList<Day>();
        weeks = new ArrayList<Week>();

        displayingWeek = true;

        incomeTotal = 0;
        expenseTotal = 0;
        netTotal = 0;

        totalIncome = new Income();
        totalExpense = new Expense();

        actionDBHelper = new ActionDBHelper(this);
        db = actionDBHelper.getReadableDatabase();

        monthYearDialog = new MonthYearDialog(this);

        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        numberOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        Log.i("number of week", "July" + " " + numberOfWeeks);

        initializeDay();
        addDay();
        addWeek();
        loadView();

        monthYear.setText(MONTHS[month] + " " + year);
        monthYearDialog.build(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedMonthString = monthYearDialog.getSelectedMonthName();
                int selectedMonthInt = getMonthIndex(selectedMonthString);
                int selectedYear = monthYearDialog.getSelectedYear();
                monthYear.setText(selectedMonthString + " " + selectedYear);
                month = selectedMonthInt;
                year = selectedYear;
                days.clear();
                weeks.clear();
                totalIncome = new Income();
                totalExpense = new Expense();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                numberOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
                incomeTotal = 0;
                expenseTotal = 0;
                netTotal = 0;
                initializeDay();
                addDay();
                addWeek();
                loadView();
            }
        }, null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (displayingWeek) {
                    Log.i("TEST", "Position: " + position);
                    Calendar c = Calendar.getInstance();
                    Log.i("TEST", "Monthly Summary item clicked");
                    Day day = findDay(position);
                    Intent intent = new Intent(MonthlySummaryActivity.this, WeeklySummaryActivity.class);
                    intent.putExtra("year", weekMonthlySummaryAdapter.getItem(position).getYear());
                    intent.putExtra("month", weekMonthlySummaryAdapter.getItem(position).getMonth());
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.WEEK_OF_YEAR, weekMonthlySummaryAdapter.getItem(position).getWeekOfYear());
                    c.set(Calendar.DAY_OF_WEEK, 1);
                    int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                    Log.i("TEST", "First Day: " + dayOfMonth);
                    intent.putExtra("dayOfMonth", day.getDayOfMonth());
                    startActivity(intent);
                } else {
                    Log.i("TEST", "Weekly Summary item clicked");
                    Intent intent = new Intent(MonthlySummaryActivity.this, DailySummaryActivity.class);
                    intent.putExtra("year",dayMonthlySummaryAdapter.getItem(position).getYear());
                    intent.putExtra("month",dayMonthlySummaryAdapter.getItem(position).getMonth());
                    intent.putExtra("dayOfMonth", dayMonthlySummaryAdapter.getItem(position).getDayOfMonth());
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.weekDayMonthlySummary)
    public void weekOrDayClicked() {
        if (displayingWeek) {
            listView.setAdapter(dayMonthlySummaryAdapter);
            displayingWeek = false;
        } else {
            listView.setAdapter(weekMonthlySummaryAdapter);
            displayingWeek = true;
        }
    }

    public Day findDay(int position) {
        Day day = null;
        for (int i = 0; i < days.size(); ++i) {
            if (days.get(i).getWeekOfMonth() == position + 1) {
                day = days.get(i);
            }
        }
        return day;
    }

    public int getMonthIndex(String month) {
        int index = 0;
        for (int i = 0; i < MONTHS.length; ++i) {
            if (MONTHS[i].equals(month)) {
                index = i;
            }
        }
        return index;
    }

    @OnClick(R.id.monthYearMonthlySummary)
    public void monthYearClicked() {
        monthYearDialog.show();
    }

    private void initializeDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weekNumber;
        int dayOfWeek;
        for (int i = 1; i <= maxDay; ++i) {
            c.set(year, month, i);
            weekNumber = c.get(Calendar.WEEK_OF_MONTH);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            Day day = new Day();
            day.setYear(year);
            day.setMonth(month);
            day.setDayOfMonth(i);
            day.setWeekOfMonth(weekNumber);
            day.setDay(DAYS[dayOfWeek - 1]);
            days.add(day);
        }
    }

    private void addDay() {
        for (int i = 0; i < days.size(); ++i) {
            int chosenYear = days.get(i).getYear();
            int chosenMonth = days.get(i).getMonth();
            int chosenDay = days.get(i).getDayOfMonth();
            days.get(i).setIncome(loadIncome(false, 0, chosenYear, chosenMonth, chosenDay));
            days.get(i).setExpense(loadExpense(false, 0,chosenYear, chosenMonth, chosenDay));
        }
    }

    private void addWeek() {
        Calendar c = Calendar.getInstance();
        Log.i("TEST", "Add Week");
        for (int i = 1; i <= numberOfWeeks; ++i) {
            Week week = new Week();
            week.setIncome(loadIncome(true, i, 0, 0 , 0));
            week.setExpense(loadExpense(true, i, 0, 0, 0));
            week.setMonth(month);
            week.setYear(year);
            week.setWeekOfMonth(i);
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.WEEK_OF_MONTH, i);
            week.setWeekOfYear(c.get(Calendar.WEEK_OF_YEAR));
            weeks.add(week);
        }
    }

    private Income loadIncome(boolean isWeek, int weekNum, int chosenYear, int chosenMonth, int chosenDay) {
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
                        ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH + " = ?";
        String[] selectionArgs = {INCOME, year + "", month + "", weekNum + ""};
        if (!isWeek) {
            selection =
                    ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE + " = ?" + " and " +
                            ActionContract.ActionEntry.COLUMN_NAME_YEAR + " = ?" + " and " +
                            ActionContract.ActionEntry.COLUMN_NAME_MONTH + " = ?" + " and " +
                            ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH + " = ?";
            selectionArgs = new String[]{INCOME, chosenYear + "", chosenMonth + "", chosenDay + ""};
        }

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
            int dayOfMonth = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH));
            int dayOfWeek = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_DAYOFWEEK));
            int weekOfMonth = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH));
            int weekOfYear = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFYEAR));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_AMOUNT));

            Action action = new Action();
            action.setActionType(actionType);
            action.setYear(year);
            action.setMonth(month);
            action.setDayOfMonth(dayOfMonth);
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

    private Expense loadExpense(boolean isWeek, int weekNum, int chosenYear, int chosenMonth, int chosenDay) {

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
                        ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH + " = ?";
        String[] selectionArgs = {EXPENSE, year + "", month + "", weekNum + ""};
        if (!isWeek) {
            selection =
                    ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE + " = ?" + " and " +
                            ActionContract.ActionEntry.COLUMN_NAME_YEAR + " = ?" + " and " +
                            ActionContract.ActionEntry.COLUMN_NAME_MONTH + " = ?" + " and " +
                            ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH + " = ?";
            selectionArgs = new String[]{EXPENSE, chosenYear + "", chosenMonth + "", chosenDay + ""};
        }

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
            int dayOfMonth = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH));
            int dayOfWeek = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_DAYOFWEEK));
            int weekOfMonth = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH));
            int weekOfYear = cursor.getInt(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFYEAR));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_AMOUNT));

            Action action = new Action();
            action.setActionType(actionType);
            action.setYear(year);
            action.setMonth(month);
            action.setDayOfMonth(dayOfMonth);
            action.setDayOfWeek(dayOfWeek);
            action.setWeekOfMonth(weekOfMonth);
            action.setWeekOfYear(weekOfYear);
            action.setCategory(category);
            action.setPayment(payment);
            action.setNotes(notes);
            action.setAmount(amount);

            expense.add(category, payment, amount);
            totalExpense.add(category, payment, amount);
            Log.i("Food: ", action.getCategory().equals("Food") + "");
            Log.i("amount: ", action.getAmount() + "");
        }
        cursor.close();
        return expense;
    }

    private void loadView() {
        Log.i("TEST", "Load view");
        // Income total
        for (int i = 0; i < weeks.size(); ++i) {
            incomeTotal = incomeTotal + weeks.get(i).getIncome().getTotalAmount();
        }
        // Expense total
        for (int i = 0; i < weeks.size(); ++i) {
            expenseTotal = expenseTotal + weeks.get(i).getExpense().getTotalAmount();
        }

        netTotal = incomeTotal - expenseTotal;
        Log.i("NET TOTAL", netTotal + "");

        Double truncatedExpenseTotal = BigDecimal.valueOf(expenseTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedIncomeTotal = BigDecimal.valueOf(incomeTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedNetTotal = BigDecimal.valueOf(netTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();

        expenseAmountTextView.setText(truncatedExpenseTotal + "");
        incomeAmountTextView.setText(truncatedIncomeTotal + "");
        netAmountTextView.setText(truncatedNetTotal + "");

        weekMonthlySummaryAdapter = new MonthlySummaryAdapter(this, R.layout.listview_monthly_summary, weeks);
        dayMonthlySummaryAdapter = new WeeklySummaryAdapter(this, R.layout.listview_weekly_summary, days);
        listView.setAdapter(weekMonthlySummaryAdapter);
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
                Intent intent = new Intent(MonthlySummaryActivity.this, ChartActivity.class);
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
