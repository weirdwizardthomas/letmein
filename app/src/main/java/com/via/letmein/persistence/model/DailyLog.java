package com.via.letmein.persistence.model;

import java.sql.Timestamp;
import java.util.List;


public class DailyLog {

    private Timestamp timestamp;

    private List<Log> logs;

    public DailyLog(Timestamp timestamp, List<Log> logs) {
        this.timestamp = timestamp;
        this.logs = logs;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public List<Log> getLogs() {
        return logs;
    }
}
