package com.via.letmein.ui.live;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.via.letmein.R;

/**
 * Fragment displaying the live feed from the camera and allowing remote door unlocking.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class LiveFragment extends Fragment {

    private static final int REQUEST_CODE = 1;

    private VideoView liveView;
    private Button openButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_live, container, false);

        //TODO set up a live feed from the server
        liveView = root.findViewById(R.id.videoView);
        openButton = root.findViewById(R.id.openButton);

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EnterPinActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                //todo test where return goes
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                }
                break;
        }
    }
}
