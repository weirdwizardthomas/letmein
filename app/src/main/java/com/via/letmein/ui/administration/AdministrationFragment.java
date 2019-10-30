package com.via.letmein.ui.administration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.via.letmein.R;

import java.util.ArrayList;
import java.util.List;

public class AdministrationFragment extends Fragment {

    private RecyclerView membersList;
    private RecyclerView.Adapter membersAdapter;
    private FloatingActionButton addMemberFloatingActionButton;

    public AdministrationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_administration, container, false);
        membersAdapter = new MemberAdapter(mockupData());

        initialiseMemberRecyclerView(root);
        initialiseAddMemberButton(root);

        return root;
    }

    private void initialiseAddMemberButton(View root) {
        addMemberFloatingActionButton = root.findViewById(R.id.membersAdministration_addMember);
        addMemberFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_add_member);
            }
        });
    }

    private void initialiseMemberRecyclerView(View root) {
        membersList = root.findViewById(R.id.membersAdministration_membersList);
        membersList.hasFixedSize();
        membersList.setAdapter(membersAdapter);
        membersList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }


    private List<Member> mockupData() {
        List<Member> data = new ArrayList<>();

        Member member = new Member("Tomas", "Owner", R.mipmap.profile_icon_placeholder);
        for (int i = 0; i < 30; ++i)
            data.add(member);

        return data;

    }

}
