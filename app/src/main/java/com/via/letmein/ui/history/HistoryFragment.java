package com.via.letmein.ui.history;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.via.letmein.R;
import com.via.letmein.ui.administration.Member;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView daysList;
    private RecyclerView.Adapter daysAdapter;

    public HistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        daysAdapter = new DayEntryAdapter(getContext(), mockupData());

        initialiseDaysRecyclerView(root);

        return root;
    }

    private void initialiseDaysRecyclerView(View root) {
        daysList = root.findViewById(R.id.visits_recycler_view);
        daysList.hasFixedSize();
        daysList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
