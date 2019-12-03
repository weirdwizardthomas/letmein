package com.via.letmein.persistence.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.persistence.room.entity.Visit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VisitRepository {

    private static VisitRepository instance;
    private final MutableLiveData<List<Visit>> allVisits;

    private VisitRepository(Application application) {

        int dayInMiliseconds = 86400000;
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis() - dayInMiliseconds);
        Timestamp timestamp3 = new Timestamp(System.currentTimeMillis() - dayInMiliseconds * 2);
        Timestamp timestamp4 = new Timestamp(System.currentTimeMillis() + dayInMiliseconds);

        List<Visit> dummy = new ArrayList<>();
      /*  dummy.add(new Visit(new Member("Tomas Koristka", "Postman", R.mipmap.profile_icon_placeholder), timestamp1));
        dummy.add(new Visit(new Member("person1", "Owner", R.mipmap.profile_icon_placeholder), timestamp1));
        dummy.add(new Visit(new Member("person3", "Member", R.mipmap.profile_icon_placeholder), timestamp2));
        dummy.add(new Visit(new Member("Tomas Koristka", "Postman", R.mipmap.profile_icon_placeholder), timestamp2));
        dummy.add(new Visit(new Member("person1", "Owner", R.mipmap.profile_icon_placeholder), timestamp3));
        dummy.add(new Visit(new Member("person1", "Owner", R.mipmap.profile_icon_placeholder), timestamp3));
        dummy.add(new Visit(new Member("person2", "Member", R.mipmap.profile_icon_placeholder), timestamp4));
        dummy.add(new Visit(new Member("person2", "Member", R.mipmap.profile_icon_placeholder), timestamp4));
        dummy.add(new Visit(new Member("person2", "Member", R.mipmap.profile_icon_placeholder), timestamp1));
        dummy.add(new Visit(new Member("Tomas Koristka", "Postman", R.mipmap.profile_icon_placeholder), timestamp2));
        dummy.add(new Visit(new Member("person3", "Member", R.mipmap.profile_icon_placeholder), timestamp3));
        dummy.add(new Visit(new Member("person3", "Member", R.mipmap.profile_icon_placeholder), timestamp4));
*/
        allVisits = new MutableLiveData<>(dummy);
    }

    public static VisitRepository getInstance(Application application) {
        if (instance == null)
            instance = new VisitRepository(application);
        return instance;
    }

    public void insert(Visit visit) {
        List<Visit> dummy = allVisits.getValue();
        Objects.requireNonNull(dummy).add(visit);
        allVisits.setValue(dummy);
    }

    public void update(Visit visit) {
        //todo
    }

    public void delete(Visit visit) {
        List<Visit> dummy = allVisits.getValue();
        Objects.requireNonNull(dummy).remove(visit);
        allVisits.setValue(dummy);
    }

    public void deleteAll() {
        List<Visit> dummy = allVisits.getValue();
        Objects.requireNonNull(dummy).clear();
        allVisits.setValue(dummy);
    }

    public LiveData<List<Visit>> getAllVisits() {
        return allVisits;
    }


}
