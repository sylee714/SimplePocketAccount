package com.example.mingkie.simplepocketaccount.MainActivities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

@TargetApi(Build.VERSION_CODES.N)
public class ExpenseActivity extends AppCompatActivity {
    @BindView(R.id.expenseWarning)
    TextView warningMessage;
    @BindView(R.id.amountExpenseEditView)
    EditText amountEditText;
    @BindView(R.id.dateExpenseTextView)
    TextView date;
    @BindView(R.id.submitExpenseButton)
    Button submitButton;
    @BindView(R.id.categoryExpenseSpinner)
    Spinner categorySpinner;
    @BindView(R.id.paymentMethodExpenseSpinner)
    Spinner paymentSpinner;
    @BindView(R.id.notesExpenseEditText)
    EditText notesEditText;
    @BindView(R.id.expenseBottomNavigation)
    BottomNavigationView bottomNavigationView;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private ActionDBHelper actionDBHelper;
    private Calendar calendar;
    private final String ACTION_TYPE = "Expense";
    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    private int weekOfMonth;
    private int weekOfYear;
    private String category;
    private String payment;
    private String notes;
    private double amount;

    private ArrayAdapter<CharSequence> categoryAdapter;
    private ArrayAdapter<CharSequence> paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Shows activity_expense
        setContentView(R.layout.activity_expense);
        // Lets use Butterknife
        ButterKnife.bind(this);
        // Instantiate to access the database
        actionDBHelper = new ActionDBHelper(this);
        // Sets the title of the activity as 'Add Expense'
        setTitle(R.string.title_activity_expense);
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

        categoryAdapter = ArrayAdapter.createFromResource(this, R.array.list_expense_category, R.layout.spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        paymentAdapter = ArrayAdapter.createFromResource(this, R.array.list_payment_method, R.layout.spinner_item);
        paymentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        paymentSpinner.setAdapter(paymentAdapter);

        // Displays the bottom navigation bar.
        displayBottomBar();

        categorySpinnerSetOnItemSelected();
        paymentSpinnerSetOnItemSelected();

        // When mDateSetListener is called
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(month + 1 + "/" + dayOfMonth + "/" + year);
            }
        };
    }

    @OnClick(R.id.notesExpenseEditText)
    public void notesClicked() {
        showInputDialog();
    }

    protected void showInputDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ExpenseActivity.this);
        View promptView = layoutInflater.inflate(R.layout.notes_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpenseActivity.this);
        alertDialogBuilder.setView(promptView);
        final EditText editText = (EditText) promptView.findViewById(R.id.notesDialogEditText);
        editText.setText(notesEditText.getText());
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        notesEditText.setText(editText.getText());
                    }
                })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb (0, 153, 153));
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb (0, 153, 153));
    }

    public void categorySpinnerSetOnItemSelected() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void paymentSpinnerSetOnItemSelected() {
        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payment = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // method: currentDateClicked()
    // purpose: Sets the date as the chosen date.
    @OnClick(R.id.dateExpenseTextView)
    public void currentDateClicked() {
        DatePickerDialog dialog = new DatePickerDialog(ExpenseActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener,
                year, month, dayOfMonth);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    @OnClick(R.id.submitExpenseButton)
    public void submitButtonClicked() {
        submitButton.setBackgroundColor(Color.GRAY);
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
            notes = notesEditText.getText().toString();

            Action action = createAction();
            insertAction(action);
            amountEditText.setHint("Amount");
            amountEditText.setText(null);
            notesEditText.setHint("Notes");
            notesEditText.setText(null);

        } else {
            warningMessage.setVisibility(View.VISIBLE);
            amountEditText.setHint("Amount");
            amountEditText.setText(null);
        }
        submitButton.setBackgroundColor(Color.rgb (0, 153, 153));

    }

    public Action createAction() {
        Action action = new Action();
        action.setActionType(ACTION_TYPE);
        action.setAmount(amount);
        action.setCategory(category);
        action.setPayment(payment);
        action.setNotes(notes);
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
        values.put(ActionContract.ActionEntry.COLUMN_NAME_CATEGORY, action.getCategory());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_PAYMENT, action.getPayment());
        values.put(ActionContract.ActionEntry.COLUMN_NAME_NOTES, action.getNotes());
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
                        Toast.makeText(ExpenseActivity.this, "Action Add Action Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_add_income:
                        Toast.makeText(ExpenseActivity.this, "Action Add Income Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(ExpenseActivity.this, IncomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_view_summary:
                        Toast.makeText(ExpenseActivity.this, "Action View Summary Clicked", Toast.LENGTH_SHORT).show();
                        intent = new Intent(ExpenseActivity.this, SummaryActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
}
