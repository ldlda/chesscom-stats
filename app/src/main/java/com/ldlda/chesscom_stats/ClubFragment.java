package com.ldlda.chesscom_stats;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.ldlda.chesscom_stats.adapter.CountrySpinnerAdapter;
import com.ldlda.chesscom_stats.java_api.ApiClient;
import com.ldlda.chesscom_stats.java_api.Club;
import com.ldlda.chesscom_stats.java_api.ClubData;
import com.ldlda.chesscom_stats.java_api.Country;
import com.ldlda.chesscom_stats.java_api.CountryClubs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ClubFragment extends Fragment {

    private ProgressBar searchProg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club, container, false);

        // Components
        Spinner spinner = view.findViewById(R.id.select_country);
        searchProg = view.findViewById(R.id.prog_bar);

        CountrySpinnerAdapter adapter = new CountrySpinnerAdapter(requireContext());
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                String countryName = (String) parent.getItemAtPosition(position);
                String isoCode = adapter.getCountryCode(position);

                Country api = ApiClient.getClient().create(Country.class);

                Call<CountryClubs> countrycall = api.getClubsFromCountry(isoCode);

                searchProg.setVisibility(View.VISIBLE);

                countrycall.enqueue(new retrofit2.Callback<CountryClubs>(){

                    @Override
                    public void onResponse(Call<CountryClubs> call, Response<CountryClubs> response) {
                        searchProg.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {

                            CountryClubs clubs = response.body();

                            String[] clubUrls = clubs.clubUrlIDs;
                            if (clubUrls == null || clubUrls.length == 0) {
                                Log.e("CountryClub", "This country has no clubs");
                                return;
                            }

                            Club clubApi = ApiClient.getClient().create(Club.class);

                            for (String url : clubUrls) {
                                // Extract club urlID from URL
                                String urlID = url.substring(url.lastIndexOf("/") + 1);

                                Call<ClubData> clubDataCall = clubApi.getClubProfile(urlID);

                                clubDataCall.enqueue(new retrofit2.Callback<ClubData>() {
                                    @Override
                                    public void onResponse(Call<ClubData> call, Response<ClubData> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            ClubData clubData = response.body();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ClubData> call, Throwable t) {
                                        Log.e("Club", "Error fetching club: " + t.getMessage());
                                    }
                                });
                            }


                        }else{
                            Log.e("CountryClub","This country has nothing to see");
                        }
                    }

                    @Override
                    public void onFailure(Call<CountryClubs> call, Throwable t) {
                        searchProg.setVisibility(View.GONE);
                        Log.e("CountryClub","Error " + t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }
}