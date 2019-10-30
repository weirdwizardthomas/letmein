package com.via.letmein.ui.history;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.via.letmein.R;
import com.via.letmein.ui.administration.Member;
import com.via.letmein.ui.history.day_entry.DayEntry;
import com.via.letmein.ui.history.day_entry.DayEntryAdapter;
import com.via.letmein.ui.history.visit.Visit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private HistoryViewModel viewModel;

    public HistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        initialiseViewModel();
        initialiseAdapter();
        initialiseDaysRecyclerView(root);

        return root;
    }

    public void initialiseAdapter() {
        List<DayEntry> dummy = viewModel.getData().getValue();
        adapter = new DayEntryAdapter(getContext(), dummy);
    }

    public void initialiseViewModel() {
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
    }

    private void initialiseDaysRecyclerView(View root) {
        recycler = root.findViewById(R.id.visits_recycler_view);
        recycler.hasFixedSize();
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
    }

}
