package com.via.letmein.ui.member_profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.HouseholdMember;
import com.via.letmein.persistence.repository.HouseholdMemberImageRepository;

/**
 * ViewModel for {@See MemberProfileFragment}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class MemberProfileViewModel extends AndroidViewModel {
    private final HouseholdMemberImageRepository repository;
    private HouseholdMember householdMember;

    public MemberProfileViewModel(@NonNull Application application) {
        super(application);
        repository = HouseholdMemberImageRepository.getInstance(Session.getInstance(application));
        householdMember = new HouseholdMember();
    }

    public LiveData<ApiResponse> getImagePaths(String sessionId) {
        return repository.getImagePaths(householdMember.getName(), sessionId);
    }

    public HouseholdMember getHouseholdMember() {
        return householdMember;
    }

    public void setHouseholdMember(HouseholdMember householdMember) {
        this.householdMember = householdMember;
    }
}
