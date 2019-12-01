package com.via.letmein.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.via.letmein.R;
import com.via.letmein.persistence.room.entity.DayEntry;
import com.via.letmein.ui.history.day_entry.DayEntryAdapter;

import java.util.List;

public class HistoryFragment extends Fragment {

    public static final int WEEK_IN_MILISECONDS = 604800000;

    private RecyclerView dayEntryRecyclerView;
    private RecyclerView.Adapter dayEntryAdapter;
    private HistoryViewModel historyViewModel;
    private Button openCalendarButton;
    private MaterialDatePicker<Pair<Long, Long>> picker;
    private Pair<Long, Long> selectionDates;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        initialiseAdapter();
        initialiseDaysRecyclerView(root);
        setDefaultSelectionDates();
        initialiseDatePicker();
        initialiseDatePickerButton(root);

        picker.show(getChildFragmentManager(), picker.toString());

        return root;
    }

    private void setDefaultSelectionDates() {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        long weekAgo = today - WEEK_IN_MILISECONDS;
        selectionDates = new Pair<>(weekAgo, today);
    }

    private void initialiseDatePickerButton(View root) {
        openCalendarButton = root.findViewById(R.id.openCalendarButton);
        openCalendarButton.setText("Select range to show:");
        openCalendarButton.setOnClickListener(v -> picker.show(getChildFragmentManager(), picker.toString()));
    }

    private void initialiseDatePicker() {
        picker = setupDateSelectorBuilder().build();
        picker.addOnPositiveButtonClickListener(selection -> {
            selectionDates = selection;
            openCalendarButton.setText("Select range to show:" + picker.getHeaderText());
        });
    }

    private MaterialDatePicker.Builder<Pair<Long, Long>> setupDateSelectorBuilder() {
        final MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder
                .dateRangePicker()
                .setSelection(selectionDates)
                .setTheme(resolveOrThrow(getContext(), R.attr.materialCalendarTheme));
        return builder;
    }

    private static int resolveOrThrow(Context context, @AttrRes int attributeResId) throws IllegalArgumentException {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attributeResId, typedValue, true))
            return typedValue.data;

        throw new IllegalArgumentException(context.getResources().getResourceName(attributeResId));
    }

    private void initialiseAdapter() {
        List<DayEntry> dummy = historyViewModel.getData().getValue();
        dayEntryAdapter = new DayEntryAdapter(getContext(), dummy);
    }

    private void initialiseDaysRecyclerView(View root) {
        dayEntryRecyclerView = root.findViewById(R.id.visitsRecyclerView);
        dayEntryRecyclerView.hasFixedSize();
        dayEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dayEntryRecyclerView.setAdapter(dayEntryAdapter);
    }

}
