package com.via.letmein.ui.add_member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.HouseholdMemberRepository;

import java.util.Arrays;
import java.util.List;

public class AddMemberViewModel extends AndroidViewModel {

    private final HouseholdMemberRepository repository;
    private final MutableLiveData<List<String>> roles;
    private Session session;

    public AddMemberViewModel(@NonNull Application application) {
        super(application);
        session = Session.getInstance(application);
        repository = HouseholdMemberRepository.getInstance(session);

        List<String> dummy = Arrays.asList("Member", "Owner", "Postman", "Cleaning lady");
        roles = new MutableLiveData<>();
        roles.setValue(dummy);
    }


    public LiveData<List<String>> getRoles() {
        return roles;
    }

    //todo get sessionID from the activity instead?
    public LiveData<ApiResponse> createUser(String name, String role) {
        return repository.createMember(name, role, session.getSessionId());
    }
}
