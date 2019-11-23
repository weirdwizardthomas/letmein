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
import com.via.letmein.persistence.entity.DayEntry;
import com.via.letmein.ui.history.day_entry.DayEntryAdapter;

import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView dayEntryRecyclerView;
    private RecyclerView.Adapter dayEntryAdapter;
    private HistoryViewModel historyViewModel;

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
        List<DayEntry> dummy = historyViewModel.getData().getValue();
        dayEntryAdapter = new DayEntryAdapter(getContext(), dummy);
    }

    public void initialiseViewModel() {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
    }

    private void initialiseDaysRecyclerView(View root) {
        dayEntryRecyclerView = root.findViewById(R.id.visitsRecyclerView);
        dayEntryRecyclerView.hasFixedSize();
        dayEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dayEntryRecyclerView.setAdapter(dayEntryAdapter);
    }

}
