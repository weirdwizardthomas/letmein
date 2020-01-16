package com.via.letmein.ui.add_member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.HouseholdMemberRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ViewModel for {@see AddMemberFragment}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class AddMemberViewModel extends AndroidViewModel {

    private final HouseholdMemberRepository repository;
    private final MutableLiveData<List<String>> roles;

    public AddMemberViewModel(@NonNull Application application) {
        super(application);
        repository = HouseholdMemberRepository.getInstance(Session.getInstance(application));

        roles = new MutableLiveData<>();
        roles.setValue(Arrays.asList(
                "Member",
                "Owner",
                "Postman",
                "Cleaner",
                "Baby sitter",
                "Family"));
    }


    public LiveData<List<String>> getRoles() {
        return roles;
    }

    public List<String> getRolesAsList() {
        return new ArrayList<>(Arrays.asList(
                "Member",
                "Owner",
                "Postman",
                "Cleaner",
                "Baby sitter",
                "Family"));
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

    public LiveData<ApiResponse> addBiometricData(int userId, String sessionId) {
        //todo fix addBiometric data by requesting an id in registration and using int instead of string everywhere
        return repository.addBiometricData(userId, sessionId);
    }
}
