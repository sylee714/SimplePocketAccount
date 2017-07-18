package com.syl.mingkie.simplepocketaccount.Data;

import android.util.Log;

/**
 * This class represents an income transaction.
 */
public class Income extends Transaction {
    public Income(double[] categories, double[] payments) {
        super(categories, payments);
    }

    public void addCategories(String category, double amount) {
        if (category.equals("Salary")) {
            setCategory(0, getCategory(0) + amount);
        } else if (category.equals("Business/Profession")) {
            setCategory(1, getCategory(1) + amount);
        } else if (category.equals("Capital Gain")){
            setCategory(2, getCategory(2) + amount);
        } else if (category.equals("House Property")){
            setCategory(3, getCategory(3) + amount);
        } else if (category.equals("Gifts")) {
            setCategory(4, getCategory(4) + amount);
        } else if (category.equals("Others")){
            setCategory(5, getCategory(5) + amount);
        }
    }
}
