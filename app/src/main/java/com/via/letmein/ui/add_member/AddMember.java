package com.via.letmein.ui.add_member;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.via.letmein.persistence.entity.Member;

import java.util.List;

public class AddMember extends Fragment {
    //TODO add a button to upload a custom picture

    public static final int PROGRESS_MIN = 0;
    public static final int PROGRESS_MAX = 100;
    public static final int SECOND_IN_MILISECONDS = 1000;

    private ArrayAdapter<String> roleSpinnerAdapter;
    private Spinner roleSpinner;
    private AddMemberViewModel addMemberViewModel;
    private ProgressBar progressBar;
    private Button addFingerprintButton;
    private Button addPictureButton;
    private FloatingActionButton floatingActionButton;
    private TextView progressTextView;
    private TextView nameInput;
    private View.OnClickListener onAddCredentialClickListener;
    private View.OnClickListener onSaveClickListener;

    private boolean countdownInProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_member, container, false);

        initialiseViewModel();
        initialiseProgressBar(root);
        initialiseButtons(root);
        initaliseRoleAdapter();
        initaliseInput(root);

        return root;
    }

    private void initaliseInput(View root) {
        nameInput = root.findViewById(R.id.nameInput);
        roleSpinner = root.findViewById(R.id.roleSpinner);
        roleSpinner.setAdapter(roleSpinnerAdapter);
    }

    private void initialiseProgressBar(View root) {
        countdownInProgress = false;

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        progressTextView = root.findViewById(R.id.progressText);
        progressTextView.setVisibility(View.INVISIBLE);
    }

    private void initialiseListeners() {
        onAddCredentialClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!countdownInProgress)
                    startProgressBar(10);
                //TODO send a request
                //TODO display a notification
                //TODO add a timer to the progress bar & show it
            }
        };

        onSaveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send a request
                //TODO display a notification
                String name = nameInput.getText().toString();
                String role = roleSpinner.getSelectedItem().toString();
                int imageID = R.mipmap.profile_icon_placeholder;

                addMemberViewModel.insert(new Member(name, role, imageID));
                Toast.makeText(v.getContext(), "Saved a new member", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
            }
        };
    }

    private void initialiseButtons(final View root) {
        initialiseListeners(); //TODO rename to fit better

        addFingerprintButton = root.findViewById(R.id.addFingerprintButton);
        addFingerprintButton.setOnClickListener(onAddCredentialClickListener);

        addPictureButton = root.findViewById(R.id.addPictureButton);
        addPictureButton.setOnClickListener(onAddCredentialClickListener);

        //TODO change to a button on the action bar
        floatingActionButton = root.findViewById(R.id.saveMemberButton);
        floatingActionButton.setOnClickListener(onSaveClickListener);

    }

    private void initialiseViewModel() {
        addMemberViewModel = ViewModelProviders.of(this).get(AddMemberViewModel.class);
    }

    private void initaliseRoleAdapter() {
        List<String> roles = addMemberViewModel.getRoles().getValue();

        roleSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, roles);
        roleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void startProgressBar(final int seconds) {

        final int[] elapsed = {PROGRESS_MIN};
        final int total = seconds * SECOND_IN_MILISECONDS;
        CountDownTimer countDownTimer = new CountDownTimer(total, SECOND_IN_MILISECONDS) {

            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((total - millisUntilFinished) / (total));
                elapsed[0]++;
                int remaining = seconds - elapsed[0];

                progressBar.setProgress(progress);
                progressTextView.setText(Integer.toString(remaining));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(PROGRESS_MAX);
                progressTextView.setText(Integer.toString(PROGRESS_MAX));
                progressBar.setVisibility(View.INVISIBLE);
                progressTextView.setVisibility(View.INVISIBLE);
                countdownInProgress = false;
            }
        };

        progressBar.setProgress(PROGRESS_MIN);
        progressBar.setVisibility(View.VISIBLE);
        progressTextView.setVisibility(View.VISIBLE);
        countdownInProgress = true;
        countDownTimer.start();
    }

}
