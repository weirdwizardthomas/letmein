package com.via.letmein.persistence.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Collection of logs with the same date received from the server.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class DailyLog {

    private final Timestamp timestamp;
    private final List<LoggedAction> loggedActions;

    public DailyLog(Timestamp timestamp, List<LoggedAction> loggedActions) {
        this.timestamp = timestamp;
        this.loggedActions = loggedActions;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public List<LoggedAction> getLoggedActions() {
        return loggedActions;
    }
}
