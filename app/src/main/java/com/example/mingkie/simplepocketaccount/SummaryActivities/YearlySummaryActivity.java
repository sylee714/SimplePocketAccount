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
import com.example.mingkie.simplepocketaccount.ArrayAdapters.YearlySummaryAdapter;
import com.example.mingkie.simplepocketaccount.Data.Action;
import com.example.mingkie.simplepocketaccount.Data.ActionContract;
import com.example.mingkie.simplepocketaccount.Data.ActionDBHelper;
import com.example.mingkie.simplepocketaccount.Data.Expense;
import com.example.mingkie.simplepocketaccount.Data.Income;
import com.example.mingkie.simplepocketaccount.Data.Month;
import com.example.mingkie.simplepocketaccount.Dialogs.YearDialog;
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
 * Created by MingKie on 6/30/2017.
 */

public class YearlySummaryActivity extends AppCompatActivity{

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

    //private int month;
    private int year;

    private final int NUM_OF_MONTHS = 12;
    private final String[] MONTHS  = new String[]{"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December" };

    private List<Month> months;

    private double incomeTotal;
    private double expenseTotal;
    private double netTotal;

    private SQLiteDatabase db;
    private ActionDBHelper actionDBHelper;

    private YearlySummaryAdapter yearlySummaryAdapter;

    private YearDialog yearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_summary);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_yearly_summary);

        ButterKnife.bind(this);

        months = new ArrayList<Month>();

        incomeTotal = 0;
        expenseTotal = 0;
        netTotal = 0;

        actionDBHelper = new ActionDBHelper(this);
        db = actionDBHelper.getReadableDatabase();

        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        yearTextView.setText(year + "");

        addMonth();
        loadView();

        yearDialog = new YearDialog(this);
        yearDialog.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedYear = yearDialog.getSelectedYear();
                yearTextView.setText(selectedYear + "");
                year = selectedYear;
                incomeTotal = 0;
                expenseTotal = 0;
                netTotal = 0;
                months.clear();
                addMonth();
                loadView();
            }
        }, null);

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

    private Income loadIncome(int month) {

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
                ActionContract.ActionEntry.COLUMN_NAME_TYPE,
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
            String type = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_TYPE));
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
            action.setType(type);
            action.setAmount(amount);

            addIncome(income, action.getType(), action.getAmount());
            Log.i("type: ", action.getType() + "");
            Log.i("amount: ", action.getAmount() + "");

            //incomeList.add(action);

        }

        cursor.close();

        /*
        for (int i = 0; i < incomeList.size(); ++i) {
            Log.i("Action Type: ", incomeList.get(i).getActionType() + "");
            Log.i("Year: ", incomeList.get(i).getYear() + "");
            Log.i("Month: ", incomeList.get(i).getMonth() + "");
            Log.i("day of month: ", incomeList.get(i).getDayOfMonth() + "");
            Log.i("day of week: ", incomeList.get(i).getDayOfWeek() + "");
            Log.i("week of month: ", incomeList.get(i).getWeekOfMonth() + "");
            Log.i("week of year: ", incomeList.get(i).getWeekOfYear() + "");
            Log.i("type: ", incomeList.get(i).getType() + "");
            Log.i("amount: ", incomeList.get(i).getAmount() + "");
        }
        */

        return income;
    }

    private void addIncome(Income income, String type, double amount) {

        Log.i("Add Income", "Called");

        if (type.equals("Cash")) {
            income.addCash(amount);
            Log.i("Type: ", "Cash");
        } else if (type.equals("Check")) {
            income.addCheck(amount);
            Log.i("Type: ", "Check");
        } else if (type.equals("Electronic money")) {
            income.addEMoney(amount);
            Log.i("Type: ", "E money");
        }

    }

    private Expense loadExpense(int month) {

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
                ActionContract.ActionEntry.COLUMN_NAME_TYPE,
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
            String type = cursor.getString(cursor.getColumnIndexOrThrow(ActionContract.ActionEntry.COLUMN_NAME_TYPE));
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
            action.setType(type);
            action.setAmount(amount);

            addExpense(expense, action.getType(), action.getAmount());
            Log.i("Food: ", action.getType().equals("Food") + "");
            Log.i("amount: ", action.getAmount() + "");

            //expenseList.add(action);

        }

        cursor.close();

        /*
        for (int i = 0; i < expenseList.size(); ++i) {
            Log.i("Action Type: ", expenseList.get(i).getActionType() + "");
            Log.i("Year: ", expenseList.get(i).getYear() + "");
            Log.i("Month: ", expenseList.get(i).getMonth() + "");
            Log.i("day of month: ", expenseList.get(i).getDayOfMonth() + "");
            Log.i("day of week: ", expenseList.get(i).getDayOfWeek() + "");
            Log.i("week of month: ", expenseList.get(i).getWeekOfMonth() + "");
            Log.i("week of year: ", expenseList.get(i).getWeekOfYear() + "");
            Log.i("type: ", expenseList.get(i).getType() + "");
            Log.i("amount: ", expenseList.get(i).getAmount() + "");
        }
        */

        return expense;

    }

    private void addExpense(Expense expense, String type, double amount) {

        Log.i("Add Expense", "Called");

        if (type.equals("Housing")) {
            expense.addHousing(amount);
            Log.i("Type: ", "Housing");
        } else if (type.equals("Utilities")) {
            expense.addUtility(amount);
            Log.i("Type: ", "Uti");
        } else if (type.equals("Food")){
            expense.addFood(amount);
            Log.i("Type: ", "Food");
        } else if (type.equals("Clothing")){
            expense.addClothing(amount);
            Log.i("Type: ", "Clothing");
        } else if (type.equals("Medical/Healthcare")) {
            expense.addMedical(amount);
            Log.i("Type: ", "Medi");
        } else if (type.equals("Donations/Gifts to Charity")){
            expense.addDonation(amount);
            Log.i("Type: ", "Dona");
        } else if (type.equals("Savings and Insurance")){
            expense.addSaving(amount);
            Log.i("Type: ", "SAv");
        } else if (type.equals("Entertainment and Recreation")) {
            expense.addEntertain(amount);
            Log.i("Type: ", "Enter");
        } else if (type.equals("Transportation")){
            expense.addTransportation(amount);
            Log.i("Type: ", "Trans");
        } else if (type.equals("Personal/Debt Payments/Misc")){
            expense.addPersonal(amount);
            Log.i("Type: ", "Persona");
        }

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

        Double truncatedExpenseTotal = BigDecimal.valueOf(expenseTotal).setScale(4, RoundingMode.HALF_UP).doubleValue();
        Double truncatedIncomeTotal = BigDecimal.valueOf(incomeTotal).setScale(4, RoundingMode.HALF_UP).doubleValue();
        Double truncatedNetTotal = BigDecimal.valueOf(netTotal).setScale(4, RoundingMode.HALF_UP).doubleValue();

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
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
