package com.via.letmein.ui.live;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.via.letmein.R;


public class LiveFragment extends Fragment {

    private VideoView liveView;
    private Button acceptButton;


    public LiveFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_live, container, false);

        //TODO set up a live feed from the server
        liveView = root.findViewById(R.id.liveView_videoView);
        acceptButton = root.findViewById(R.id.liveView_acceptButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send a request to the server
            }
        });

        return root;
    }


}
