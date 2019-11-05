package com.via.letmein.ui.administration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.via.letmein.R;
import com.via.letmein.persistence.entity.Member;

import java.util.List;

import static com.via.letmein.ui.view_member.ViewMember.BUNDLE_ID_KEY;
import static com.via.letmein.ui.view_member.ViewMember.BUNDLE_IMAGEID_KEY;
import static com.via.letmein.ui.view_member.ViewMember.BUNDLE_NAME_KEY;
import static com.via.letmein.ui.view_member.ViewMember.BUNDLE_ROLE_KEY;

public class AdministrationFragment extends Fragment implements MemberAdapter.OnItemClickListener {

    private RecyclerView membersRecyclerView;
    private MemberAdapter membersAdapter;

    private AdministrationViewModel administrationViewModel;
    private FloatingActionButton addMemberButton;

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

    private void initialiseViewModel() {
        administrationViewModel = ViewModelProviders.of(this).get(AdministrationViewModel.class);
        administrationViewModel.getAllMembers().observe(this, new Observer<List<Member>>() {
            @Override
            public void onChanged(List<Member> members) {
                membersAdapter.setData(administrationViewModel.getAllMembers().getValue());
            }
        });
    }

    private void initialiseAdapter() {
        membersAdapter = new MemberAdapter(this);
    }

    private void initialiseAddMemberButton(View root) {
        addMemberButton = root.findViewById(R.id.membersAdministration_addMember);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_add_member);
            }
        };

        addMemberButton.setOnClickListener(onClickListener);
    }

    private void initialiseMemberRecyclerView(final View root) {
        membersRecyclerView = root.findViewById(R.id.membersAdministration_membersList);
        membersRecyclerView.hasFixedSize();
        membersRecyclerView.setAdapter(membersAdapter);
        membersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                administrationViewModel.delete(membersAdapter.getMemberAt(viewHolder.getAdapterPosition()));
                Toast.makeText(root.getContext(), "", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(membersRecyclerView);

    }

    @Override
    public void onItemClick(Member item) {

        Bundle extras = new Bundle();

        //TODO consider making Member serialisable and send the Member instance instead?
        extras.putString(BUNDLE_NAME_KEY, item.getName());
        extras.putString(BUNDLE_ROLE_KEY, item.getRole());
        extras.putInt(BUNDLE_ID_KEY, item.getId());
        extras.putInt(BUNDLE_IMAGEID_KEY, item.getImageID());

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_view_member, extras);
    }
}
