package com.ldlda.chesscom_stats.ui.clubs;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.java_api.ApiClient;
import com.ldlda.chesscom_stats.java_api.Club;
import com.ldlda.chesscom_stats.java_api.ClubData;
import com.ldlda.chesscom_stats.java_api.Country;
import com.ldlda.chesscom_stats.java_api.CountryClubs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ClubFragment extends Fragment {
    public static final String TAG = "ClubFragment";
    private static final int BATCH_SIZE = 10;
    private final Club clubApi = ApiClient.getClient().create(Club.class);
    private final Country api = ApiClient.getClient().create(Country.class);
    private final List<ClubData> clubList = new ArrayList<>();
    private final List<String> clubUrls = new ArrayList<>();
    private ProgressBar searchProg;
    private RecyclerView recyclerView;
    private ClubItemAdapter clubItemAdapter;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club, container, false);

        Spinner spinner = view.findViewById(R.id.select_country);
        searchProg = view.findViewById(R.id.prog_bar);
        recyclerView = view.findViewById(R.id.club_list);

        clubItemAdapter = new ClubItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(clubItemAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView1, int dx, int dy) {
                super.onScrolled(recyclerView1, dx, dy);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView1.getLayoutManager();
                if (!isLoading && manager != null &&
                        manager.findLastVisibleItemPosition() == clubList.size() - 1) {
                    if (clubList.size() < BATCH_SIZE) return;
                    Toast.makeText(requireContext(), "Loading more items", Toast.LENGTH_SHORT).show();
                    isLoading = true;
                    loadMore();
                }
            }
        });

        CountrySpinnerAdapter adapter = new CountrySpinnerAdapter(requireContext());
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                String isoCode = adapter.getCountryCode(position);
                loadCountryClubs(isoCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void loadCountryClubs(String isoCode) {
        Log.d(TAG, "loadCountryClubs: " + isoCode);
        searchProg.setVisibility(View.VISIBLE);
        api.getClubsFromCountry(isoCode).enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CountryClubs> call, @NonNull Response<CountryClubs> response) {
                searchProg.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    CountryClubs clubs = response.body();
                    if (clubs.clubUrlIDs == null || clubs.clubUrlIDs.isEmpty()) {
                        Log.d(TAG, "This country has ZERO clubs lmao");
                        return;
                    }

                    clubUrls.clear();
                    clubList.clear();
                    Log.d(TAG, "onResponse: cleared");
                    clubItemAdapter.submitList(new ArrayList<>(clubList));
                    assert clubItemAdapter.getCurrentList().isEmpty();

                    clubUrls.addAll(clubs.clubUrlIDs);
                    Log.d(TAG, "onResponse: loaded country code");
                    loadBatch(0, Math.min(BATCH_SIZE, clubUrls.size()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CountryClubs> call, @NonNull Throwable t) {
                searchProg.setVisibility(View.GONE);
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });
    }

    private void loadBatch(int start, int end) {
        if (start >= clubUrls.size()) throw new IllegalArgumentException("never happening");
        clubUrls.subList(start, Math.min(end, clubUrls.size())).parallelStream().forEach(url -> {
            if (url == null) return;
            String urlID = url.substring(url.lastIndexOf("/") + 1);

            clubApi.getClubProfile(urlID).enqueue(new retrofit2.Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ClubData> call, @NonNull Response<ClubData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        clubList.add(response.body());
                        clubItemAdapter.submitList(new ArrayList<>(clubList)); // ass https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-recyclerview
                        Log.d(TAG, "onResponse: submitted " + urlID); //, its " + clubItemAdapter.getCurrentList());
                        // lowk setclubs extreme
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ClubData> call, @NonNull Throwable t) {
                    Log.e(TAG, "Failed to fetch club: " + t.getMessage());
                }
            });
        });
        isLoading = false;
        // bro really thought this runs after those
    }

    private void loadMore() {
        // A debounce to keep things sync lmao
        // preventing multiple overlapping callbacks when isLoading == false
        new Handler().postDelayed(() -> {
            int start = clubList.size();
            int end = Math.min(start + BATCH_SIZE, clubUrls.size());

            loadBatch(start, end);
        }, 500);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }
}
