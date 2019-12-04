package com.via.letmein.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.ui.OpenDoorOnClickListener;
import com.via.letmein.ui.home.visit.VisitAdapter;

import java.util.Objects;

/**
 * Fragment showing brief overlook of the app.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class HomeFragment extends Fragment implements VisitAdapter.OnItemClickListener {
    //todo show last 7 days

    private HomeViewModel homeViewModel;
    private RecyclerView recentEntriesRecyclerView;
    private VisitAdapter recentEntriesAdapter;
    private Button openButton;
    private TextView usernameTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initialiseLayout(root);
        observeData();

        return root;
    }

    private void observeData() {
        homeViewModel.getData().observe(this, visits -> recentEntriesAdapter.setData(visits));
    }

    /**
     * Initialises the fragment's layout
     *
     * @param root Parent view
     */
    private void initialiseLayout(View root) {
        openButton = root.findViewById(R.id.openButton);
        openButton.setOnClickListener(new OpenDoorOnClickListener(this));
        recentEntriesAdapter = new VisitAdapter(homeViewModel.getData().getValue(), this);
        recentEntriesRecyclerView = root.findViewById(R.id.recentEntries);
        recentEntriesRecyclerView.setHasFixedSize(true);
        recentEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recentEntriesRecyclerView.setAdapter(recentEntriesAdapter);
        usernameTextView = root.findViewById(R.id.username);
        usernameTextView.setText(Session.getInstance(getContext()).getUsername());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OpenDoorOnClickListener.PIN_REQUEST_CODE: {
                if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                    Toast.makeText(getActivity(), "CANCELLED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "REQUEST SENT", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /**
     * Opens {@see HistoryFragment}
     */
    @Override
    public void onItemClick() {
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        navController.navigate(R.id.home_to_history);
    }
}