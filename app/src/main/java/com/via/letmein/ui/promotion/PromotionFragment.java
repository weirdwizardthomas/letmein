package com.via.letmein.ui.promotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.via.letmein.R;
import com.via.letmein.persistence.model.HouseholdMember;

import static com.via.letmein.ui.member_profile.MemberProfileFragment.BUNDLE_MEMBER_KEY;
import static com.via.letmein.ui.member_profile.MemberProfileFragment.NEW_SESSION_ID_KEY;

public class PromotionFragment extends Fragment {

    private PromotionViewModel promotionViewModel;

    private TextView ipAddressText;
    private TextView sessionIdText;

    private ImageButton shareButton;
    private ImageButton closeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_promotion, container, false);
        promotionViewModel = ViewModelProviders.of(this).get(PromotionViewModel.class);

        initialiseLayout(root);
        extractBundle();
        displayText();

        return root;
    }

    private void displayText() {
        ipAddressText.setText(promotionViewModel.getIpAddress());
        sessionIdText.setText(promotionViewModel.getSessionID());
    }

    private void initialiseLayout(View root) {
        ipAddressText = root.findViewById(R.id.ipAddressText);
        sessionIdText = root.findViewById(R.id.sessionIdText);
        shareButton = root.findViewById(R.id.shareButton);
        closeButton = root.findViewById(R.id.closeButton);

        shareButton.setOnClickListener(v -> {
            Intent intent = new Intent();

            intent
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, promotionViewModel.getSessionID())
                    .setType("text/plain");

            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);
        });
        closeButton.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
        });
    }

    private void extractBundle() {
        if (getArguments() != null) {
            promotionViewModel.setSessionID(getArguments().getString(NEW_SESSION_ID_KEY));
            promotionViewModel.setMember((HouseholdMember) getArguments().getSerializable(BUNDLE_MEMBER_KEY));
        }
    }
}
