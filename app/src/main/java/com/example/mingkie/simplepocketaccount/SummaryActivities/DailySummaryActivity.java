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

import com.example.mingkie.simplepocketaccount.ArrayAdapters.DailySummaryAdapter;
import com.example.mingkie.simplepocketaccount.Chart.ChartActivity;
import com.example.mingkie.simplepocketaccount.Data.Action;
import com.example.mingkie.simplepocketaccount.Data.ActionContract;
import com.example.mingkie.simplepocketaccount.Data.ActionDBHelper;
import com.example.mingkie.simplepocketaccount.Data.Expense;
import com.example.mingkie.simplepocketaccount.Data.Income;
import com.example.mingkie.simplepocketaccount.MainActivities.SummaryActivity;
import com.example.mingkie.simplepocketaccount.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MingKie on 6/25/2017.
 */

public class DailySummaryActivity extends AppCompatActivity {
    @BindView(R.id.dailySummaryDate)
    TextView date;
    @BindView(R.id.dailySummaryExpenseTotalAmount)
    TextView expenseAmount;
    @BindView(R.id.dailySummaryIncomeTotalAmount)
    TextView incomeAmount;
    @BindView(R.id.dailySummaryNetTotalAmount)
    TextView netAmount;
    @BindView(R.id.actionHistoryListView)
    ListView listView;
    @BindView(R.id.expenseIncomeDailySummary)
    TextView expenseOrIncome;

    private boolean displayingExpense;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private final String INCOME = "Income";
    private final String EXPENSE = "Expense";

    private Income income;
    private Expense expense;

    private int selectedYear;
    private int selectedMonth;
    private int selectedDayOfMonth;

    private double incomeTotal;
    private double expenseTotal;
    private double netTotal;

    private List<Action> incomeList;
    private List<Action> expenseList;
    private List<Action> combinedList;

    private SQLiteDatabase db;
    private ActionDBHelper actionDBHelper;

    private DailySummaryAdapter expenseDailySummaryAdapter;
    private DailySummaryAdapter incomeDailySummaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_summary);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_daily_summary);
        ButterKnife.bind(this);

        actionDBHelper = new ActionDBHelper(this);
        db = actionDBHelper.getReadableDatabase();

        displayingExpense = true;

        incomeList = new ArrayList<Action>();
        expenseList = new ArrayList<Action>();
        combinedList = new ArrayList<Action>();

        incomeTotal = 0;
        expenseTotal = 0;
        netTotal = 0;

        income = new Income();
        expense = new Expense();

        Intent intent = getIntent();
        selectedYear = intent.getIntExtra("year", 0);
        selectedMonth = intent.getIntExtra("month", 0);
        selectedDayOfMonth = intent.getIntExtra("dayOfMonth", 0);
        date.setText((selectedMonth + 1) + "/" + selectedDayOfMonth + "/" + selectedYear);

        loadView(loadIncome(), loadExpense());

        // When mDateSetListener is called
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(month + 1 + "/" + dayOfMonth + "/" + year);
                String dateString = date.getText().toString();
                String[] data = dateString.split("/");
                //selectedMonth = Integer.parseInt(data[0]) - 1;
                selectedMonth = month;
                Log.i("Month", selectedMonth + "");
                //selectedDayOfMonth = Integer.parseInt(data[1]);
                selectedDayOfMonth = dayOfMonth;
                Log.i("Day of Month", selectedDayOfMonth + "");
                //selectedYear = Integer.parseInt(data[2]);
                selectedYear = year;
                Log.i("Year", selectedYear + "");
                incomeList.clear();
                expenseList.clear();
                combinedList.clear();
                loadView(loadIncome(), loadExpense());
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TEST", "Yearly Summary item clicked");
                if (displayingExpense) {
                    Intent intent = new Intent(DailySummaryActivity.this, DetailedSummaryActivity.class);
                    intent.putExtra("title", "Expense");
                    intent.putExtra("year", expenseDailySummaryAdapter.getItem(position).getYear());
                    intent.putExtra("month", expenseDailySummaryAdapter.getItem(position).getMonth());
                    intent.putExtra("dayOfMonth", expenseDailySummaryAdapter.getItem(position).getDayOfMonth());
                    intent.putExtra("category", expenseDailySummaryAdapter.getItem(position).getCategory());
                    intent.putExtra("payment", expenseDailySummaryAdapter.getItem(position).getPayment());
                    intent.putExtra("amount", expenseDailySummaryAdapter.getItem(position).getAmount());
                    intent.putExtra("notes", expenseDailySummaryAdapter.getItem(position).getNotes());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DailySummaryActivity.this, DetailedSummaryActivity.class);
                    intent.putExtra("title", "Income");
                    intent.putExtra("year", incomeDailySummaryAdapter.getItem(position).getYear());
                    intent.putExtra("month", incomeDailySummaryAdapter.getItem(position).getMonth());
                    intent.putExtra("dayOfMonth", incomeDailySummaryAdapter.getItem(position).getDayOfMonth());
                    intent.putExtra("category", incomeDailySummaryAdapter.getItem(position).getCategory());
                    intent.putExtra("payment", incomeDailySummaryAdapter.getItem(position).getPayment());
                    intent.putExtra("amount", incomeDailySummaryAdapter.getItem(position).getAmount());
                    intent.putExtra("notes", incomeDailySummaryAdapter.getItem(position).getNotes());
                    startActivity(intent);
                }

            }
        });
    }

    @OnClick(R.id.dailySummaryDate)
    public void dateClicked() {
        DatePickerDialog dialog = new DatePickerDialog(DailySummaryActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener,
                selectedYear, selectedMonth, selectedDayOfMonth);
        dialog.show();
    }

    @OnClick(R.id.expenseIncomeDailySummary)
    public void expenseOrIncomeClicked() {
        if (displayingExpense) {
            listView.setAdapter(incomeDailySummaryAdapter);
            displayingExpense = false;
        } else {
            listView.setAdapter(expenseDailySummaryAdapter);
            displayingExpense = true;
        }
    }

    private Income loadIncome() {
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
        String[] selectionArgs = {INCOME, selectedYear + "", selectedMonth + "", selectedDayOfMonth + ""};

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
            action.setYear(selectedYear);
            action.setMonth(selectedMonth);
            action.setDayOfMonth(selectedDayOfMonth);
            action.setDayOfWeek(dayOfWeek);
            action.setWeekOfMonth(weekOfMonth);
            action.setWeekOfYear(weekOfYear);
            action.setCategory(category);
            action.setPayment(payment);
            action.setNotes(notes);
            action.setAmount(amount);

            income.add(category, payment, amount);
            Log.i("type: ", action.getCategory() + "");
            Log.i("amount: ", action.getAmount() + "");

            incomeList.add(action);
        }
        cursor.close();
        return income;
    }

    private Expense loadExpense() {
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
        String[] selectionArgs = {EXPENSE, selectedYear + "", selectedMonth + "", selectedDayOfMonth + ""};

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
            action.setYear(selectedYear);
            action.setMonth(selectedMonth);
            action.setDayOfMonth(selectedDayOfMonth);
            action.setDayOfWeek(dayOfWeek);
            action.setWeekOfMonth(weekOfMonth);
            action.setWeekOfYear(weekOfYear);
            action.setCategory(category);
            action.setPayment(payment);
            action.setNotes(notes);
            action.setAmount(amount);

            expense.add(category, payment, amount);
            Log.i("Food: ", action.getCategory().equals("Food") + "");
            Log.i("amount: ", action.getAmount() + "");

            expenseList.add(action);
        }
        cursor.close();
        return expense;
    }

    private void loadView(Income income, Expense expense) {
        this.income = income;
        this.expense = expense;
        expenseTotal = expense.getTotalAmount();
        incomeTotal = income.getTotalAmount();
        netTotal = incomeTotal - expenseTotal;

        Double truncatedExpenseTotal = BigDecimal.valueOf(expenseTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedIncomeTotal = BigDecimal.valueOf(incomeTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedNetTotal = BigDecimal.valueOf(netTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();

        expenseAmount.setText(truncatedExpenseTotal + "");
        incomeAmount.setText(truncatedIncomeTotal + "");
        netAmount.setText(truncatedNetTotal + "");

        combinedList.addAll(expenseList);
        combinedList.addAll(incomeList);
        expenseDailySummaryAdapter = new DailySummaryAdapter(this, R.layout.listview_daily_summary, expenseList);
        incomeDailySummaryAdapter = new DailySummaryAdapter(this, R.layout.listview_daily_summary, incomeList);
        listView.setAdapter(expenseDailySummaryAdapter);
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
                Intent intent = new Intent(DailySummaryActivity.this, ChartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDoubleArray("incomeAmounts", income.getCategories());
                bundle.putDoubleArray("incomePaymentAmounts", income.getPayments());
                bundle.putDoubleArray("expenseAmounts", expense.getCategories());
                bundle.putDoubleArray("expensePaymentAmounts", expense.getPayments());
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
