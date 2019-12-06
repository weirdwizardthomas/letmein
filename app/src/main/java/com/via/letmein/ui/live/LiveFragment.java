package com.via.letmein.ui.live;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.ui.main_activity.MainActivity;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;
import java.util.Objects;

import static com.via.letmein.persistence.repository.HouseholdMemberRepository.ERROR_EXPIRED_SESSION_ID;

/**
 * Fragment displaying the live feed from the camera and allowing remote door unlocking.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class LiveFragment extends Fragment {

    public static final int PIN_REQUEST_CODE = 1;

    private LiveViewModel liveViewModel;

    private Button openButton;

    private LibVLC libVLC;
    private VLCVideoLayout vlcVideoLayout;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        liveViewModel = ViewModelProviders.of(this).get(LiveViewModel.class);
        View root = inflater.inflate(R.layout.fragment_live, container, false);

        final ArrayList<String> args = new ArrayList<>();
        args.add("-vvv");
        libVLC = new LibVLC(getContext(), args);
        mediaPlayer = new MediaPlayer(libVLC);
        initialiseLayout(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("fuk", "OnViewCreated");
        String sessionId = Session.getInstance(getContext()).getSessionId();
        liveViewModel.getStreamUrl(sessionId).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null) {
                    String url = (String) apiResponse.getContent();
                    Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                    initialiseStream(url);
                }
                if (apiResponse.isError() && apiResponse.getErrorMessage() != null) {
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("fuk", "pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.detachViews();
        Log.d("fuk", "stop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("fuk", "destroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        libVLC.release();
        Log.d("fuk", "destroy");
    }

    private void initialiseStream(String url) {
        Log.d("fuk", "initialiseStream");
        try {
            mediaPlayer.attachViews(vlcVideoLayout, null, false, false);

            final Media media = new Media(libVLC, Uri.parse(url));
            mediaPlayer.setMedia(media);
            media.release();
            mediaPlayer.play();
        } catch (Exception e) {
            //todo figure this shit out - attaching and detaching when swapping fragments causes crashes
        }
    }

    private void initialiseLayout(View root) {

        vlcVideoLayout = root.findViewById(R.id.videoView);
        openButton = root.findViewById(R.id.openButton);
        openButton.setOnClickListener(v -> {
            //authenticate
            openPin();
            //make the call
            liveViewModel
                    .openDoor(Session.getInstance(getContext()).getSessionId())
                    .observe(this, apiResponse -> {
                        if (apiResponse != null) {
                            if (!apiResponse.isError() && apiResponse.getContent() != null)
                                Toast.makeText(getContext(), "Opening door...", Toast.LENGTH_SHORT).show();

                            if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                                handleErrors(apiResponse.getErrorMessage());

                        }
                    });
        });
    }

    /**
     * Opens the application's pin lock screen
     */
    private void openPin() {
        Intent intent = new Intent(getContext(), EnterPinActivity.class);
        startActivity(intent);
    }

    /**
     * Handles error responses from the server
     * @param errorMessage Error response to be handled
     */
    private void handleErrors(String errorMessage) {

        switch (errorMessage) {
            case ERROR_EXPIRED_SESSION_ID: {
                ((MainActivity) Objects.requireNonNull(getActivity())).login();
            }
        }

        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PIN_REQUEST_CODE: {
                if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                    Toast.makeText(getActivity(), "Request cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Request sent", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
