package com.via.letmein.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.R;
import com.via.letmein.persistence.room.entity.Member;
import com.via.letmein.persistence.room.entity.DayEntry;
import com.via.letmein.persistence.room.entity.Visit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private MutableLiveData<List<DayEntry>> data;

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        data = new MutableLiveData<>(mockupData());
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

    private List<DayEntry> mockupData() {
        List<DayEntry> dayEntries = new ArrayList<>();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        List<Visit> visits = new ArrayList<>();

        visits.add(new Visit(new Member("Tomas Koristka", "Postman", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person1", "Owner", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person3", "Member", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("Tomas Koristka", "Postman", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person1", "Owner", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person1", "Owner", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person2", "Member", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person2", "Member", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person2", "Member", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("Tomas Koristka", "Postman", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person3", "Member", R.mipmap.profile_icon_placeholder), timestamp));
        visits.add(new Visit(new Member("person3", "Member", R.mipmap.profile_icon_placeholder), timestamp));


        for (int i = 0; i < 5; ++i)
            dayEntries.add(new DayEntry(timestamp, visits));
        return dayEntries;
    }

}
