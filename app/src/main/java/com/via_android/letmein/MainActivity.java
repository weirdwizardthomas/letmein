package com.via_android.letmein;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.via_android.letmein.Adapters.MemberAdapter;
import com.via_android.letmein.Entities.Member;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView membersList;
    RecyclerView.Adapter membersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        membersList = findViewById(R.id.members_list);
        membersList.hasFixedSize();
        membersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<Member> members = new ArrayList<>();

        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));
        members.add(new Member("a", "b", R.mipmap.profile_icon_placeholder));

        membersAdapter = new MemberAdapter(members);
        membersList.setAdapter(membersAdapter);
    }
}
