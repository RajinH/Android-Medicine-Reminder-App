package com.example.medicalapp;

import java.time.LocalDate;
import java.util.ArrayList;

/*
    Event class
 */

public class Event {

    // Member variables
    public LocalDate date;
    public ArrayList<Prescription> datePrescriptions = new ArrayList<>();

    // Constructor
    public Event(LocalDate date, ArrayList<Prescription> datePrescriptions) {
        this.date = date;
        this.datePrescriptions = datePrescriptions;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ArrayList<Prescription> getDatePrescriptions() {
        return datePrescriptions;
    }

    public void setDatePrescriptions(ArrayList<Prescription> datePrescriptions) {
        this.datePrescriptions = datePrescriptions;
    }
}
