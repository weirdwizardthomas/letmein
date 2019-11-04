package com.via.letmein.ui.administration;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.entity.Member;
import com.via.letmein.persistence.repository.MemberRepository;

import java.util.List;

public class AdministrationViewModel extends AndroidViewModel {

    private MemberRepository repository;

    public AdministrationViewModel(@NonNull Application application) {
        super(application);
        repository = MemberRepository.getInstance(application);
    }

    public void insert(Member member) {
        repository.insert(member);
    }

    public void update(Member member) {
        repository.update(member);
    }

    public void delete(Member member) {
        repository.delete(member);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Member>> getAllMembers() {
        return repository.getAllMembers();
    }

}
