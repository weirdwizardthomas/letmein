package com.via.letmein.ui;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.amirarcane.lockscreen.activity.EnterPinActivity;

public class OpenDoorOnClickListener implements View.OnClickListener {
    public static final int PIN_REQUEST_CODE = 1;

    private Fragment parent;

    public OpenDoorOnClickListener(Fragment parent) {
        this.parent = parent;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(parent.getActivity(), EnterPinActivity.class);
        parent.startActivityForResult(intent, PIN_REQUEST_CODE);
    }
}
