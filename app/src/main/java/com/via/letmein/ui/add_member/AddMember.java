package com.via.letmein.ui.add_member;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.via.letmein.R;
import com.via.letmein.ui.MainActivity;

import java.util.List;

public class AddMember extends Fragment {
    public static final int PROGRESS_MIN = 0;
    public static final int SECOND_IN_MILISECONDS = 1000;
    public static final int PROGRESS_MAX = 100;

    private ArrayAdapter<String> roleSpinnerAdapter;
    private AddMemberViewModel addMemberViewModel;
    private ProgressBar progressBar;
    private Button addFingerprintButton;
    private Button addPictureButton;
    private TextView progressTextView;
    private View.OnClickListener onClickListener;

    private boolean countdownInProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_member, container, false);

        initialiseViewModel();
        initialiseProgressBar(root);
        initialiseListeners(); //TODO rename to fit better
        initialiseButtons(root);
        initaliseRoleAdapter();
        initialiseRoleSpinner(root);

        return root;
    }

    private void initialiseProgressBar(View root) {
        countdownInProgress = false;

        progressBar = root.findViewById(R.id.addMember_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        progressTextView = root.findViewById(R.id.addMember_progressTextView);
        progressTextView.setVisibility(View.INVISIBLE);
    }

    private void initialiseListeners() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!countdownInProgress)
                    startProgressBar(10);
                //TODO send a request
                //TODO display a notification
                //TODO add a timer to the progress bar & show it
            }
        };
    }

    private void initialiseButtons(final View root) {
        //TODO extract listeners to member attributes
        addFingerprintButton = root.findViewById(R.id.addMember_addFingerprintButton);
        addFingerprintButton.setOnClickListener(onClickListener);

        addPictureButton = root.findViewById(R.id.addMember_addPictureButton);
        addPictureButton.setOnClickListener(onClickListener);
    }

    private void initialiseViewModel() {
        addMemberViewModel = ViewModelProviders.of(this).get(AddMemberViewModel.class);
    }

    private void initaliseRoleAdapter() {
        List<String> roles = addMemberViewModel.getRoles().getValue();

        roleSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, roles);
        roleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initialiseRoleSpinner(View view) {
        Spinner roleSpinner = view.findViewById(R.id.addMember_selectRoleSpinner);
        roleSpinner.setAdapter(roleSpinnerAdapter);
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
