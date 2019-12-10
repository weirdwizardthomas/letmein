package com.via.letmein.ui.member_profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.HouseholdMember;
import com.via.letmein.ui.main_activity.MainActivity;

import java.util.List;
import java.util.Objects;

import static com.via.letmein.persistence.api.Api.ADDRESS_PORT_DELIMITER;
import static com.via.letmein.persistence.api.Api.HTTP;
import static com.via.letmein.persistence.api.Api.PARAMETER_DELIMITER;
import static com.via.letmein.persistence.api.Api.PORT;
import static com.via.letmein.persistence.api.Api.QUERY_DELIMITER;
import static com.via.letmein.persistence.api.Api.SESSION_ID;
import static com.via.letmein.persistence.api.Errors.ERROR_DATABASE_ERROR;
import static com.via.letmein.persistence.api.Errors.ERROR_EXPIRED_SESSION_ID;
import static com.via.letmein.persistence.api.Errors.ERROR_MISSING_REQUIRED_PARAMETERS;

/**
 *
 */
public class MemberProfileFragment extends Fragment implements ImageAdapter.OnItemClickListener {

    public static final String BUNDLE_MEMBER_KEY = "MEMBER";
    public static final int GRID_SPAN = 3;

    private static final String TAG = "MemberProfile";

    private ImageView profilePicture;
    private TextView name;
    private TextView role;
    private RecyclerView imageGallery;
    private ImageAdapter imageAdapter;

    private MemberProfileViewModel memberProfileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        memberProfileViewModel = ViewModelProviders.of(this).get(MemberProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_member_profile, container, false);

        if (getArguments() != null)
            memberProfileViewModel.setHouseholdMember((HouseholdMember) getArguments().getSerializable(BUNDLE_MEMBER_KEY));

        initialiseLayout(root, getArguments());
        getProfilePicture();
        getImages();

        return root;
    }

    //todo can generalise by passing the path & the target image view - maybe even extend imageView?
    private void getProfilePicture() {
        String url = new StringBuilder()
                .append(HTTP)
                .append(Session.getInstance(getContext()).getIpAddress())
                .append(ADDRESS_PORT_DELIMITER)
                .append(PORT)
                .append(memberProfileViewModel.getHouseholdMember().getProfilePhoto())
                .append(QUERY_DELIMITER)
                .append(SESSION_ID)
                .append(PARAMETER_DELIMITER)
                .append(Session.getInstance(getContext()).getSessionId())
                .toString();
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.profile_icon_placeholder_background)
                .into(profilePicture);

    }

    private void getImages() {
        memberProfileViewModel.getImagePaths("").observe(this, response -> {
            if (response != null) {

                if (!response.isError() && response.getContent() != null)
                    imageAdapter.setData((List<String>) response.getContent());

                if (response.isError() && response.getErrorMessage() != null)
                    handleErrors(response.getErrorMessage());

            }
        });
    }

    /**
     * Handles server's error messages
     *
     * @param errorMessage Error message to be handled
     */
    private void handleErrors(String errorMessage) {
        switch (errorMessage) {
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                Log.d(TAG, ERROR_MISSING_REQUIRED_PARAMETERS);
                break;
            }
            case ERROR_EXPIRED_SESSION_ID: {
                ((MainActivity) Objects.requireNonNull(getActivity())).login();
                break;
            }
            case ERROR_DATABASE_ERROR: {
                Log.i(TAG, ERROR_DATABASE_ERROR);
                break;
            }
        }
    }

    /**
     * Initialises the fragment's layout
     *
     * @param root
     * @param extras
     */
    private void initialiseLayout(View root, Bundle extras) {

        name = root.findViewById(R.id.name);
        role = root.findViewById(R.id.action);

        name.setText(memberProfileViewModel.getHouseholdMember().getName());
        role.setText(memberProfileViewModel.getHouseholdMember().getRole());

        profilePicture = root.findViewById(R.id.portrait);


        imageGallery = root.findViewById(R.id.recentEntries);
        imageGallery.setHasFixedSize(true);
        imageGallery.setLayoutManager(new GridLayoutManager(getContext(), GRID_SPAN));
        imageAdapter = new ImageAdapter(this, getContext());
        imageGallery.setAdapter(imageAdapter);

    }

    @Override
    public void onItemClick(String item) {

    }
}
