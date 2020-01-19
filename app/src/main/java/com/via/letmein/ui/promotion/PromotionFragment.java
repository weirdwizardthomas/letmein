package com.via.letmein.ui.promotion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.via.letmein.R;
import com.via.letmein.persistence.model.HouseholdMember;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.via.letmein.ui.member_profile.MemberProfileFragment.BUNDLE_MEMBER_KEY;
import static com.via.letmein.ui.member_profile.MemberProfileFragment.NEW_SESSION_ID_KEY;

public class PromotionFragment extends Fragment {

    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;

    private PromotionViewModel promotionViewModel;

    private TextView ipAddressText;
    private TextView sessionIdText;

    private ImageButton shareButton;
    private ImageButton closeButton;

    private ImageView qrImage;

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

        try {
            Bitmap bitmap = encodeAsBitmap(promotionViewModel.getIpAddress() + "|" + promotionViewModel.getSessionID());
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void initialiseLayout(View root) {
        ipAddressText = root.findViewById(R.id.ipAddressText);
        sessionIdText = root.findViewById(R.id.sessionIdText);
        shareButton = root.findViewById(R.id.shareButton);
        closeButton = root.findViewById(R.id.closeButton);
        qrImage = root.findViewById(R.id.qr_image);

        shareButton.setOnClickListener(v -> {
            Intent intent = new Intent();

            intent
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, promotionViewModel.getSessionID())
                    .setType("text/plain");

            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);
        });
        closeButton.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack());
    }

    private void extractBundle() {
        if (getArguments() != null) {
            promotionViewModel.setSessionID(getArguments().getString(NEW_SESSION_ID_KEY));
            promotionViewModel.setMember((HouseholdMember) getArguments().getSerializable(BUNDLE_MEMBER_KEY));
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
