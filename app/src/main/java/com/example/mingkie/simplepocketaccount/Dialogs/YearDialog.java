package com.example.mingkie.simplepocketaccount.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.NumberPicker;

import com.example.mingkie.simplepocketaccount.R;

import java.util.Calendar;

/**
 * Created by MingKie on 7/7/2017.
 */

public class YearDialog {
    private static final int MIN_YEAR = 1970;
    private static final int MAX_YEAR = 2099;

    private View view;
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog pickerDialog;
    private boolean build = false;
    private NumberPicker yearNumberPicker;

    /**
     * Instantiates a new month year picker.
     *
     * @param activity
     *            the activity
     */
    public YearDialog(Activity activity) {
        this.activity = activity;
        this.view = activity.getLayoutInflater().inflate(R.layout.activity_year_dialog, null);
    }

    /**
     * Builds the month year alert dialog.
     *
     * @param positiveButtonListener
     *            the positive listener
     * @param negativeButtonListener
     *            the negative listener
     */
    public void build(DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
        this.build(-1, positiveButtonListener, negativeButtonListener);
    }

    private int currentYear;


    /**
     * Builds the month year alert dialog.
     *
     * @param selectedYear
     *            the selected year 1970 to 2099 (sets current year if invalid
     *            value)
     * @param positiveButtonListener
     *            the positive listener
     * @param negativeButtonListener
     *            the negative listener
     */
    public void build(int selectedYear, DialogInterface.OnClickListener positiveButtonListener,
                      DialogInterface.OnClickListener negativeButtonListener) {

        final Calendar instance = Calendar.getInstance();
        currentYear = instance.get(Calendar.YEAR);


        if (selectedYear < MIN_YEAR || selectedYear > MAX_YEAR) {
            selectedYear = currentYear;
        }

        if (selectedYear == -1) {
            selectedYear = currentYear;
        }

        builder = new AlertDialog.Builder(activity);
        builder.setView(view);

        yearNumberPicker = (NumberPicker) view.findViewById(R.id.yearNumberPicker);
        yearNumberPicker.setMinValue(MIN_YEAR);
        yearNumberPicker.setMaxValue(MAX_YEAR);

        yearNumberPicker.setValue(selectedYear);

        yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        builder.setTitle(activity.getString(R.string.alert_dialog_title));
        builder.setPositiveButton(activity.getString(R.string.positive_button_text), positiveButtonListener);
        builder.setNegativeButton(activity.getString(R.string.negative_button_text), negativeButtonListener);
        build = true;
        pickerDialog = builder.create();

    }

    /**
     * Show month year picker dialog.
     */
    public void show() {
        if (build) {
            pickerDialog.show();
            pickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb (0, 153, 153));
            pickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb (0, 153, 153));
        } else {
            throw new IllegalStateException("Build picker before use");
        }
    }

    /**
     * Gets the selected year.
     *
     * @return the selected year
     */
    public int getSelectedYear() {
        return yearNumberPicker.getValue();
    }

    /**
     * Gets the current year.
     *
     * @return the current year
     */
    public int getCurrentYear() {
        return currentYear;
    }


    /**
     * Sets the year value changed listener.
     *
     * @param valueChangeListener
     *            the new year value changed listener
     */
    public void setYearValueChangedListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        yearNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    /**
     * Sets the year wrap selector wheel.
     *
     * @param wrapSelectorWheel
     *            the new year wrap selector wheel
     */
    public void setYearWrapSelectorWheel(boolean wrapSelectorWheel) {
        yearNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }
}
