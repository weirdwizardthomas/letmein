package com.via.letmein.ui.add_member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddMemberViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> roles;


    public AddMemberViewModel(@NonNull Application application) {
        super(application);
        List<String> dummy = Arrays.asList("Member", "Owner", "Postman", "Cleaning lady");
        roles = new MutableLiveData<>();
        roles.setValue(dummy);
    }

    public void insert(String role) {
        List<String> dummy = roles.getValue();
        dummy.add(role);
        roles.setValue(dummy);
    }

    public LiveData<List<String>> getRoles() {
        return roles;
    }

    public void setRoles(MutableLiveData<List<String>> roles) {
        this.roles = roles;
    }
}
