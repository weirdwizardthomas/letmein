package com.via.letmein.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.R;
import com.via.letmein.ui.administration.Member;
import com.via.letmein.ui.history.day_entry.DayEntry;
import com.via.letmein.ui.history.visit.Visit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private MutableLiveData<List<DayEntry>> data;

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        List<DayEntry> dayEntries = new ArrayList<>();

        Member member = new Member("Tomas Koristka", "Postman", R.mipmap.profile_icon_placeholder);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<Visit> visits = new ArrayList<>();
        for (int i = 0; i < 20; ++i)
            visits.add(new Visit(member, timestamp));

        for (int i = 0; i < 5; ++i)
            dayEntries.add(new DayEntry(timestamp, visits));

        data = new MutableLiveData<>(dayEntries);
    }

    public LiveData<List<DayEntry>> getData() {
        return data;
    }

    public void setData(MutableLiveData<List<DayEntry>> data) {
        this.data = data;
    }

    public void insert(DayEntry dayEntry) {
        List<DayEntry> dummy = data.getValue();
        dummy.add(dayEntry);
        data.setValue(dummy);
    }
}
