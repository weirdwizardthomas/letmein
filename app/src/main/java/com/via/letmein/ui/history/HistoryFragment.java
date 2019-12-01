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

import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView dayEntryRecyclerView;
    private RecyclerView.Adapter dayEntryAdapter;
    private HistoryViewModel historyViewModel;
    private Button openCalendarButton;
    private MaterialDatePicker<Pair<Long, Long>> picker;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        initialiseAdapter();
        initialiseDaysRecyclerView(root);
        initialiseDatePicker();
        initialiseDatePickerButton(root);

        return root;
    }

    private void initialiseDatePickerButton(View root) {
        openCalendarButton = root.findViewById(R.id.openCalendarButton);
        openCalendarButton.setOnClickListener(v -> picker.show(getChildFragmentManager(), picker.toString()));
    }

    private void initialiseDatePicker() {
        picker = setupDateSelectorBuilder().build();
        picker.addOnPositiveButtonClickListener(selection -> {
            Pair<Date, Date> dateRange = new Pair<>(new Date(selection.first), new Date(selection.second));
            openCalendarButton.setText(picker.getHeaderText());
        });
    }

    private MaterialDatePicker.Builder<Pair<Long, Long>> setupDateSelectorBuilder() {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Pair<Long, Long> todayPair = new Pair<>(today, today);

        final MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker
                .Builder
                .dateRangePicker();
        builder.setSelection(todayPair);
        builder.setTheme(resolveOrThrow(getContext(), R.attr.materialCalendarTheme));
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
