package com.via_android.letmein.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.via_android.letmein.R;
import com.via_android.letmein.adapters.MemberAdapter;
import com.via_android.letmein.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class MembersAdministration extends AppCompatActivity {
    private RecyclerView membersList;
    private RecyclerView.Adapter membersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_administration);

        membersAdapter = new MemberAdapter(mockupData());

        membersList = findViewById(R.id.membersAdministration_membersList);
        membersList.hasFixedSize();
        membersList.setAdapter(membersAdapter);
        membersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private List<Member> mockupData() {
        List<Member> data = new ArrayList<>();

        Member member = new Member("Tomas", "Owner", R.mipmap.profile_icon_placeholder);
        for (int i = 0; i < 30; ++i)
            data.add(member);

        return data;

    }
}
