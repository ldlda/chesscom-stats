package com.ldlda.chesscom_stats.ui.clubs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.api.data.club.Club;
import com.ldlda.chesscom_stats.databinding.FragmentClubBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClubsFragment extends Fragment {
    public static final String TAG = "ClubFragment";
    private static final int BATCH_SIZE = 10;
    private FragmentClubBinding binding;
    private ClubsViewModel viewModel;
    private ClubItemAdapter adapter;

    private int ahhSize = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClubBinding.inflate(inflater, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(container, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(ClubsViewModel.class);
//        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);


        setupRecyclerView();
        setupSpinner();
        observeViewModel();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new ClubItemAdapter();
        binding.clubList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.clubList.setAdapter(adapter);

        binding.clubList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm != null && !Boolean.TRUE.equals(viewModel.isLoadingList().getValue())) {
                    int lastVisible = lm.findLastVisibleItemPosition();
                    int totalItems = adapter.getItemCount();

                    if (lastVisible >= totalItems - 3) { // Load 3 items before end
                        viewModel.loadMoar(BATCH_SIZE);
                    }
                }
            }
        });
    }

    private void setupSpinner() {
        CountrySpinnerAdapter spinnerAdapter = new CountrySpinnerAdapter(requireContext());
        binding.selectCountry.setAdapter(spinnerAdapter);

        binding.selectCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return; // Skip "Select Country" header
                String isoCode = spinnerAdapter.getCountryCode(position);
                viewModel.fromCountry(isoCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void observeViewModel() {
        viewModel.getClubList().observe(getViewLifecycleOwner(), clubs -> {
            var displayList = new ArrayList<>(clubs);
            Log.d(TAG, "observeViewModel: got an update");
            if (ahhSize != 0) {
                int currrent = displayList.size();
                binding.clubsOverview.setText(String.format("%s of %s loaded (%s error)", currrent, ahhSize, viewModel.getErrorClubs().getValue()));
            } else {
                binding.clubsOverview.setText("not loaded yet");
            }
            // If loading, add null placeholder at end
            if (Boolean.TRUE.equals(viewModel.isLoadingList().getValue())) {
                displayList.add(null);
            }
            adapter.submitList(displayList);
        });

        viewModel.getClubUrls().observe(getViewLifecycleOwner(), l -> ahhSize = l.size());

        viewModel.isLoadingUrls().observe(getViewLifecycleOwner(), loading -> {
            Log.d(TAG, "observeViewModel: loadingg:" + loading);
            binding.progBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            if (loading) adapter.submitList(Collections.emptyList());
            else {
                var i = viewModel.getClubList().getValue();
                if (i == null) return;
                adapter.submitList(new ArrayList<>(i));
            }
        });

        viewModel.isLoadingList().observe(getViewLifecycleOwner(), loading -> {
            Log.d(TAG, "observeViewModel: loadingl: " + loading);

            if (loading) return;
            List<Club> value = viewModel.getClubList().getValue();
            if (value == null) return;
            var l = new ArrayList<>(value);
            adapter.submitList(l);

        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                binding.clubsOverview.setText(error);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        binding = null;

    }
}