package com.via.letmein.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.LoggedAction;
import com.via.letmein.ui.main_activity.MainActivity;

import java.util.List;
import java.util.Objects;

import static com.via.letmein.persistence.api.Error.ERROR_DATABASE_ERROR;
import static com.via.letmein.persistence.api.Error.ERROR_EXPIRED_SESSION_ID;
import static com.via.letmein.persistence.api.Error.ERROR_MISSING_REQUIRED_PARAMETERS;

/**
 * Fragment to show {@see DailyLog} to the user.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class HistoryFragment extends Fragment {

    private static final int WEEK_IN_MILISECONDS = 604800000;
    private static final String TAG = "History";

    private HistoryViewModel historyViewModel;

    private RecyclerView dayEntryRecyclerView;
    private DailyLogAdapter dailyLogAdapter;
    private ExtendedFloatingActionButton openCalendarButton;
    private MaterialDatePicker<Pair<Long, Long>> picker;
    private Pair<Long, Long> selectionDates;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        initialiseLayout(root);
        getLogs();

        return root;
    }

    /**
     * Initialises fragment's layout
     *
     * @param root Parent element of the layout
     */
    private void initialiseLayout(View root) {
        dailyLogAdapter = new DailyLogAdapter(getContext());
        dayEntryRecyclerView = root.findViewById(R.id.visitsRecyclerView);
        dayEntryRecyclerView.setHasFixedSize(true);
        dayEntryRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        dayEntryRecyclerView.setAdapter(dailyLogAdapter);
        setDefaultSelectionDates();

        picker = setupDateSelectorBuilder().build();
        picker.addOnPositiveButtonClickListener(selection -> {
            selectionDates = selection;
            String calendarButtonText = getString(R.string.labelSelectRangeToShow) +
                    picker.getHeaderText();
            openCalendarButton.setText(calendarButtonText);
            getLogs();
        });
        openCalendarButton = root.findViewById(R.id.openCalendarButton);
        openCalendarButton.setText(getString(R.string.labelSelectRangeToShow));
        openCalendarButton.setOnClickListener(v -> picker.show(getChildFragmentManager(), picker.toString()));


    }

    private void getLogs() {
        historyViewModel.getVisits(Session.getInstance(getContext()).getSessionId(), selectionDates).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null)
                    dailyLogAdapter.setData((List<LoggedAction>) apiResponse.getContent());
                if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                    handleErrors(apiResponse.getErrorMessage());
            }
        });
    }

    private void handleErrors(String errorMessage) {
        switch (errorMessage) {
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                Log.d(TAG, ERROR_MISSING_REQUIRED_PARAMETERS);
                break;
            }
            case ERROR_EXPIRED_SESSION_ID: {
                ((MainActivity) Objects.requireNonNull(getActivity())).login();
                break;
            }
            case ERROR_DATABASE_ERROR: {
                Log.d(TAG, ERROR_DATABASE_ERROR);
                break;
            }
        }
    }

    /**
     * Sets the default(from today to 7 days before) date range for the {@see HistoryFragment#selectionDates}
     */
    private void setDefaultSelectionDates() {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        selectionDates = new Pair<>(today - WEEK_IN_MILISECONDS, today);
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
