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

import static com.via.letmein.persistence.api.Errors.ERROR_EXPIRED_SESSION_ID;
import static com.via.letmein.persistence.api.Errors.ERROR_LOCKING_DEVICE_NOT_FOUND;
import static com.via.letmein.persistence.api.Errors.ERROR_MISSING_REQUIRED_PARAMETERS;
import static com.via.letmein.persistence.api.Errors.ERROR_NO_LIVE_IMAGE_AVAILABLE;
import static com.via.letmein.persistence.api.Errors.ERROR_UNABLE_GET_IP_ADDRESS;

/**
 * Fragment displaying the live feed from the camera and allowing remote door unlocking.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class LiveFragment extends Fragment {

    public static final int PIN_REQUEST_CODE = 1;
    private static final String TAG = "Live";

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
        libVLC = new LibVLC(Objects.requireNonNull(getContext()), args);
        mediaPlayer = new MediaPlayer(libVLC);
        initialiseLayout(root);
        return root;
    }

   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "OnViewCreated");
        String sessionId = Session.getInstance(getContext()).getSessionId();
        liveViewModel.getStreamUrl(sessionId).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null) {
                    String url = (String) apiResponse.getContent();
                    Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                    initialiseStream(url);
                }
                if (apiResponse.isError() && apiResponse.getErrorMessage() != null) {
                    handleErrors(apiResponse.getErrorMessage());
                }
            }
        });

    }*/

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "pause");
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.detachViews();
        Log.d(TAG, "stop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "destroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        libVLC.release();
        Log.d(TAG, "destroy");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIN_REQUEST_CODE) {
            if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                Toast.makeText(getActivity(), "Request cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Request sent", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialiseStream(String url) {
        Log.d(TAG, "initialiseStream");
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
     *
     * @param errorMessage Error response to be handled
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
            case ERROR_UNABLE_GET_IP_ADDRESS: {
                Log.d(TAG, ERROR_UNABLE_GET_IP_ADDRESS);
                break;
            }
            case ERROR_NO_LIVE_IMAGE_AVAILABLE: {
                Toast.makeText(getContext(), "No stream available", Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_LOCKING_DEVICE_NOT_FOUND: {
                Toast.makeText(getContext(), "Could not contact the lock", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

}
