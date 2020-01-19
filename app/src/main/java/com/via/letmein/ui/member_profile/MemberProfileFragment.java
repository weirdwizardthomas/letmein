package com.via.letmein.ui.member_profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.squareup.picasso.Picasso;
import com.via.letmein.R;
import com.via.letmein.persistence.api.ApiResponse;
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
import static com.via.letmein.persistence.api.Error.ERROR_DATABASE_ERROR;
import static com.via.letmein.persistence.api.Error.ERROR_EXPIRED_SESSION_ID;
import static com.via.letmein.persistence.api.Error.ERROR_MISSING_REQUIRED_PARAMETERS;

/**
 * Fragment showing details of a household member
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class MemberProfileFragment extends Fragment implements ImageAdapter.OnItemClickListener {

    private static final String TAG = "MemberProfile";

    public static final String BUNDLE_MEMBER_KEY = "MEMBER";
    public static final int GRID_SPAN = 2;
    public static final String NEW_SESSION_ID_KEY = "newSessionId";
    private static final int PIN_REQUEST_CODE = 1;

    private ImageView profilePicture;
    private TextView name;
    private TextView role;
    private RecyclerView imageGallery;
    private ImageAdapter imageAdapter;
    private ImageButton promoteButton;

    private MemberProfileViewModel memberProfileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        memberProfileViewModel = ViewModelProviders.of(this).get(MemberProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_member_profile, container, false);

        if (getArguments() != null)
            memberProfileViewModel.setHouseholdMember(
                    (HouseholdMember) getArguments().getSerializable(BUNDLE_MEMBER_KEY));

        initialiseLayout(root);
        getProfilePicture();
        getImages();

        return root;
    }


    /**
     * Retrieves a picture from the server and displays it in {@see MemberProfileFragment#profilePicture}
     */
    private void getProfilePicture() {
        //Construct the url path
        String url = HTTP +
                Session.getInstance(getContext()).getIpAddress() +
                ADDRESS_PORT_DELIMITER +
                PORT +
                memberProfileViewModel.getHouseholdMember().getProfilePhoto() +
                QUERY_DELIMITER +
                SESSION_ID +
                PARAMETER_DELIMITER +
                Session.getInstance(getContext()).getSessionId();
        //Retrieve and display the picture
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.profile_icon_placeholder_background)
                .into(profilePicture);
    }

    /**
     * Retrieves all household member's images and displays them in {@see MemberProfileFragment#imageGallery}
     */
    private void getImages() {
        memberProfileViewModel.getImagePaths(Session.getInstance(getContext()).getSessionId()).observe(this, response -> {
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
     * @param root Fragment's root element
     */
    private void initialiseLayout(View root) {

        name = root.findViewById(R.id.name);
        name.setText(memberProfileViewModel
                .getHouseholdMember()
                .getName());

        role = root.findViewById(R.id.action);
        role.setText(memberProfileViewModel
                .getHouseholdMember()
                .getRole());

        profilePicture = root.findViewById(R.id.portrait);

        imageAdapter = new ImageAdapter(this, getContext());

        imageGallery = root.findViewById(R.id.pictures);
        imageGallery.setHasFixedSize(true);
        imageGallery.setLayoutManager(new GridLayoutManager(getContext(), GRID_SPAN));
        imageGallery.setAdapter(imageAdapter);

        promoteButton = root.findViewById(R.id.promoteButton);
        //Create the instance of PopupMenu
        PopupMenu popupMenu = new PopupMenu(getContext(), promoteButton);

        //Inflate the Popup using xml file
        popupMenu.getMenuInflater().inflate(R.menu.promotion_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.promoteConfirm)
                openPin();

            return true;
        });
        promoteButton.setOnClickListener(v -> popupMenu.show());

        //Disable the button if the user is already admin
        if (memberProfileViewModel.getHouseholdMember().isOwner())
            disableButton();

    }

    private void disableButton() {
        promoteButton.setEnabled(false);
        promoteButton.setAlpha(.5f);
    }

    private void openPin() {
        startActivityForResult(new Intent(getContext(), EnterPinActivity.class), PIN_REQUEST_CODE);
    }

    private void openPromotionFragment(ApiResponse apiResponse) {
        String newSessionId = (String) apiResponse.getContent();
        HouseholdMember householdMember = memberProfileViewModel.getHouseholdMember();

        Bundle bundle = new Bundle();

        bundle.putString(NEW_SESSION_ID_KEY, newSessionId);
        bundle.putSerializable(BUNDLE_MEMBER_KEY, householdMember);

        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                .navigate(R.id.member_to_promotion,bundle);
    }


    @Override
    public void onItemClick(String item) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIN_REQUEST_CODE) {
            if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED)
                Toast.makeText(getActivity(), "Request cancelled", Toast.LENGTH_SHORT).show();
            else {
                memberProfileViewModel
                        .promoteAdmin(Session.getInstance(getContext()).getSessionId())
                        .observe(this, apiResponse -> {
                            if (apiResponse != null) {

                                if (!apiResponse.isError() && apiResponse.getContent() != null)
                                    openPromotionFragment(apiResponse);

                                if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                                    handleErrors(apiResponse.getErrorMessage());

                            }
                        });
            }
        }
    }
}
