package com.via.letmein.ui.view_member;

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
import androidx.recyclerview.widget.RecyclerView;

public class ViewMember extends Fragment {
    public static final String BUNDLE_NAME_KEY = "name";
    public static final String BUNDLE_ROLE_KEY = "role";
    public static final String BUNDLE_IMAGEID_KEY = "imageID";
    public static final String BUNDLE_ID_KEY = "id";

    private ImageView imageView;
    private TextView nameView;
    private TextView roleView;
    private RecyclerView recentEntriesRecyclerView;
    //private RecyclerView.Adapter recentEntriesAdapter;

    //TODO add edit option (in the toolbar)
    //TODO add recent entries as a recyclerview list

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_member, container, false);

        Bundle extras = getArguments();

        imageView = root.findViewById(R.id.portrait);
        nameView = root.findViewById(R.id.name);
        roleView = root.findViewById(R.id.role);
        recentEntriesRecyclerView = root.findViewById(R.id.recentEntriesRecyclerView);

        imageView.setImageResource(extras != null ? extras.getInt(BUNDLE_IMAGEID_KEY) : R.mipmap.profile_icon_placeholder);
        nameView.setText(extras != null ? extras.getString(BUNDLE_NAME_KEY) : "No name found");
        roleView.setText(extras != null ? extras.getString(BUNDLE_ROLE_KEY) : "No role found");

        return root;
    }

}
