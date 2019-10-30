package com.via.letmein.ui.administration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.via.letmein.R;
import com.via.letmein.ui.add_member.AddMemberViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdministrationFragment extends Fragment {

    private RecyclerView membersList;
    private RecyclerView.Adapter membersAdapter;
    private FloatingActionButton addMemberFloatingActionButton;
    private AdministrationViewModel viewModel;


    public AdministrationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_administration, container, false);

        initialiseViewModel();
        initialiseAdapter();
        initialiseMemberRecyclerView(root);
        initialiseAddMemberButton(root);

        return root;
    }

    public void initialiseViewModel() {
        viewModel = ViewModelProviders.of(this).get(AdministrationViewModel.class);
    }

    public void initialiseAdapter() {
        List<Member> members = viewModel.getData().getValue();
        membersAdapter = new MemberAdapter(members);
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


}
