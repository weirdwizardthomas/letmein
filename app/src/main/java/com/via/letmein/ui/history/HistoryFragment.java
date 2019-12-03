package com.via.letmein.ui.history;

import android.annotation.SuppressLint;
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
import java.util.Objects;

public class HistoryFragment extends Fragment {

    public static final int WEEK_IN_MILISECONDS = 604800000;

    private HistoryViewModel historyViewModel;

    private RecyclerView dayEntryRecyclerView;
    private RecyclerView.Adapter dayEntryAdapter;
    private Button openCalendarButton;
    private MaterialDatePicker<Pair<Long, Long>> picker;
    private Pair<Long, Long> selectionDates;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        initialiseLayout(root);

        //Open the calendar to pick a custom range
        picker.show(getChildFragmentManager(), picker.toString());

        return root;
    }

    private void initialiseLayout(View root) {
        initialiseAdapter();
        initialiseDaysRecyclerView(root);
        setDefaultSelectionDates();
        initialiseDatePicker();
        initialiseDatePickerButton(root);
    }

    private void setDefaultSelectionDates() {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        long weekAgo = today - WEEK_IN_MILISECONDS;
        selectionDates = new Pair<>(weekAgo, today);
    }

    @SuppressLint("SetTextI18n")
    private void initialiseDatePickerButton(View root) {
        openCalendarButton = root.findViewById(R.id.openCalendarButton);
        openCalendarButton.setText(getString(R.string.selectRangeToShow));
        openCalendarButton.setOnClickListener(v -> picker.show(getChildFragmentManager(), picker.toString()));
    }

    private void initialiseDatePicker() {
        picker = setupDateSelectorBuilder().build();
        picker.addOnPositiveButtonClickListener(selection -> {
            selectionDates = selection;
            openCalendarButton.setText(getString(R.string.selectRangeToShow) + picker.getHeaderText());
            //TODO fetch data
        });
    }

    private MaterialDatePicker.Builder<Pair<Long, Long>> setupDateSelectorBuilder() {
        return MaterialDatePicker.Builder
                .dateRangePicker()
                .setSelection(selectionDates)
                .setTheme(resolveOrThrow(Objects.requireNonNull(getContext()), R.attr.materialCalendarTheme));
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
        dayEntryRecyclerView.setHasFixedSize(true);
        dayEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dayEntryRecyclerView.setAdapter(dayEntryAdapter);
    }

}
