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
import com.via.letmein.ui.history.day_entry.DayEntryAdapter;

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

    /**
     * Initialises fragment's layout
     *
     * @param root Parent element of the layout
     */
    private void initialiseLayout(View root) {
        dayEntryAdapter = new DayEntryAdapter(getContext(), historyViewModel.getData().getValue());
        dayEntryRecyclerView = root.findViewById(R.id.visitsRecyclerView);
        dayEntryRecyclerView.setHasFixedSize(true);
        dayEntryRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        dayEntryRecyclerView.setAdapter(dayEntryAdapter);
        setDefaultSelectionDates();
        picker = setupDateSelectorBuilder().build();
        picker.addOnPositiveButtonClickListener(selection -> {
            selectionDates = selection;
            openCalendarButton.setText(getString(R.string.selectRangeToShow) + picker.getHeaderText());
            //TODO fetch data
        });
        openCalendarButton = root.findViewById(R.id.openCalendarButton);
        openCalendarButton.setText(getString(R.string.selectRangeToShow));
        openCalendarButton.setOnClickListener(v -> picker.show(getChildFragmentManager(), picker.toString()));
    }

    /**
     * Sets the default(from today to 7 days before) date range for the {@see HistoryFragment#selectionDates}
     */
    private void setDefaultSelectionDates() {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        long weekAgo = today - WEEK_IN_MILISECONDS;
        selectionDates = new Pair<>(weekAgo, today);
    }

    /**
     * Builds the MaterialDatePicker dialog
     *
     * @return MaterialDatePicker with {@see HistoryFragment#selecitonDates}
     */
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

}
