package com.via.letmein.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.room.entity.Member;
import com.via.letmein.persistence.room.entity.Visit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Visit>> data;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        data = new MutableLiveData<>(new ArrayList<>());
        data.setValue(mockupData());
    }

    LiveData<List<Visit>> getData() {
        return data;
    }

    public void setData(List<Visit> data) {
        this.data.setValue(data);
    }

    private List<Visit> mockupData() {

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

        return visits;

    }
}