package com.via.letmein.ui.add_member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.via.letmein.R;

import java.util.ArrayList;
import java.util.List;

public class AddMember extends Fragment {

    private ArrayAdapter<String> roleAdapter;
    private AddMemberViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_member, container, false);
        viewModel = ViewModelProviders.of(this).get(AddMemberViewModel.class);

        initaliseRoleAdapter();
        initialiseRoleSpinner(root);

        return root;
    }

    private void initaliseRoleAdapter() {
        List<String> roles = viewModel.getRoles().getValue();

        roleAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, roles);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initialiseRoleSpinner(View view) {
        Spinner roleSpinner = view.findViewById(R.id.addMember_selectRoleSpinner);
        roleSpinner.setAdapter(roleAdapter);
    }


    public void onAddPictureButtonClick(View view) {
        //TODO send a request
        //TODO display a notification
    }

    public void onAddFingeprintButtonClick(View view) {
        //TODO send a request
        //TODO display a notification
    }

    public void onSaveButtonClick(View view) {
        //TODO send a request
        //TODO display a notification
        //TODO return from the activity
          /*
            accessing spinner's value
            String dummy = roleSpinner.getSelectedItem().toString();
            Toast.makeText(this, dummy, Toast.LENGTH_SHORT).show();
            */
    }
}
