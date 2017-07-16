package com.example.mingkie.simplepocketaccount.MainActivities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mingkie.simplepocketaccount.Data.Action;
import com.example.mingkie.simplepocketaccount.Data.ActionContract;
import com.example.mingkie.simplepocketaccount.Data.ActionDBHelper;
import com.example.mingkie.simplepocketaccount.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MingKie on 6/28/2017.
 */

public class IncomeActivity extends AppCompatActivity {

    @BindView(R.id.incomeWarning)
    TextView warningMessage;
    @BindView(R.id.amountIncomeEditView)
    EditText amountEditText;
    @BindView(R.id.dateIncomeTextView)
    TextView date;
    @BindView(R.id.submitIncomeButton)
    Button submitButton;
    @BindView(R.id.typeIncomeSpinner)
    Spinner moneyTypeSpinner;
    @BindView(R.id.incomeBottomNavigation)
    BottomNavigationView bottomNavigationView;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private ActionDBHelper actionDBHelper;
    private Calendar calendar;
    private final String ACTION_TYPE = "Income";
    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    private int weekOfMonth;
    private int weekOfYear;
    private String type;
    private double amount;

    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Shows activity_expense
        setContentView(R.layout.activity_income);
        // Lets use Butterknife
        ButterKnife.bind(this);
        // Instantiate to access the database
        actionDBHelper = new ActionDBHelper(this);
        // Sets the title of the activity as 'Main'
        setTitle(R.string.title_activity_income);
        // Displays current date
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        // Adds 1 to month since it counts from 0 - 11, not 1 - 12
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        Log.i("Year", year + "");
        Log.i("Month", month + "");
        Log.i("Day of Month", dayOfMonth + "");
        Log.i("Day of Week", dayOfWeek + "");
        Log.i("Week of Month", weekOfMonth + "");
        Log.i("Week of Year", weekOfYear + "");
        // Sets date to current date which is the default value
        date.setText(month + 1 + "/" + dayOfMonth + "/" + year);


        adapter = ArrayAdapter.createFromResource(this, R.array.list_money_type, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        moneyTypeSpinner.setAdapter(adapter);

        // Displays the bottom navigation bar.
        displayBottomBar();

        incomeSpinnerSetOnItemSelected();

        // When mDateSetListener is called
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(month + 1 + "/" + dayOfMonth + "/" + year);
            }
        };
    }

    public void incomeSpinnerSetOnItemSelected() {
        moneyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // method: currentDateClicked()
    // purpose: Sets the date as the chosen date.
    @OnClick(R.id.dateIncomeTextView)
    public void currentDateClicked() {
        DatePickerDialog dialog = new DatePickerDialog(IncomeActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                year, month, dayOfMonth);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static boolean isNum(String strNum) {
        boolean ret = true;
        try {

            Double.parseDouble(strNum);

        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

    @OnClick(R.id.submitIncomeButton)
    public void submitButtonClicked() {

        String amountString = amountEditText.getText().toString();

        if (!amountString.equals("") || isNum(amountString)) {
            warningMessage.setVisibility(View.INVISIBLE);
            String dateString = date.getText().toString();
            String[] data = dateString.split("/");
            // Subtracts 1 since month counts from 0 - 11 not 1 - 12
            month = Integer.parseInt(data[0]) - 1;
            Log.i("Month", month + "");
            dayOfMonth = Integer.parseInt(data[1]);
            Log.i("Day of Month", dayOfMonth + "");
            year = Integer.parseInt(data[2]);
            Log.i("Year", year + "");

            calendar.set(year, month, dayOfMonth);

            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
            weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
            Log.i("Day of Week", dayOfWeek + "");
            Log.i("Week of Month", weekOfMonth + "");
            Log.i("Week of Year", weekOfYear + "");

            amount = Double.parseDouble(amountEditText.getText().toString());

            Action action = createAction();
            insertAction(action);
            amountEditText.setHint("Amount");
            amountEditText.setText(null);

        } else {
            warningMessage.setVisibility(View.VISIBLE);
            amountEditText.setHint("Amount");
            amountEditText.setText(null);
        }
    }

    public Action createAction() {
        Action action = new Action();
        action.setActionType(ACTION_TYPE);
        action.setAmount(amount);
        action.setType(type);
        action.setYear(year);
        action.setMonth(month);
        action.setDayOfMonth(dayOfMonth);
        action.setDayOfWeek(dayOfWeek);
        action.setWeekOfMonth(weekOfMonth);
        action.setWeekOfYear(weekOfYear);
        return action;
    }

    public void insertAction(Action action) {
        // Gets the data repository in write mode
        SQLiteDatabase db = actionDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ActionContract.ActionEntry.COLUMN_NAME_ACTIONTYPE, action.getActionType());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_YEAR, action.getYear());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_MONTH, action.getMonth());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_DAYOFMONTH, action.getDayOfMonth());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_DAYOFWEEK, action.getDayOfWeek());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFMONTH, action.getWeekOfMonth());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_WEEKOFYEAR, action.getWeekOfYear());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_TYPE, action.getType());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_AMOUNT, action.getAmount());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ActionContract.ActionEntry.TABLE_NAME, null, values);
        Log.i("TEST", "New row id: " + newRowId);
    }

    // method: displayBottomBar()
    // purpose: Displays bottom navigation bar.
    public void displayBottomBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId())
                {
                    case R.id.action_add_expense:
                        Toast.makeText(IncomeActivity.this, "Action Add Action Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(IncomeActivity.this, ExpenseActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_add_income:
                        Toast.makeText(IncomeActivity.this, "Action Add Income Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_view_summary:
                        Toast.makeText(IncomeActivity.this, "Action View Summary Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(IncomeActivity.this, SummaryActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
}
