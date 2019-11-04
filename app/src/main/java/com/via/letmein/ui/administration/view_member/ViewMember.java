package com.via.letmein.ui.administration.view_member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.via.letmein.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewMember extends Fragment {
    private ImageView imageView;
    private TextView nameView;
    private TextView roleView;

    //TODO add toolbar
    //TODO add edit option (in the toolbar)
    //TODO add recent entries as a recyclerview list


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_member, container, false);

        Bundle extras = getArguments();

        imageView = root.findViewById(R.id.viewMember_profileImage);
        nameView =  root.findViewById(R.id.viewMember_nameText);
        roleView =  root.findViewById(R.id.viewMember_roleText);

        imageView.setImageResource(extras.getInt("imageID"));
        nameView.setText(extras.getString("name"));
        roleView.setText(extras.getString("role"));

        return root;
    }

}
