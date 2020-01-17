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

import java.util.Objects;

import static com.via.letmein.persistence.api.Error.ERROR_DATABASE_ERROR;
import static com.via.letmein.persistence.api.Error.ERROR_EXPIRED_SESSION_ID;
import static com.via.letmein.persistence.api.Error.ERROR_MISSING_REQUIRED_PARAMETERS;
import static com.via.letmein.persistence.api.Error.ERROR_NAME_ALREADY_IN_USE;
import static com.via.letmein.persistence.api.Error.ERROR_USERNAME_TOO_SHORT;


/**
 * Fragment for creation of a new member record.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class AddMemberFragment extends Fragment {
    private static final String TAG = "AddMemberFragment";

    private ArrayAdapter<String> roleSpinnerAdapter;
    private Spinner roleSpinner;
    private AddMemberViewModel addMemberViewModel;
    private Button addFaceAndFingerprintButton;
    private FloatingActionButton saveMemberFloatingActionButton;
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
     * @param root Parent view of the fragment
     */
    private void initialiseLayout(View root) {
        nameInput = root.findViewById(R.id.nameTextView);
        roleSpinner = root.findViewById(R.id.roleSpinner);

        addFaceAndFingerprintButton = root.findViewById(R.id.addFingerprintButton);
        saveMemberFloatingActionButton = root.findViewById(R.id.saveMemberButton);

        roleSpinnerAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getContext()),
                android.R.layout.simple_spinner_item,
                Objects.requireNonNull(addMemberViewModel.getRoles().getValue()));
        roleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleSpinnerAdapter);

        addFaceAndFingerprintButton.setOnClickListener(v -> {
            //todo send request to the server to take pictures and fingerprints
        });

        saveMemberFloatingActionButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();

            addMemberViewModel
                    .createMember(name, role, Session.getInstance(getContext()).getSessionId())
                    .observe(this, apiResponse -> {
                        if (apiResponse != null) {

                            if (!apiResponse.isError() && apiResponse.getContent() != null)
                                addBiometricData((Integer) apiResponse.getContent());

                            if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                                handleErrors(apiResponse.getErrorMessage());
                        }
                    });


        });


    }

    private void addBiometricData(int userId) {

        addMemberViewModel
                .addBiometricData(userId, Session.getInstance(getContext()).getSessionId())
                .observe(this, apiResponse -> {
                    if (apiResponse != null) {

                        if (!apiResponse.isError() && apiResponse.getContent() != null) {
                            Toast.makeText(getContext(), "Added a new member.", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment).popBackStack();
                        }

                        if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                            handleErrors(apiResponse.getErrorMessage());
                    }
                });

    }

    /**
     * Handles error received from the Api
     *
     * @param errorMessage Error message to be handled
     */
    private void handleErrors(String errorMessage) {
        switch (errorMessage) {
            case ERROR_EXPIRED_SESSION_ID: {
                ((MainActivity) Objects.requireNonNull(getActivity())).login();
                break;
            }
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                Log.d(TAG, ERROR_MISSING_REQUIRED_PARAMETERS);
                break;
            }
            case ERROR_USERNAME_TOO_SHORT: {
                Toast.makeText(getContext(), "Username too short", Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_NAME_ALREADY_IN_USE: {
                Toast.makeText(getContext(), "Username already in use", Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_DATABASE_ERROR: {
                Log.d(TAG, ERROR_DATABASE_ERROR);
                break;
            }
        }
    }
}
