package com.via.letmein.ui.add_member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.persistence.entity.Member;
import com.via.letmein.persistence.repository.MemberRepository;

import java.util.Arrays;
import java.util.List;

public class AddMemberViewModel extends AndroidViewModel {

    private MemberRepository repository;
    private MutableLiveData<List<String>> roles;

    public AddMemberViewModel(@NonNull Application application) {
        super(application);
        repository = MemberRepository.getInstance(application);

        List<String> dummy = Arrays.asList("Member", "Owner", "Postman", "Cleaning lady");
        roles = new MutableLiveData<>();
        roles.setValue(dummy);
    }

    public void insert(Member member) {
        repository.insert(member);
    }

    public LiveData<List<String>> getRoles() {
        return roles;
    }

}
