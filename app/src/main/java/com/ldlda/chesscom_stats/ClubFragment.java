package com.ldlda.chesscom_stats;

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

import com.ldlda.chesscom_stats.adapter.ClubAdapter;
import com.ldlda.chesscom_stats.adapter.CountrySpinnerAdapter;
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
    private final Club clubApi = ApiClient.getClient().create(Club.class);
    private final Country api = ApiClient.getClient().create(Country.class);

    private ProgressBar searchProg;
    private RecyclerView recyclerView;
    private ClubAdapter clubAdapter;

    private final List<ClubData> clubList = new ArrayList<>();
    private final List<String> clubUrls = new ArrayList<>();
    private boolean isLoading = false;

    private static final int BATCH_SIZE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club, container, false);

        Spinner spinner = view.findViewById(R.id.select_country);
        searchProg = view.findViewById(R.id.prog_bar);
        recyclerView = view.findViewById(R.id.club_list);

        clubAdapter = new ClubAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(clubAdapter);

        initScrollListener();

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
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return view;
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && manager != null &&
                        manager.findLastVisibleItemPosition() == clubList.size() - 1) {
                    if (clubList.size() < BATCH_SIZE) return;
                    Toast.makeText(requireContext(), "Loading more items", Toast.LENGTH_SHORT).show();
                    loadMore();
                    isLoading = true;
                }
            }
        });
    }

    private void loadCountryClubs(String isoCode) {
        searchProg.setVisibility(View.VISIBLE);
        api.getClubsFromCountry(isoCode).enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(Call<CountryClubs> call, Response<CountryClubs> response) {
                searchProg.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    CountryClubs clubs = response.body();
                    if (clubs.clubUrlIDs == null || clubs.clubUrlIDs.isEmpty()) {
                        Log.e("CountryClub", "This country has ZERO clubs lmao");
                        return;
                    }

                    clubUrls.clear();
                    clubList.clear();
                    clubAdapter.setClubs(clubList);

                    clubUrls.addAll(clubs.clubUrlIDs);

                    loadBatch(0, Math.min(BATCH_SIZE, clubUrls.size()));
                }
            }

            @Override
            public void onFailure(Call<CountryClubs> call, Throwable t) {
                searchProg.setVisibility(View.GONE);
                Log.e("CountryClub", "Error: " + t.getMessage());
            }
        });
    }

    private void loadBatch(int start, int end) {
        for (int i = start; i < end; i++) {
            String url = clubUrls.get(i);
            if (url == null) continue;
            String urlID = url.substring(url.lastIndexOf("/") + 1);

            clubApi.getClubProfile(urlID).enqueue(new retrofit2.Callback<>() {
                @Override
                public void onResponse(Call<ClubData> call, Response<ClubData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        clubList.add(response.body());
                        clubAdapter.setClubs(new ArrayList<>(clubList));
                    }
                }

                @Override
                public void onFailure(Call<ClubData> call, Throwable t) {
                    Log.e("Club", "Failed to fetch club: " + t.getMessage());
                }
            });
        }
        isLoading = false;
    }

    private void loadMore() {
        /*
        This part was supposed to make a loading item, but it lowk messed with the OutOfBoundException, I'm ignoring it for now
        //clubList.add(null);
        //clubAdapter.notifyItemInserted(clubList.size() - 1);
         */

        // A debounce to keep things sync lmao
        // preventing multiple overlapping callbacks when isLoading == false
        new Handler().postDelayed(() -> {
            int start = clubList.size();
            int end = Math.min(start + BATCH_SIZE, clubUrls.size());

            loadBatch(start, end);
        }, 1500);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }
}
