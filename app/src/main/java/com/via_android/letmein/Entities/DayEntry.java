package com.via_android.letmein.Entities;

import java.sql.Timestamp;
import java.util.List;

public class DayEntry {
    private List<Visit> visits;

    public DayEntry(List<Visit> visits) {
        this.visits = visits;
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
