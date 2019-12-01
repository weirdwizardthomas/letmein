package com.via.letmein.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.via.letmein.R;
import com.via.letmein.persistence.room.entity.Visit;
import com.via.letmein.ui.OpenDoorOnClickListener;
import com.via.letmein.ui.home.visit.VisitAdapter;
import com.via.letmein.ui.live.LiveFragment;

import java.util.List;

/**
 * Fragment showing brief overlook of the app.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class HomeFragment extends Fragment implements VisitAdapter.OnItemClickListener {

    //TODO add control over how many recent entries are shown - number, date range, ... ? - adjustable in settings
    private static final int REQUEST_CODE = 1;

    private HomeViewModel homeViewModel;
    private RecyclerView recentEntriesRecyclerView;
    private RecyclerView.Adapter recentEntriesAdapter;
    private ExtendedFloatingActionButton openButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        openButton = root.findViewById(R.id.openButton);
        openButton.setOnClickListener(new OpenDoorOnClickListener(this));

        initialiseLayout(root);

        return root;
    }

    /**
     * Initialises the fragment's layout
     *
     * @param root Parent view
     */
    private void initialiseLayout(View root) {
        initialiseAdapter();
        initialiseRecyclerView(root);
    }

    /**
     * Initialises the adapter.
     */
    private void initialiseAdapter() {
        List<Visit> dummy = homeViewModel.getData().getValue();
        recentEntriesAdapter = new VisitAdapter(dummy, this);
    }

    /**
     * Initialises the recycler view.
     *
     * @param root Parent view
     */
    private void initialiseRecyclerView(final View root) {
        recentEntriesRecyclerView = root.findViewById(R.id.imageGallery);
        recentEntriesRecyclerView.hasFixedSize();
        recentEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recentEntriesRecyclerView.setAdapter(recentEntriesAdapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LiveFragment.PIN_REQUEST_CODE: {
                if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
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
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_history);
    }
}