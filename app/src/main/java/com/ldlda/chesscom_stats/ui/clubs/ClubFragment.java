package com.ldlda.chesscom_stats.ui.clubs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.databinding.FragmentClubBinding;

import java.util.ArrayList;
import java.util.Collections;

public class ClubFragment extends Fragment {
    public static final String TAG = "ClubFragment";
    private static final int BATCH_SIZE = 10;
    private FragmentClubBinding binding;
    private ClubViewModel viewModel;
    private ClubItemAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClubBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ClubViewModel.class);
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

            // If loading, add null placeholder at end
            if (Boolean.TRUE.equals(viewModel.isLoadingList().getValue())) {
                displayList.add(null);
            }

            adapter.submitList(displayList);
        });

        viewModel.isLoadingUrls().observe(getViewLifecycleOwner(), loading -> {
            binding.progBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            adapter.submitList(Collections.emptyList());
        });

        viewModel.isLoadingList().observe(getViewLifecycleOwner(), loading -> {
/* Dont run ts
            if (loading) {
                var list = new ArrayList<>(adapter.getCurrentList());
                list.add(null);
                adapter.submitList(list);
            }
*/
            var l = new ArrayList<>(adapter.getCurrentList());
            int i = l.size() - 1;
            Log.d(TAG, "observeViewModel: i = " + i);
            if (!loading && i != -1 && l.get(i) == null) {
                l.remove(i);
                adapter.submitList(l);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
//        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        super.onDestroyView();
        binding = null;

    }
}