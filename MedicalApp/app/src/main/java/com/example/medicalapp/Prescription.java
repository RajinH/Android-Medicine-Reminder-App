package com.example.medicalapp;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

/*
    Prescription class
 */

public class Prescription {

    // Member variables
    private LocalDate startDate;
    private LocalDate endDate;
    private String medicineName;
    private Double prescribedDosage;
    private int totalNumIntakes;
    private int currentAmount;
    private int confirmedIntakes;
    private ArrayList<DayOfWeek> repeatingDaysOfWeek;
    private boolean hasExpired;
    private Time setTime;

    // Constructor
    public Prescription(LocalDate startDate, LocalDate endDate, String medicineName, Double prescribedDosage, ArrayList<DayOfWeek> repeatingDaysOfWeek, Time setTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.medicineName = medicineName;
        this.prescribedDosage = prescribedDosage;
        this.confirmedIntakes = 0;
        this.repeatingDaysOfWeek = repeatingDaysOfWeek;
        this.hasExpired = false;
        this.setTime = setTime;
    }

    // Getters and Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Double getPrescribedDosage() {
        return prescribedDosage;
    }

    public void setPrescribedDosage(Double prescribedDosage) {
        this.prescribedDosage = prescribedDosage;
    }

    public int getTotalNumIntakes() {
        return totalNumIntakes;
    }

    public void setTotalNumIntakes(int totalNumIntakes) {
        this.totalNumIntakes = totalNumIntakes;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getConfirmedIntakes() {
        return confirmedIntakes;
    }

    public void setConfirmedIntakes(int confirmedIntakes) {
        this.confirmedIntakes = confirmedIntakes;
    }

    public ArrayList<DayOfWeek> getRepeatingDaysOfWeek() {
        return repeatingDaysOfWeek;
    }

    public void setRepeatingDaysOfWeek(ArrayList<DayOfWeek> repeatingDaysOfWeek) {
        this.repeatingDaysOfWeek = repeatingDaysOfWeek;
    }

    public boolean isHasExpired() {
        return hasExpired;
    }

    public void setHasExpired(boolean hasExpired) {
        this.hasExpired = hasExpired;
    }

    public Time getSetTime() {
        return setTime;
    }

    public void setSetTime(Time setTime) {
        this.setTime = setTime;
    }
}
