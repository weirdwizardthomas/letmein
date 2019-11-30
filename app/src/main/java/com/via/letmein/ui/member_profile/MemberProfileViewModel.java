package com.via.letmein.ui.member_profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.response.ApiResponse;
import com.via.letmein.persistence.repository.HouseholdMemberImageRepository;

public class MemberProfileViewModel extends AndroidViewModel {
    private HouseholdMemberImageRepository repository;

    public MemberProfileViewModel(@NonNull Application application) {
        super(application);
        repository = HouseholdMemberImageRepository.getInstance();
    }

    public LiveData<ApiResponse> getImagePaths(String username, String sessionId) {
        return repository.getImagePaths(username, sessionId);
    }
}
