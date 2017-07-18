package com.example.mingkie.simplepocketaccount.Data;

import android.util.Log;

/**
 * This class represents an expense transaction.
 */
public class Expense extends Transaction {
    public Expense(double[] categories, double[] payments) {
        super(categories, payments);
    }

    public void addCategories(String category, double amount) {
        if (category.equals("Housing")) {
            setCategory(0, getCategory(0) + amount);
        } else if (category.equals("Utilities")) {
            setCategory(1, getCategory(1) + amount);
        } else if (category.equals("Food")){
            setCategory(2, getCategory(2) + amount);
        } else if (category.equals("Clothing")){
            setCategory(3, getCategory(3) + amount);
        } else if (category.equals("Medical/Healthcare")) {
            setCategory(4, getCategory(4) + amount);
        } else if (category.equals("Donations/Gifts to Charity")){
            setCategory(5, getCategory(5) + amount);
        } else if (category.equals("Savings and Insurance")){
            setCategory(6, getCategory(6) + amount);
        } else if (category.equals("Entertainment and Recreation")) {
            setCategory(7, getCategory(7) + amount);
        } else if (category.equals("Transportation")){
            setCategory(8, getCategory(8) + amount);
        } else if (category.equals("Personal/Debt Payments/Misc")){
            setCategory(9, getCategory(9) + amount);
        }
    }
}
