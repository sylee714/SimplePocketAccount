package com.syl.mingkie.simplepocketaccount.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;

import com.syl.mingkie.simplepocketaccount.R;

import java.util.Calendar;

/**
 * This class is a custom month and year picker dialog.
 */
public class MonthYearCustomDialog extends CustomDialog {
    private NumberPicker monthNumberPicker;
    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[] { "Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    private static final String[] MONTHS = new String[] { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December" };

    /**
     * Instantiates a new month year picker.
     *
     * @param activity
     *            the activity
     */
    public MonthYearCustomDialog(Activity activity) {
        super(activity);
        setView(activity.getLayoutInflater().inflate(R.layout.activity_month_year_dialog, null));
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
        this.build(-1, -1, positiveButtonListener, negativeButtonListener);
    }

    private int currentMonth;

    /**
     * Builds the month year alert dialog.
     *
     * @param selectedMonth
     *            the selected month 0 to 11 (sets current moth if invalid
     *            value)
     * @param selectedYear
     *            the selected year 1970 to 2099 (sets current year if invalid
     *            value)
     * @param positiveButtonListener
     *            the positive listener
     * @param negativeButtonListener
     *            the negative listener
     */
    public void build(int selectedMonth, int selectedYear, DialogInterface.OnClickListener positiveButtonListener,
                      DialogInterface.OnClickListener negativeButtonListener) {
        final Calendar instance = Calendar.getInstance();
        currentMonth = instance.get(Calendar.MONTH);
        setCurrentYear(instance.get(Calendar.YEAR));

        if (selectedMonth > 11 || selectedMonth < -1) {
            selectedMonth = currentMonth;
        }

        if (selectedYear < getMinYear() || selectedYear > getMaxYear()) {
            selectedYear = getCurrentYear();
        }

        if (selectedMonth == -1) {
            selectedMonth = currentMonth;
        }

        if (selectedYear == -1) {
            selectedYear = getCurrentYear();
        }

        setBuilder(new AlertDialog.Builder(getActivity()));
        getBuilder().setView(getView());

        monthNumberPicker = (NumberPicker) getView().findViewById(R.id.monthNumberPicker);
        monthNumberPicker.setDisplayedValues(PICKER_DISPLAY_MONTHS_NAMES);

        monthNumberPicker.setMinValue(0);
        monthNumberPicker.setMaxValue(MONTHS.length - 1);

        setYearNumberPicker((NumberPicker) getView().findViewById(R.id.yearNumberPicker));
        getYearNumberPicker().setMinValue(getMinYear());
        getYearNumberPicker().setMaxValue(getMaxYear());

        monthNumberPicker.setValue(selectedMonth);
        getYearNumberPicker().setValue(selectedYear);

        monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        getYearNumberPicker().setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        getBuilder().setTitle(getActivity().getString(R.string.alert_dialog_title));
        getBuilder().setPositiveButton(getActivity().getString(R.string.positive_button_text), positiveButtonListener);
        getBuilder().setNegativeButton(getActivity().getString(R.string.negative_button_text), negativeButtonListener);
        setBuild(true);
        setPickerDialog(getBuilder().create());
    }

    /**
     * Gets the selected month.
     *
     * @return the selected month
     */
    public int getSelectedMonth() {
        return monthNumberPicker.getValue();
    }

    /**
     * Gets the selected month name.
     *
     * @return the selected month name
     */
    public String getSelectedMonthName() {
        return MONTHS[monthNumberPicker.getValue()];
    }

    /**
     * Gets the selected month name.
     *
     * @return the selected month short name i.e Jan, Feb ...
     */
    public String getSelectedMonthShortName() {
        return PICKER_DISPLAY_MONTHS_NAMES[monthNumberPicker.getValue()];
    }

    /**
     * Gets the current month.
     *
     * @return the current month
     */
    public int getCurrentMonth() {
        return currentMonth;
    }

    /**
     * Sets the month value changed listener.
     *
     * @param valueChangeListener
     *            the new month value changed listener
     */
    public void setMonthValueChangedListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        monthNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    /**
     * Sets the month wrap selector wheel.
     *
     * @param wrapSelectorWheel
     *            the new month wrap selector wheel
     */
    public void setMonthWrapSelectorWheel(boolean wrapSelectorWheel) {
        monthNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }
}
