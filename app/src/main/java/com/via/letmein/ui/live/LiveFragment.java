package com.via.letmein.ui.live;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.ui.main_activity.MainActivity;

import java.util.Objects;

import static com.via.letmein.persistence.api.Api.ADDRESS_PORT_DELIMITER;
import static com.via.letmein.persistence.api.Api.API_PATH;
import static com.via.letmein.persistence.api.Api.HTTP;
import static com.via.letmein.persistence.api.Api.PARAMETER_DELIMITER;
import static com.via.letmein.persistence.api.Api.PORT;
import static com.via.letmein.persistence.api.Api.QUERY_DELIMITER;
import static com.via.letmein.persistence.api.Api.SESSION_ID;
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

    private static final String TAG = "Live";

    public static final int PIN_REQUEST_CODE = 1;
    /**
     * Miliseconds in between calls for a new live feed image
     */
    public static final int REFRESH_RATE = 500;

    private LiveViewModel liveViewModel;

    private ImageView liveCameraFeed;
    private Button openButton;

    Handler handler;

    private Runnable imageFetcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        liveViewModel = ViewModelProviders.of(this).get(LiveViewModel.class);
        View root = inflater.inflate(R.layout.fragment_live, container, false);

        initialiseLayout(root);

        //Create a background thread that can update the UI
        initialiseCameraFeed();
        return root;
    }

    private void initialiseCameraFeed() {
        handler = new Handler();
        imageFetcher = new Runnable() {
            //construct base url
            final String baseUrl = HTTP +
                    Session.getInstance(getContext()).getIpAddress() +
                    ADDRESS_PORT_DELIMITER +
                    PORT +
                    API_PATH +
                    "video" +
                    QUERY_DELIMITER +
                    SESSION_ID +
                    PARAMETER_DELIMITER;

            /*          private int i = 0;             */
            @Override
            public void run() {
                /*++i;
                //mockup
                Picasso.get()
                        .load(
                        (i % 2 == 0)
                        ? "https://image.shutterstock.com/image-vector/woman-avatar-isolated-on-white-260nw-1472212124.jpg"
                        : "https://picsum.photos/id/866/536/354")
                        .placeholder(R.drawable.profile_icon_placeholder_background)
                        .into(liveCameraFeed);
*/
                Callback callback = new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                };

                String imagePath = baseUrl + Session.getInstance(getContext()).getSessionId();
                //todo add callback and check for errors
                Picasso.get()
                        .load(imagePath)
                        .placeholder(R.drawable.profile_icon_placeholder_background)
                        .into(liveCameraFeed, callback);
                //wait in between fetches
                handler.postDelayed(this, REFRESH_RATE);
            }
        };

        //Add the runnable to activity's handler
        handler.post(imageFetcher);
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

    /**
     * Initialises the fragment's layout
     *
     * @param root Parent element of the fragment
     */
    private void initialiseLayout(View root) {
        liveCameraFeed = root.findViewById(R.id.cameraFeed);
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
        startActivityForResult(new Intent(getContext(), EnterPinActivity.class), PIN_REQUEST_CODE);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(imageFetcher);
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
