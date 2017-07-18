package com.example.mingkie.simplepocketaccount.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.View;
import android.widget.NumberPicker;

/**
 * This class is used to create a custom datepicker dialog.
 */
public abstract class CustomDialog {
    private static final int MIN_YEAR = 1970;
    private static final int MAX_YEAR = 2099;
    private View view;
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog pickerDialog;
    private boolean build = false;
    private NumberPicker yearNumberPicker;

    private int currentYear;

    public CustomDialog(Activity activity){
        this.activity = activity;
    }

    public void setView(View view) {
        this.view = view;
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

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public static int getMinYear() {
        return MIN_YEAR;
    }

    public static int getMaxYear() {
        return MAX_YEAR;
    }

    public View getView() {
        return view;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(AlertDialog.Builder builder) {
        this.builder = builder;
    }

    public AlertDialog getPickerDialog() {
        return pickerDialog;
    }

    public void setPickerDialog(AlertDialog pickerDialog) {
        this.pickerDialog = pickerDialog;
    }

    public boolean isBuild() {
        return build;
    }

    public void setBuild(boolean build) {
        this.build = build;
    }

    public NumberPicker getYearNumberPicker() {
        return yearNumberPicker;
    }

    public void setYearNumberPicker(NumberPicker yearNumberPicker) {
        this.yearNumberPicker = yearNumberPicker;
    }
}
