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

/**
 * Viewmodel for {@see AddMemberFragment}
 */
public class AddMemberViewModel extends AndroidViewModel {

    private final HouseholdMemberRepository repository;
    private final MutableLiveData<List<String>> roles;

    public AddMemberViewModel(@NonNull Application application) {
        super(application);
        repository = HouseholdMemberRepository.getInstance(Session.getInstance(application));

        List<String> dummy = Arrays.asList("Member", "Owner", "Postman", "Cleaning lady");
        roles = new MutableLiveData<>();
        roles.setValue(dummy);
    }


    public LiveData<List<String>> getRoles() {
        return roles;
    }

    /**
     * Creates a new household member and saves it to the server
     *
     * @param name      Name of the new member
     * @param role      Assigned role of the new member
     * @param sessionId Current session's id
     * @return Observable data for response checking and error handling
     */
    public LiveData<ApiResponse> createMember(String name, String role, String sessionId) {
        return repository.createMember(name, role, sessionId);
    }
}
