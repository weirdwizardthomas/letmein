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
import com.via.letmein.persistence.room.entity.Member;

import java.util.List;

/**
 * Fragment for creation of a new member record.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class AddMemberFragment extends Fragment {
    //TODO add a button to upload a custom picture

    private static final int PROGRESS_MIN = 0;
    private static final int PROGRESS_MAX = 100;
    private static final int SECOND_IN_MILISECONDS = 1000;

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
        initialiseProgressBar(root);
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

    /**
     * Initialises the progress bar that gets shown upon clicking {@see AddMemberFragment#addFingerprintButton} or {@see AddMemberFragment#AddPictureButton}.
     *
     * @param root
     */
    private void initialiseProgressBar(View root) {
        countdownInProgress = false;

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        progressTextView = root.findViewById(R.id.progressText);
        progressTextView.setVisibility(View.INVISIBLE);
    }

    /**
     * Initialises all the listeners.
     */
    private void initialiseListeners() {
        onAddCredentialClickListener = v -> {
            if (!countdownInProgress)
                startProgressBar(10);
            //TODO send a request
        };

        onSaveClickListener = v -> {
            //TODO send a request
            //TODO display a notification
            String name = nameInput.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();
            int imageID = R.mipmap.profile_icon_placeholder;

            addMemberViewModel.insert(new Member(name, role, imageID));
            Toast.makeText(v.getContext(), "Saved a new member", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
        };
    }

    /**
     * Initialises the buttons.
     *
     * @param root Parent view
     */
    private void initialiseButtons(final View root) {
        initialiseListeners();

        addFingerprintButton = root.findViewById(R.id.addFingerprintButton);
        addFingerprintButton.setOnClickListener(onAddCredentialClickListener);

        addPictureButton = root.findViewById(R.id.addPictureButton);
        addPictureButton.setOnClickListener(onAddCredentialClickListener);

        //TODO change to a button on the action bar
        floatingActionButton = root.findViewById(R.id.saveMemberButton);
        floatingActionButton.setOnClickListener(onSaveClickListener);
    }

    /**
     * Initialises the {@see AddMemberFragment#roleSpinner}.
     */
    private void initaliseRoleAdapter() {
        List<String> roles = addMemberViewModel.getRoles().getValue();

        roleSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, roles);
        roleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    /**
     * Commences & displays the countdown.
     *
     * @param seconds Number of seconds to count down to 0.
     */
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
                countdownInProgress = false; //unlock the countdown
            }
        };

        progressBar.setProgress(PROGRESS_MIN);
        progressBar.setVisibility(View.VISIBLE);
        progressTextView.setVisibility(View.VISIBLE);
        countdownInProgress = true; //lock the countdown progress
        countDownTimer.start();
    }

}
