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

public class MonthlySummaryActivity extends AppCompatActivity{

    @BindView(R.id.monthYearMonthlySummary)
    TextView monthYear;
    @BindView(R.id.incomeAmountMonthlySummary)
    TextView incomeAmountTextView;
    @BindView(R.id.expenseAmountMonthlySummary)
    TextView expenseAmountTextView;
    @BindView(R.id.netAmountMonthlySummary)
    TextView netAmountTextView;
    @BindView(R.id.listViewMonthlySummary)
    ListView listView;

    private final String INCOME = "Income";
    private final String EXPENSE = "Expense";

    private Calendar calendar;

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

    private MonthlySummaryAdapter monthlySummaryAdapter;

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

        incomeTotal = 0;
        expenseTotal = 0;
        netTotal = 0;

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
                weeks.clear();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                numberOfWeeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
                incomeTotal = 0;
                expenseTotal = 0;
                netTotal = 0;
                addWeek();
                loadView();
            }
        }, null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TEST", "Position: " + position);
                Calendar c = Calendar.getInstance();
                Log.i("TEST", "Monthly Summary item clicked");
                Day day = findDay(position);
                Intent intent = new Intent(MonthlySummaryActivity.this, WeeklySummaryActivity.class);
                intent.putExtra("year",monthlySummaryAdapter.getItem(position).getYear());
                intent.putExtra("month",monthlySummaryAdapter.getItem(position).getMonth());
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.WEEK_OF_YEAR, monthlySummaryAdapter.getItem(position).getWeekOfYear());
                c.set(Calendar.DAY_OF_WEEK, 1);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                Log.i("TEST", "First Day: " + dayOfMonth);
                intent.putExtra("dayOfMonth", day.getDayOfMonth());
                startActivity(intent);

            }
        });
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

    private void addDay() {
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


    private void addWeek() {
        Calendar c = Calendar.getInstance();
        Log.i("TEST", "Add Week");
        for (int i = 1; i <= numberOfWeeks; ++i) {
            Week week = new Week();
            week.setIncome(loadIncome(i));
            week.setExpense(loadExpense(i));
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

    private Income loadIncome(int weekNum) {

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
                        ActionContract.ActionEntry.COLUMN_NAME_MONTH + " = ?" + " and " +
                        ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH + " = ?";
        String[] selectionArgs = {INCOME, year + "", month + "", weekNum + ""};

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

    private Expense loadExpense(int weekNum) {

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
                        ActionContract.ActionEntry.COLUMN_NAME_MONTH + " = ?" + " and " +
                        ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH + " = ?";
        String[] selectionArgs = {EXPENSE, year + "", month + "", weekNum + ""};

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
        for (int i = 0; i < weeks.size(); ++i) {
            incomeTotal = incomeTotal + weeks.get(i).getIncome().getTotalAmount();
        }
        // Expense total
        for (int i = 0; i < weeks.size(); ++i) {
            expenseTotal = expenseTotal + weeks.get(i).getExpense().getTotalAmount();
        }

        netTotal = incomeTotal - expenseTotal;
        Log.i("NET TOTAL", netTotal + "");

        Double truncatedExpenseTotal = BigDecimal.valueOf(expenseTotal).setScale(4, RoundingMode.HALF_UP).doubleValue();
        Double truncatedIncomeTotal = BigDecimal.valueOf(incomeTotal).setScale(4, RoundingMode.HALF_UP).doubleValue();
        Double truncatedNetTotal = BigDecimal.valueOf(netTotal).setScale(4, RoundingMode.HALF_UP).doubleValue();

        expenseAmountTextView.setText(truncatedExpenseTotal + "");
        incomeAmountTextView.setText(truncatedIncomeTotal + "");
        netAmountTextView.setText(truncatedNetTotal + "");

        monthlySummaryAdapter = new MonthlySummaryAdapter(this, R.layout.listview_monthly_summary, weeks);
        listView.setAdapter(monthlySummaryAdapter);
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
