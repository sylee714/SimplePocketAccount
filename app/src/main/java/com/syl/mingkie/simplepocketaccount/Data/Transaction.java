package com.syl.mingkie.simplepocketaccount.Data;

import android.util.Log;

/**
 * This class represents a transaction.
 */
public abstract class Transaction {
    private double[] categories;
    private double[] payments;

    public Transaction(double[] categories, double[] payments) {
        this.categories = categories;
        this.payments = payments;
    }

    public void add(String category, String payment, double amount) {
        addCategories(category, amount);
        addPayments(payment, amount);
    }

    /**
     * Increase the amount of corresponding category.
     * @param category
     *          category
     * @param amount
     *          amount
     */
    public abstract void addCategories(String category, double amount);

    /**
     * Increase the amount of corresponding payment method.
     * @param payment
     *          payment method
     * @param amount
     *          amount
     */
    public void addPayments(String payment, double amount) {
        Log.i("Transaction", "addPayments called");
        if (payment.equals("Cash")) {
            payments[0] = payments[0] + amount;
        } else if (payment.equals("Check")) {
            payments[1] = payments[1] + amount;
        } else if (payment.equals("Debit/Credit Card")) {
            payments[2] = payments[2] + amount;
        } else if (payment.equals("Others")) {
            payments[3] = payments[3] + amount;
        }
    }

    public double[] getCategories() {
        return categories;
    }

    public void setCategory(int index, double amount) {
        categories[index] = amount;
    }

    public double getCategory(int index) {
        return categories[index];
    }

    public double[] getPayments() {
        return payments;
    }

    public double getPayment(int index) {
        return payments[index];
    }

    public double getTotalAmount() {
        double totalAmount = 0;
        for (int i = 0; i < payments.length; ++i) {
            totalAmount = totalAmount + payments[i];
        }
        return totalAmount;
    }
}
