package com.via.letmein.ui.add_member;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.ui.main_activity.MainActivity;

import java.util.List;
import java.util.Objects;

import static com.via.letmein.persistence.repository.HouseholdMemberRepository.ERROR_DATABASE_ERROR;
import static com.via.letmein.persistence.repository.HouseholdMemberRepository.ERROR_EXPIRED_SESSION_ID;
import static com.via.letmein.persistence.repository.HouseholdMemberRepository.ERROR_MISSING_REQUIRED_PARAMETERS;
import static com.via.letmein.persistence.repository.HouseholdMemberRepository.ERROR_NAME_IN_USE;
import static com.via.letmein.persistence.repository.HouseholdMemberRepository.ERROR_USERNAME_TOO_SHORT;

/**
 * Fragment for creation of a new member record.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class AddMemberFragment extends Fragment {
    //TODO add a button to upload a custom picture

    private ArrayAdapter<String> roleSpinnerAdapter;
    private Spinner roleSpinner;
    private AddMemberViewModel addMemberViewModel;
    private Button addCredentials;
    private FloatingActionButton floatingActionButton;
    private TextView nameInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addMemberViewModel = ViewModelProviders.of(this).get(AddMemberViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_member, container, false);
        initialiseLayout(root);

        return root;
    }

    /**
     * Initialises the fragment's layout
     *
     * @param root Parent view of the fragment.
     */
    private void initialiseLayout(View root) {
        initialiseButtons(root);
        initaliseRoleAdapter();
        initaliseInput(root);
    }

    /**
     * Initialises input elements.
     *
     * @param root Parent view.
     */
    private void initaliseInput(View root) {
        nameInput = root.findViewById(R.id.nameTextView);
        roleSpinner = root.findViewById(R.id.roleSpinner);
        roleSpinner.setAdapter(roleSpinnerAdapter);
    }

    private void handleErrors(String errorMessage) {
        switch (errorMessage) {
            case ERROR_EXPIRED_SESSION_ID: {
                ((MainActivity) Objects.requireNonNull(getActivity())).login();
                break;
            }
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                Log.i("AddMemberFragment", ERROR_MISSING_REQUIRED_PARAMETERS);
                break;
            }
            case ERROR_USERNAME_TOO_SHORT: {
                //todo add a textview? or a snackbar
                Toast.makeText(getContext(), "Username too short", Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_NAME_IN_USE: {
                //todo add a textview? or a snackbar
                Toast.makeText(getContext(), "Username already in use", Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_DATABASE_ERROR: {
                Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    /**
     * Initialises the buttons.
     *
     * @param root Parent view
     */
    private void initialiseButtons(final View root) {

        addCredentials = root.findViewById(R.id.addFingerprintButton);
        addCredentials.setOnClickListener(v -> {

        });

        floatingActionButton = root.findViewById(R.id.saveMemberButton);
        floatingActionButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();

            addMemberViewModel.createMember(name, role, Session.getInstance(getContext()).getSessionId());
            Toast.makeText(v.getContext(), "Saved a new member", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment).popBackStack();
        });
    }

    /**
     * Initialises the {@see AddMemberFragment#roleSpinner}.
     */
    private void initaliseRoleAdapter() {
        List<String> roles = addMemberViewModel.getRoles().getValue();

        roleSpinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, Objects.requireNonNull(roles));
        roleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

}
