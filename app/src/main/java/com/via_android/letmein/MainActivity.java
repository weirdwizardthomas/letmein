package com.via_android.letmein;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.via_android.letmein.Adapters.DayEntryAdapter;
import com.via_android.letmein.Adapters.VisitAdapter;
import com.via_android.letmein.Entities.DayEntry;
import com.via_android.letmein.Entities.Member;
import com.via_android.letmein.Entities.Visit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView daysList;
    RecyclerView.Adapter daysAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daysList = findViewById(R.id.visits_recycler_view);
        daysList.hasFixedSize();
        daysList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<DayEntry> dayEntries = new ArrayList<>();


        Member member = new Member("Tomas Koristka", "Owner", R.mipmap.profile_icon_placeholder);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<Visit> visits = new ArrayList<>();
        for (int i = 0; i < 20; ++i)
            visits.add(new Visit(member, timestamp));

        for (int i = 0; i < 5; ++i)
            dayEntries.add(new DayEntry(timestamp, visits));

        daysAdapter = new DayEntryAdapter(this, dayEntries);
        daysList.setAdapter(daysAdapter);
    }
}
