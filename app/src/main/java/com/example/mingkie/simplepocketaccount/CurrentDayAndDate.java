package com.example.mingkie.simplepocketaccount;

import android.annotation.TargetApi;
import android.icu.text.DateFormat;
import android.icu.text.DateFormatSymbols;
import android.os.Build;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MingKie on 6/30/2017.
 */

@TargetApi(Build.VERSION_CODES.N)
public class CurrentDayAndDate {
    private String weekdays[] = new DateFormatSymbols(Locale.ENGLISH).getWeekdays();
    private TextView dayTextView;
    private TextView dateTextView;

    public CurrentDayAndDate(TextView day, TextView date) {
        dayTextView = day;
        dateTextView = date;
    }

    public void setCurrentDayDate() {
        String currentDateString = DateFormat.getDateInstance().format(new Date());
        String currentDayString = weekdays[(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))];
        dateTextView.setText(currentDateString);
        dayTextView.setText(currentDayString);
    }


}
