package com.example.mingkie.simplepocketaccount.Data;

/**
 * Created by MingKie on 7/13/2017.
 */

public class Expense {

    private double totalAmount;
    private double housingAmount;
    private double utilitiesAmount;
    private double foodAmount;
    private double clothingAmount;
    private double medicalAmount;
    private double donationAmount;
    private double savingInsurAmount;
    private double entertainAmount;
    private double transportationAmount;
    private double personalAmount;

    public Expense() {
        totalAmount = 0;
        housingAmount = 0;
        utilitiesAmount = 0;
        foodAmount = 0;
        clothingAmount = 0;
        medicalAmount = 0;
        donationAmount = 0;
        savingInsurAmount = 0;
        entertainAmount = 0;
        transportationAmount = 0;
        personalAmount = 0;
    }

    public void addHousing(double amount) {
        totalAmount = totalAmount + amount;
        housingAmount = housingAmount + amount;
    }

    public void addUtility(double amount) {
        totalAmount = totalAmount + amount;
        utilitiesAmount = utilitiesAmount + amount;
    }

    public void addFood(double amount) {
        totalAmount = totalAmount + amount;
        foodAmount = foodAmount + amount;
    }

    public void addClothing(double amount) {
        totalAmount = totalAmount + amount;
        clothingAmount = clothingAmount + amount;
    }

    public void addMedical(double amount) {
        totalAmount = totalAmount + amount;
        medicalAmount = medicalAmount + amount;
    }

    public void addDonation(double amount) {
        totalAmount = totalAmount + amount;
        donationAmount = donationAmount + amount;
    }

    public void addSaving(double amount) {
        totalAmount = totalAmount + amount;
        savingInsurAmount = savingInsurAmount + amount;
    }

    public void addEntertain(double amount) {
        totalAmount = totalAmount + amount;
        entertainAmount = entertainAmount + amount;
    }

    public void addTransportation(double amount) {
        totalAmount = totalAmount + amount;
        transportationAmount = transportationAmount + amount;
    }

    public void addPersonal(double amount) {
        totalAmount = totalAmount + amount;
        personalAmount = personalAmount + amount;
    }


    public double getTotalAmount() {
        return totalAmount;
    }

    public double getHousingAmount() {
        return housingAmount;
    }

    public double getUtilitiesAmount() {
        return utilitiesAmount;
    }

    public double getFoodAmount() {
        return foodAmount;
    }

    public double getClothingAmount() {
        return clothingAmount;
    }

    public double getMedicalAmount() {
        return medicalAmount;
    }

    public double getDonationAmount() {
        return donationAmount;
    }

    public double getSavingInsurAmount() {
        return savingInsurAmount;
    }

    public double getEntertainAmount() {
        return entertainAmount;
    }

    public double getTransportationAmount() {
        return transportationAmount;
    }

    public double getPersonalAmount() {
        return personalAmount;
    }
}
