package com.syl.mingkie.simplepocketaccount.Data;

/**
 * Record of a week.
 */
public class Week extends Record {
    private int weekOfMonth;
    private int weekOfYear;

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(int weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }
}
