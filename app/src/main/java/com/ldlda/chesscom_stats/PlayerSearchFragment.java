package com.ldlda.chesscom_stats;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.ldlda.chesscom_stats.java_api.ApiClient;
import com.ldlda.chesscom_stats.java_api.PlayerProfile;
import com.ldlda.chesscom_stats.java_api.PlayerProfileData;
import com.ldlda.chesscom_stats.java_api.PlayerStatsData;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

public class PlayerSearchFragment extends Fragment {
    private SearchView endpoints_plr_search;

    private ProgressBar search_prog;

    private TextView title;

    private TextView account_state;

    private TextView joinDate;
    private TextView lastOnlDate;

    private MaterialButton fav_btn;

    // Chess stats
    private TextView bullet_stats;
    private TextView blitz_stats;
    private TextView rapid_stats;

    private String currentUsername = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_search, container, false);
        // Components
        endpoints_plr_search = view.findViewById(R.id.all_plr_search);
        search_prog = view.findViewById(R.id.prog_bar);
        title = view.findViewById(R.id.title);
        account_state = view.findViewById(R.id.account_state);
        joinDate = view.findViewById(R.id.join_date);
        lastOnlDate = view.findViewById(R.id.last_onl);
        fav_btn = view.findViewById(R.id.fav_btn);

        bullet_stats = view.findViewById(R.id.bullet_stats);
        blitz_stats = view.findViewById(R.id.blitz_stats);
        rapid_stats = view.findViewById(R.id.rapid_stats);

        // No favoriting until having searched for a player
        fav_btn.setEnabled(false);

        fav_btn.setOnClickListener(v -> {
            if (currentUsername != null) {
                Set<String> favs = loadFavorites();

                if (favs.contains(currentUsername)) {
                    favs.remove(currentUsername);
                    saveFavorites(favs);
                    Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    updateFavButtonState(false);
                } else {
                    favs.add(currentUsername);
                    saveFavorites(favs);
                    Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    updateFavButtonState(true);
                }
            } else {
                Log.e("Favoriting", "Tried to favorite unknown player");
            }
        });

        // API https://api.chess.com/pub/player/
        PlayerProfile api = ApiClient.getClient().create(PlayerProfile.class);

        endpoints_plr_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                String username = query.toLowerCase().trim();
                search_prog.setVisibility(View.VISIBLE);
                Log.i("SearchFrag", "Searching for: " + username);

                Call<PlayerProfileData> call = api.getPlayerProfile(username);

                call.enqueue(new retrofit2.Callback<PlayerProfileData>() {

                    @Override
                    public void onResponse(Call<PlayerProfileData> call, retrofit2.Response<PlayerProfileData> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            search_prog.setVisibility(View.GONE);

                            // Valid player to favorite
                            fav_btn.setEnabled(true);

                            PlayerProfileData data = response.body();

                            currentUsername = data.profileUrl.substring(data.profileUrl.lastIndexOf("/") + 1);

                            Set<String> favs = loadFavorites();
                            updateFavButtonState(favs.contains(currentUsername));

                            setAccountTitle(data.title);
                            setAccountState(data.status);
                            setDates(data.joined, data.lastOnline);

                            // Calling the chess stats, big L chat

                            Call<PlayerStatsData> statCall = api.getPlayerStats(username);

                            statCall.enqueue(new retrofit2.Callback<PlayerStatsData>() {

                                @Override
                                public void onResponse(Call<PlayerStatsData> statCall, Response<PlayerStatsData> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        PlayerStatsData dataStats = response.body();

                                        int blitzLast = dataStats.blitz != null && dataStats.blitz.last != null ? dataStats.blitz.last.rating : 0;
                                        int blitzBest = dataStats.blitz != null && dataStats.blitz.best != null ? dataStats.blitz.best.rating : 0;

                                        int bulletLast = dataStats.bullet != null && dataStats.bullet.last != null ? dataStats.bullet.last.rating : 0;
                                        int bulletBest = dataStats.bullet != null && dataStats.bullet.best != null ? dataStats.bullet.best.rating : 0;

                                        int rapidLast = dataStats.rapid != null && dataStats.rapid.last != null ? dataStats.rapid.last.rating : 0;
                                        int rapidBest = dataStats.rapid != null && dataStats.rapid.best != null ? dataStats.rapid.best.rating : 0;

                                        updateChessStats(bulletBest,
                                                bulletLast,
                                                blitzBest,
                                                blitzLast,
                                                rapidBest,
                                                rapidLast);

                                    }else {
                                        Log.e("SearchFrag_Error", "HTTP " + response.code());
                                        Toast.makeText(requireContext(),
                                                "Player's stats not found (" + response.code() + ")",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<PlayerStatsData> statCall, Throwable t) {
                                    Log.e("SearchFrag_Failure", "Error: " + t.getMessage());
                                    Toast.makeText(requireContext(),
                                            "Request failed for stats: " + t.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Invalid player to favorite
                            fav_btn.setEnabled(false);

                            Log.e("SearchFrag_Error", "HTTP " + response.code());
                            Toast.makeText(requireContext(),
                                    "Player not found (" + response.code() + ")",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlayerProfileData> call, Throwable t) {
                        // Invalid player to favorite
                        fav_btn.setEnabled(false);

                        Log.e("SearchFrag_Failure", "Error: " + t.getMessage());
                        Toast.makeText(requireContext(),
                                "Request failed: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
        });

        endpoints_plr_search = view.findViewById(R.id.all_plr_search);
        endpoints_plr_search.setIconified(false);
        endpoints_plr_search.requestFocus();

        // Force keyboard to appear
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.showSoftInput(endpoints_plr_search, InputMethodManager.SHOW_IMPLICIT);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }

    private void setAccountTitle(String acc_title){
        int titleRes = R.string.none;

        if (acc_title != null) {
            switch (acc_title) {
                case "GM": titleRes = R.string.grandmaster; break;
                case "IM": titleRes = R.string.i_master; break;
                case "FM": titleRes = R.string.fide_master; break;
                case "CM": titleRes = R.string.fide_cand_master; break;
            }
        }

        title.setText(titleRes);
    }
    private void setAccountState(String status) {
        int textRes;
        int iconRes;

        switch (status) {
            case "closed":
                textRes = R.string.closed;
                iconRes = R.drawable.ic_closed_acc;
                break;
            case "closed:fair_play_violations":
                textRes = R.string.banned;
                iconRes = R.drawable.ic_banned_acc;
                break;
            case "premium":
                textRes = R.string.premium;
                iconRes = R.drawable.ic_premium_acc;
                break;
            case "mod":
                textRes = R.string.mod;
                iconRes = R.drawable.ic_mod_acc;
                break;
            case "staff":
                textRes = R.string.staff;
                iconRes = R.drawable.ic_staff;
                break;
            default:
                textRes = R.string.basic_acc;
                iconRes = R.drawable.ic_basic_acc;
                break;
        }

        account_state.setText(textRes);
        Drawable icon = ContextCompat.getDrawable(requireContext(), iconRes);
        account_state.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
    }

    private void setDates(long joinedTimeStamp, long lastOnlTimeStamp){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        // First joined date
        Date date_joined = new Date(joinedTimeStamp * 1000L);

        String joinedDateText = requireContext().getString(R.string.join_date) + "\n" + sdf.format(date_joined);

        joinDate.setText(joinedDateText);

        // Last online date
        Date date_lastOnl = new Date(lastOnlTimeStamp * 1000L);

        String lastOnlDateText = requireContext().getString(R.string.lastOnline_date) + "\n" + sdf.format(date_lastOnl);

        lastOnlDate.setText(lastOnlDateText);
    }

    private void updateChessStats(int bulletBest,
                                  int bulletLast,
                                  int blitzBest,
                                  int blitzLast,
                                  int rapidBest,
                                  int rapidLast){
        String bullet_txt = requireContext().getString(R.string.bullet_score)+"\n"
                +"Best: "+ bulletBest+"\n"
                +"Last: "+ bulletLast+"\n";


        String blitz_txt = requireContext().getString(R.string.blitz_score)+"\n"
                +"Best: "+ blitzBest+"\n"
                +"Last: "+ blitzLast+"\n";


        String rapid_txt = requireContext().getString(R.string.rapid_score)+"\n"
                +"Best: "+ rapidBest+"\n"
                +"Last: "+ rapidLast+"\n";

        blitz_stats.setText(blitz_txt);
        bullet_stats.setText(bullet_txt);
        rapid_stats.setText(rapid_txt);
    }

    private File getFavoritesFile() {
        return new File(requireContext().getFilesDir(), "favorites.txt");
    }

    private Set<String> loadFavorites() {
        Set<String> favs = new HashSet<>();
        File file = getFavoritesFile();
        if (!file.exists()) return favs;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                favs.add(line.trim());
            }
        } catch (Exception e) {
            Log.e("Favorites", "Error loading favorites: " + e.getMessage());
        }
        return favs;
    }

    private void saveFavorites(Set<String> favs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFavoritesFile(), false))) {
            for (String name : favs) writer.write(name + "\n");
        } catch (Exception e) {
            Log.e("Favorites", "Error saving favorites: " + e.getMessage());
        }
    }

    private void updateFavButtonState(boolean isFavorited) {
        fav_btn.setEnabled(true);

        if (isFavorited) {
            fav_btn.setAlpha(0.7f); // Greyed out
            fav_btn.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_unfav));
        } else {
            fav_btn.setAlpha(1f);
            fav_btn.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite));
        }
    }

}