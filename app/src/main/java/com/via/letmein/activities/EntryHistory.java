package com.via.letmein.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.via.letmein.R;
import com.via.letmein.adapters.DayEntryAdapter;
import com.via.letmein.entities.DayEntry;
import com.via.letmein.entities.Member;
import com.via.letmein.entities.Visit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EntryHistory extends AppCompatActivity {

    RecyclerView daysList;
    RecyclerView.Adapter daysAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_history);

        daysList = findViewById(R.id.visits_recycler_view);
        daysList.hasFixedSize();
        daysList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        daysAdapter = new DayEntryAdapter(this, mockupData());
        daysList.setAdapter(daysAdapter);
    }

    private List<DayEntry> mockupData() {
        List<DayEntry> dayEntries = new ArrayList<>();

        Member member = new Member("Tomas Koristka", "Owner", R.mipmap.profile_icon_placeholder);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<Visit> visits = new ArrayList<>();
        for (int i = 0; i < 20; ++i)
            visits.add(new Visit(member, timestamp));

        for (int i = 0; i < 5; ++i)
            dayEntries.add(new DayEntry(timestamp, visits));
        return dayEntries;
    }

}
