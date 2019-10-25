package com.via_android.letmein;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.via_android.letmein.Adapters.MemberAdapter;
import com.via_android.letmein.Adapters.VisitAdapter;
import com.via_android.letmein.Entities.Member;
import com.via_android.letmein.Entities.Visit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView visitList;
    RecyclerView.Adapter visitsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visitList = findViewById(R.id.visits_recycler_view);
        visitList.hasFixedSize();
        visitList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Visit> visits = new ArrayList<>();


        Member member = new Member("Tomas Koristka", "Owner", R.mipmap.profile_icon_placeholder);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < 20; ++i)
            visits.add(new Visit(member, timestamp));

        visitsAdapter = new VisitAdapter(visits);
        visitList.setAdapter(visitsAdapter);
    }
}
