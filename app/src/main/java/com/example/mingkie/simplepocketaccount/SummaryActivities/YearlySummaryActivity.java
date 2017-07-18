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

import com.example.mingkie.simplepocketaccount.ArrayAdapters.YearlySummaryAdapter;
import com.example.mingkie.simplepocketaccount.Chart.ChartActivity;
import com.example.mingkie.simplepocketaccount.Data.Action;
import com.example.mingkie.simplepocketaccount.Data.ActionContract;
import com.example.mingkie.simplepocketaccount.Data.ActionDBHelper;
import com.example.mingkie.simplepocketaccount.Data.Expense;
import com.example.mingkie.simplepocketaccount.Data.Income;
import com.example.mingkie.simplepocketaccount.Data.Month;
import com.example.mingkie.simplepocketaccount.Data.Transaction;
import com.example.mingkie.simplepocketaccount.Dialogs.YearCustomDialog;
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
 * This class activity displays the information of the selected year.
 */
public class YearlySummaryActivity extends AppCompatActivity {
    @BindView(R.id.yearYearlySummary)
    TextView yearTextView;
    @BindView(R.id.incomeAmountYearlySummary)
    TextView incomeAmountTextView;
    @BindView(R.id.expenseAmountYearlySummary)
    TextView expenseAmountTextView;
    @BindView(R.id.netAmountYearlySummary)
    TextView netAmountTextView;
    @BindView(R.id.listViewYearlySummary)
    ListView listView;

    private final String INCOME = "Income";
    private final String EXPENSE = "Expense";

    private int year;

    private final int NUM_OF_MONTHS = 12;
    private final String[] MONTHS  = new String[]{"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December" };

    private List<Month> months;

    private double[] incomeCategories;
    private double[] expenseCategories;
    private double[] incomePayments;
    private double[] expensePayments;

    private double incomeTotal;
    private double expenseTotal;
    private double netTotal;

    private Transaction totalIncome;
    private Transaction totalExpense;

    private SQLiteDatabase db;
    private ActionDBHelper actionDBHelper;

    private YearlySummaryAdapter yearlySummaryAdapter;

    private YearCustomDialog yearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_summary);
        setTitle(R.string.title_activity_yearly_summary);
        ButterKnife.bind(this);

        months = new ArrayList<Month>();

        incomeTotal = 0;
        expenseTotal = 0;
        netTotal = 0;

        expenseCategories = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        incomeCategories = new double[]{0, 0, 0, 0, 0, 0,};
        expensePayments = new double[]{0, 0, 0, 0};
        incomePayments = new double[]{0, 0, 0, 0};

        totalIncome = new Income(incomeCategories, incomePayments);
        totalExpense = new Expense(expenseCategories, expensePayments);

        actionDBHelper = new ActionDBHelper(this);
        db = actionDBHelper.getReadableDatabase();

        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        yearTextView.setText(year + "");

        addMonth();
        loadView();

        // year picker dialog
        yearDialog = new YearCustomDialog(this);
        yearDialog.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedYear = yearDialog.getSelectedYear();
                yearTextView.setText(selectedYear + "");
                year = selectedYear;
                incomeTotal = 0;
                expenseTotal = 0;
                netTotal = 0;
                expenseCategories = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                incomeCategories = new double[]{0, 0, 0, 0, 0, 0,};
                expensePayments = new double[]{0, 0, 0, 0};
                incomePayments = new double[]{0, 0, 0, 0};
                totalIncome = new Income(incomeCategories, incomePayments);
                totalExpense = new Expense(expenseCategories, expensePayments);
                months.clear();
                addMonth();
                loadView();
            }
        }, null);

        // When an item of the list view is selected.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TEST", "Yearly Summary item clicked");
                Intent intent = new Intent(YearlySummaryActivity.this, MonthlySummaryActivity.class);
                intent.putExtra("year",yearlySummaryAdapter.getItem(position).getYear());
                intent.putExtra("month",yearlySummaryAdapter.getItem(position).getMonth());
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.yearYearlySummary)
    public void setSelectMonthYearClicked() {
        yearDialog.show();
    }

    private void addMonth() {
        for (int i = 0; i < NUM_OF_MONTHS; ++i) {
            Month month = new Month();
            month.setIncome(loadIncome(i));
            month.setExpense(loadExpense(i));
            month.setYear(year);
            month.setMonth(i);
            month.setMonthString(MONTHS[i]);
            months.add(month);
        }
    }

    /**
     * Load income data from the table.
     * @param month
     *          month
     * @return
     *          income
     */
    private Transaction loadIncome(int month) {
        double[] incomeCategories = {0,0,0,0,0,0,};
        double[] incomePayments = {0,0,0,0};
        Transaction income = new Income(incomeCategories, incomePayments);

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
                        ActionContract.ActionEntry.COLUMN_NAME_MONTH + " = ?";
        String[] selectionArgs = {INCOME, year + "", month + ""};

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

    /**
     * Load expense data from the table.
     * @param month
     *          month
     * @return
     *          expense
     */
    private Transaction loadExpense(int month) {
        double[] expenseCategories = {0,0,0,0,0,0,0,0,0,0};
        double[] expensePayments = {0,0,0,0};
        Transaction expense = new Expense(expenseCategories, expensePayments);

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
                        ActionContract.ActionEntry.COLUMN_NAME_MONTH + " = ?";
        String[] selectionArgs = {EXPENSE, year + "", month + ""};

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
        for (int i = 0; i < months.size(); ++i) {
            incomeTotal = incomeTotal + months.get(i).getIncome().getTotalAmount();
        }
        // Expense total
        for (int i = 0; i < months.size(); ++i) {
            expenseTotal = expenseTotal + months.get(i).getExpense().getTotalAmount();
        }

        netTotal = incomeTotal - expenseTotal;
        Log.i("NET TOTAL", netTotal + "");

        Double truncatedExpenseTotal = BigDecimal.valueOf(expenseTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedIncomeTotal = BigDecimal.valueOf(incomeTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();
        Double truncatedNetTotal = BigDecimal.valueOf(netTotal).setScale(3, RoundingMode.HALF_UP).doubleValue();

        expenseAmountTextView.setText(truncatedExpenseTotal + "");
        incomeAmountTextView.setText(truncatedIncomeTotal + "");
        netAmountTextView.setText(truncatedNetTotal + "");

        yearlySummaryAdapter = new YearlySummaryAdapter(this, R.layout.listview_yearly_summary, months);
        listView.setAdapter(yearlySummaryAdapter);
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
                Intent intent = new Intent(YearlySummaryActivity.this, ChartActivity.class);
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
