package com.example.mingkie.simplepocketaccount.Data;

import android.util.Log;

/**
 * Created by MingKie on 7/13/2017.
 */

public class Expense {
    private double[] categories;
    private double[] payments;

    public Expense() {
        categories = new double[10];
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
        if (category.equals("Housing")) {
            categories[0] = categories[0] + amount;
            Log.i("Type: ", "Housing");
        } else if (category.equals("Utilities")) {
            categories[1] = categories[1] + amount;
            Log.i("Type: ", "Utilities");
        } else if (category.equals("Food")){
            categories[2] = categories[2] + amount;
            Log.i("Type: ", "Food");
        } else if (category.equals("Clothing")){
            categories[3] = categories[3] + amount;
            Log.i("Type: ", "Clothing");
        } else if (category.equals("Medical/Healthcare")) {
            categories[4] = categories[4] + amount;
            Log.i("Type: ", "Medical");
        } else if (category.equals("Donations/Gifts to Charity")){
            categories[5] = categories[5] + amount;
            Log.i("Type: ", "Donations");
        } else if (category.equals("Savings and Insurance")){
            categories[6] = categories[6] + amount;
            Log.i("Type: ", "Saving");
        } else if (category.equals("Entertainment and Recreation")) {
            categories[7] = categories[7] + amount;
            Log.i("Type: ", "Entertainment");
        } else if (category.equals("Transportation")){
            categories[8] = categories[8] + amount;
            Log.i("Type: ", "Transportation");
        } else if (category.equals("Personal/Debt Payments/Misc")){
            categories[9] = categories[9] + amount;
            Log.i("Type: ", "Personal");
        }
    }

    private void addPayments(String payment, double amount) {
        Log.i("Add Expense", "Called");
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
