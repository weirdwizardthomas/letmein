package com.via.letmein.persistence.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.List;


public class DayEntry {


    private Timestamp date;

    private List<Visit> visits;

    public DayEntry(Timestamp date, List<Visit> visits) {
        this.date = date;
        this.visits = visits;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public void addVisit(Visit visit) {
        visits.add(visit);
    }
}
