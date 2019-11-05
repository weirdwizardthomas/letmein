package com.via.letmein.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.via.letmein.R;
import com.via.letmein.persistence.entity.Visit;
import com.via.letmein.ui.home.visit.HomeVisitAdapter;

import java.util.List;

public class HomeFragment extends Fragment implements HomeVisitAdapter.OnItemClickListener {

    //TODO add control over how many recent entries are shown - number, date range, ... ? - adjustable in settings

    private HomeViewModel viewModel;
    private RecyclerView recentEntriesRecyclerView;
    private RecyclerView.Adapter recentEntriesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initialiseViewModel();
        initialiseAdapter();
        initialiseRecyclerView(root);

        return root;
    }

    private void initialiseAdapter() {
        List<Visit> dummy = viewModel.getData().getValue();
        recentEntriesAdapter = new HomeVisitAdapter(dummy, this);
    }

    private void initialiseViewModel() {
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    private void initialiseRecyclerView(final View root) {
        recentEntriesRecyclerView = root.findViewById(R.id.home_recentEntriesRecycler);
        recentEntriesRecyclerView.hasFixedSize();
        recentEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recentEntriesRecyclerView.setAdapter(recentEntriesAdapter);
        //TODO set a listener to the entire recycler view
    }

    @Override
    public void onItemClick() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_administration);

    }
}