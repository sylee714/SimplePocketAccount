package com.example.mingkie.simplepocketaccount.Data;

/**
 * Created by MingKie on 7/12/2017.
 */

public class Transaction {

    private String transactionType;
    private int year;
    private int month;
    private int dayOfMonth;
    private int weekOfYear;
    private String type;
    private int amount;

    public Transaction(String transactionType, int year, int month, int dayOfMonth,
                       int weekOfYear, String type, int amount) {
        this.transactionType = transactionType;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.weekOfYear = weekOfYear;
        this.type = type;
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
