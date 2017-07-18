package com.syl.mingkie.simplepocketaccount.Data;

/**
 * This class records all transactions of days, weeks, or months.
 */
public abstract class Record {
    private Transaction income;
    private Transaction expense;
    private int month;
    private int year;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Transaction getIncome() {
        return income;
    }

    public void setIncome(Transaction income) {
        this.income = income;
    }

    public Transaction getExpense() {
        return expense;
    }

    public void setExpense(Transaction expense) {
        this.expense = expense;
    }
}
