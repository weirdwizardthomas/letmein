package com.via.letmein.ui.member_profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.HouseholdMember;
import com.via.letmein.persistence.repository.HouseholdMemberImageRepository;
import com.via.letmein.persistence.repository.PromotionRepository;

/**
 * ViewModel for {@see MemberProfileFragment}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class MemberProfileViewModel extends AndroidViewModel {
    private final HouseholdMemberImageRepository imageRepository;
    private final PromotionRepository promotionRepository;
    private HouseholdMember householdMember;

    public MemberProfileViewModel(@NonNull Application application) {
        super(application);
        imageRepository = HouseholdMemberImageRepository.getInstance(Session.getInstance(application));
        promotionRepository = PromotionRepository.getInstance(Session.getInstance(application));
        householdMember = new HouseholdMember();
    }

    public LiveData<ApiResponse> getImagePaths(String sessionId) {
        return imageRepository.getImagePaths(householdMember.getName(), sessionId);
    }

    public HouseholdMember getHouseholdMember() {
        return householdMember;
    }

    public void setHouseholdMember(HouseholdMember householdMember) {
        this.householdMember = householdMember;
    }

    public LiveData<ApiResponse> promoteAdmin(String sessionId) {
        return promotionRepository.getPromoteAdmin(sessionId,householdMember.getId());
    }
}
