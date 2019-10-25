package com.via_android.letmein.Entities;

import java.sql.Timestamp;
import java.util.List;

public class DayEntry {
    private List<Member> members;
    private Timestamp timestamp;

    public DayEntry(List<Member> members, Timestamp timestamp) {
        this.members = members;
        this.timestamp = timestamp;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
