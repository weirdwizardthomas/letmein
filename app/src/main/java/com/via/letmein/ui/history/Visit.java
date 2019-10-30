package com.via.letmein.ui.history;

import com.via.letmein.ui.administration.Member;

import java.sql.Timestamp;

public class Visit {

    private Member member;
    private Timestamp timestamp;

    public Visit(Member member, Timestamp timestamp) {
        this.member = member;
        this.timestamp = timestamp;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
