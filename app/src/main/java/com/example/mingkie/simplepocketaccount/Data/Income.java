package com.example.mingkie.simplepocketaccount.Data;

/**
 * Created by MingKie on 7/13/2017.
 */

public class Income {

    private double totalAmount;
    private double cashAmount;
    private double checkAmount;
    private double eMoneyAmount;

    public Income() {
        totalAmount = 0;
        cashAmount = 0;
        checkAmount = 0;
        eMoneyAmount = 0;
    }

    public void addCash(double amount) {
        totalAmount = totalAmount + amount;
        cashAmount = cashAmount + amount;
    }

    public void addCheck(double amount) {
        totalAmount = totalAmount + amount;
        checkAmount = checkAmount + amount;
    }

    public void addEMoney(double amount) {
        totalAmount = totalAmount + amount;
        eMoneyAmount = eMoneyAmount + amount;
    }


    public double getTotalAmount() {
        return totalAmount;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public double getCheckAmount() {
        return checkAmount;
    }

    public double geteMoneyAmount() {
        return eMoneyAmount;
    }
}
