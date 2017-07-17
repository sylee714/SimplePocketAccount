package com.example.mingkie.simplepocketaccount.Data;

import android.util.Log;

/**
 * Created by MingKie on 7/16/2017.
 */

public abstract class Trasaction {
    private double[] categories;
    private double[] payments;

    public void initializeCategories() {
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = 0;
        }
    }

    public void initializePayments() {
        for (int i = 0; i < payments.length; ++i) {
            payments[i] = 0;
        }
    }

    public void add(String category, String payment, double amount) {
        addCategories(category, amount);
        addPayments(payment, amount);
    }

    public abstract void addCategories(String categorym, double amount);

    public void addPayments(String payment, double amount) {
        Log.i("Add Income", "Called");
        if (payment.equals("Cash")) {
            payments[0] = payments[0] + amount;
            Log.i("Type: ", "Cash");
        } else if (payment.equals("Check")) {
            payments[1] = payments[1] + amount;
            Log.i("Type: ", "Check");
        } else if (payment.equals("Debit/Credit Card")) {
            payments[2] = payments[2] + amount;
            Log.i("Type: ", "Debit/Credit Card");
        } else if (payment.equals("Others")) {
            payments[3] = payments[3] + amount;
            Log.i("Type: ", "Others");
        }
    }

    public double[] getCategories() {
        return categories;
    }

    public double[] getPayments() {
        return payments;
    }

    public double getTotalAmount() {
        double totalAmount = 0;
        for (int i = 0; i < payments.length; ++i) {
            totalAmount = totalAmount + payments[i];
        }
        return totalAmount;
    }
}
