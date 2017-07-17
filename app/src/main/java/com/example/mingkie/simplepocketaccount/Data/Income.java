package com.example.mingkie.simplepocketaccount.Data;

import android.util.Log;

/**
 * Created by MingKie on 7/13/2017.
 */

public class Income {
    private double[] categories;
    private double[] payments;

    public Income() {
        categories = new double[6];
        payments = new double[4];
        initializeCategories();
        initializePayments();
    }


    private void initializeCategories() {
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = 0;
        }
    }

    private void initializePayments() {
        for (int i = 0; i < payments.length; ++i) {
            payments[i] = 0;
        }
    }

    public void add(String category, String payment, double amount) {
        addCategories(category, amount);
        addPayments(payment, amount);
    }


    private void addCategories(String category, double amount) {
        if (category.equals("Salary")) {
            categories[0] = categories[0] + amount;
            Log.i("Type: ", "Salary");
        } else if (category.equals("Business/Profession")) {
            categories[1] = categories[1] + amount;
            Log.i("Type: ", "Business/Profession");
        } else if (category.equals("Capital Gain")){
            categories[2] = categories[2] + amount;
            Log.i("Type: ", "Capital Gain");
        } else if (category.equals("House Property")){
            categories[3] = categories[3] + amount;
            Log.i("Type: ", "House Property");
        } else if (category.equals("Gifts")) {
            categories[4] = categories[4] + amount;
            Log.i("Type: ", "Gifts");
        } else if (category.equals("Others")){
            categories[5] = categories[5] + amount;
            Log.i("Type: ", "Others");
        }
    }

    private void addPayments(String payment, double amount) {
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
