package com.example.mingkie.simplepocketaccount.MainActivities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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

/**
 * This class activity lets the user to enter either income or expense
 * transactions.
 */
public class ExpenseIncomeActivity extends AppCompatActivity {
    @BindView(R.id.warningTextView)
    TextView warningMessage;
    @BindView(R.id.amountEditView)
    EditText amountEditText;
    @BindView(R.id.dateTextView)
    TextView date;
    @BindView(R.id.submitButton)
    Button submitButton;
    @BindView(R.id.categorySpinner)
    Spinner categorySpinner;
    @BindView(R.id.paymentMethodSpinner)
    Spinner paymentSpinner;
    @BindView(R.id.notesEditText)
    EditText notesEditText;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private boolean displayExpense;

    private ActionDBHelper actionDBHelper;

    private Calendar calendar;
    private String ACTION_TYPE;
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
        setContentView(R.layout.activity_expense_income);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        displayExpense = intent.getBooleanExtra("displayExpense", false);
        // Instantiate to access the database
        actionDBHelper = new ActionDBHelper(this);
        initialize();
        setSpinnerAdapter(displayExpense);
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

    private void initialize() {
        // Get current date
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        // Add 1 to month since it counts from 0 - 11, not 1 - 12
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
        // Set date to current date which is the default value
        date.setText(month + 1 + "/" + dayOfMonth + "/" + year);
        if (displayExpense) {
            Log.d("Action type", "Expense");
            ACTION_TYPE = "Expense";
        } else {
            Log.d("Action type", "Income");
            ACTION_TYPE = "Income";
        }
    }

    /**
     * Display corresponding spinners depending on the value of displayExpense
     * @param displayExpense
     *          true if 'Add Expense' is clicked; otherwise, false
     */
    private void setSpinnerAdapter(boolean displayExpense) {
        if (displayExpense) {
            setTitle(R.string.title_activity_expense);
            categoryAdapter = ArrayAdapter.createFromResource(this, R.array.list_expense_category, R.layout.spinner_item);
        } else {
            setTitle(R.string.title_activity_income);
            categoryAdapter = ArrayAdapter.createFromResource(this, R.array.list_income_category, R.layout.spinner_item);
        }
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        paymentAdapter = ArrayAdapter.createFromResource(this, R.array.list_payment_method, R.layout.spinner_item);
        paymentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        paymentSpinner.setAdapter(paymentAdapter);
    }

    /**
     * When date text view is clicked, show a calendar and let the user to choose
     * a date.
     */
    @OnClick(R.id.dateTextView)
    public void currentDateClicked() {
        DatePickerDialog dialog = new DatePickerDialog(ExpenseIncomeActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog, mDateSetListener,
                year, month, dayOfMonth);
        dialog.show();
    }

    /**
     * When note edite text is clicked, show a dialog to let the user to
     * type notes.
     */
    @OnClick(R.id.notesEditText)
    public void notesClicked() {
        showInputDialog();
    }

    /**
     * When submit button is clicked, get all the info and put them
     * into the database.
     */
    @OnClick(R.id.submitButton)
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
            setEditText();
        } else {
            warningMessage.setVisibility(View.VISIBLE);
            amountEditText.setHint("Amount");
            amountEditText.setText(null);
        }
        submitButton.setBackgroundColor(Color.rgb (0, 153, 153));
    }

    /**
     * Display notes dialog.
     */
    protected void showInputDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ExpenseIncomeActivity.this);
        View promptView = layoutInflater.inflate(R.layout.notes_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpenseIncomeActivity.this);
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

    private void categorySpinnerSetOnItemSelected() {
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

    private void paymentSpinnerSetOnItemSelected() {
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

    /**
     * Check if the value fo entered 'Amount' is a number.
     * @param strNum
     * @return
     */
    public static boolean isNum(String strNum) {
        boolean ret = true;
        try {
            Double.parseDouble(strNum);
        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

    /**
     * Set 'Amount' and 'Notes' back to their default values.
     */
    private void setEditText() {
        amountEditText.setHint("Amount");
        amountEditText.setText(null);
        notesEditText.setHint("Notes");
        notesEditText.setText(null);
    }

    public Action createAction() {
        Log.d("ExpenseIncomeActivity", "createAction called");
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

    /**
     * Display the bottom action bar.
     */
    public void displayBottomBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_add_expense:
                        Toast.makeText(ExpenseIncomeActivity.this, "Action Add Action Clicked", Toast.LENGTH_SHORT).show();
                        if (!displayExpense) {
                            displayExpense = true;
                            reset();
                        }
                        break;
                    case R.id.action_add_income:
                        Toast.makeText(ExpenseIncomeActivity.this, "Action Add Income Clicked", Toast.LENGTH_SHORT).show();
                        if (displayExpense) {
                            displayExpense = false;
                            reset();
                        }
                        break;
                    case R.id.action_view_summary:
                        Toast.makeText(ExpenseIncomeActivity.this, "Action View Summary Clicked", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        intent = new Intent(ExpenseIncomeActivity.this, SummaryActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    private void reset() {
        initialize();
        setEditText();
        setSpinnerAdapter(displayExpense);
    }
}
