package com.via.letmein.ui.administration;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.R;

import java.util.ArrayList;
import java.util.List;

public class AdministrationViewModel extends AndroidViewModel {
    private MutableLiveData<List<Member>> data;

    public AdministrationViewModel(@NonNull Application application) {
        super(application);

        //TODO REMOVE MOCKUP
        List<Member> dummy = new ArrayList<>();
        Member member = new Member("Tomas Koristka", "Owner", R.mipmap.profile_icon_placeholder);
        for (int i = 0; i < 30; ++i)
            dummy.add(member);

        data = new MutableLiveData<>(dummy);

    }

    public LiveData<List<Member>> getData() {
        return data;
    }

    public void setData(MutableLiveData<List<Member>> data) {
        this.data = data;
    }

    public void insert(Member member) {
        List<Member> dummy = data.getValue();
        dummy.add(member);
        data.setValue(dummy);
    }
}
