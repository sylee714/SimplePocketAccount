package com.syl.mingkie.simplepocketaccount.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;

import com.syl.mingkie.simplepocketaccount.R;

import java.util.Calendar;

/**
 * This class is a custom year picker dialog.
 */
public class YearCustomDialog extends CustomDialog {
    /**
     * Instantiates a new month year picker.
     *
     * @param activity
     *            the activity
     */
    public YearCustomDialog(Activity activity) {
        super(activity);
        setView(activity.getLayoutInflater().inflate(R.layout.activity_year_dialog, null));
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
        setCurrentYear(instance.get(Calendar.YEAR));


        if (selectedYear < getMinYear() || selectedYear > getMaxYear()) {
            selectedYear = getCurrentYear();
        }

        if (selectedYear == -1) {
            selectedYear = getCurrentYear();
        }

        setBuilder(new AlertDialog.Builder(getActivity()));
        getBuilder().setView(getView());

        setYearNumberPicker((NumberPicker) getView().findViewById(R.id.yearNumberPicker));
        getYearNumberPicker().setMinValue(getMinYear());
        getYearNumberPicker().setMaxValue(getMaxYear());

        getYearNumberPicker().setValue(selectedYear);

        getYearNumberPicker().setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        getBuilder().setTitle(getActivity().getString(R.string.alert_dialog_title));
        getBuilder().setPositiveButton(getActivity().getString(R.string.positive_button_text), positiveButtonListener);
        getBuilder().setNegativeButton(getActivity().getString(R.string.negative_button_text), negativeButtonListener);
        setBuild(true);
        setPickerDialog(getBuilder().create());
    }
}
