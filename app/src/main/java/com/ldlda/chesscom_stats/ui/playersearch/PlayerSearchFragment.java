package com.ldlda.chesscom_stats.ui.playersearch;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.ldlda.chesscom_stats.R;
import com.ldlda.chesscom_stats.databinding.FragmentPlayerSearchBinding;
import com.ldlda.chesscom_stats.java_api.ApiClient;
import com.ldlda.chesscom_stats.java_api.PlayerProfile;
import com.ldlda.chesscom_stats.java_api.PlayerProfileData;
import com.ldlda.chesscom_stats.java_api.PlayerStatsData;
import com.ldlda.chesscom_stats.ui.favorites.FavoritesViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class PlayerSearchFragment extends Fragment {
    public static final String TAG = "PlayerSearchFragment";
    private SearchView endpoints_plr_search;

    private FragmentPlayerSearchBinding binding;

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
    private Long currentPlayerId = null;

    private String currentTitle = null;
    private long currentlastOnldate = 5555;
    private FavoritesViewModel favoritesViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayerSearchBinding.inflate(getLayoutInflater(), container, false);

        // Initialize ViewModel
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        // Components
        // WHY DO THIS WHEN HAVE BINDINGS
        endpoints_plr_search = binding.allPlrSearch;
        search_prog = binding.progBar;
        title = binding.title;
        account_state = binding.accountState;
        joinDate = binding.joinDate;
        lastOnlDate = binding.lastOnl;
        fav_btn = binding.favBtn;

        bullet_stats = binding.bulletStats;
        blitz_stats = binding.blitzStats;
        rapid_stats = binding.rapidStats;

        // No favoriting until having searched for a player
        fav_btn.setEnabled(false);

        fav_btn.setOnClickListener(v -> {
            if (currentUsername != null && currentPlayerId != null) {
                favoritesViewModel.toggleFavorite(currentPlayerId, currentUsername, currentTitle, String.valueOf(currentlastOnldate),  isFav -> {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(),
                                isFav ? "Added to favorites" : "Removed from favorites",
                                Toast.LENGTH_SHORT).show();
                        updateFavButtonState(isFav);
                    });
                    return null;
                });
            } else {
                Log.e(TAG, "Tried to favorite player without playerId");
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
                Log.i(TAG, "onQueryTextSubmit: Searching for: " + username);

                Call<PlayerProfileData> call = api.getPlayerProfile(username);

                call.enqueue(new retrofit2.Callback<>() {

                    @Override
                    public void onResponse(@NonNull Call<PlayerProfileData> call, @NonNull retrofit2.Response<PlayerProfileData> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            search_prog.setVisibility(View.GONE);

                            // Valid player to favorite
                            fav_btn.setEnabled(true);

                            PlayerProfileData data = response.body();

                            currentUsername = data.profileUrl.substring(data.profileUrl.lastIndexOf("/") + 1);
                            currentPlayerId = data.playerId; // Get from API response
                            currentTitle = data.title;
                            currentlastOnldate = data.lastOnline;


                            // Check if already favorited
                            favoritesViewModel.isFavorite(currentPlayerId, isFav -> {
                                requireActivity().runOnUiThread(() -> updateFavButtonState(isFav));
                                return null;
                            });
                            Glide.with(requireContext()).load(data.avatar).placeholder(R.drawable.ic_person).error(R.drawable.ic_person).into(binding.avatar);
                            setAccountTitle(data.title);
                            setAccountState(data.status);
                            setDates(data.joined, data.lastOnline);

                            // Calling the chess stats, big L chat
                            // dont

                            Call<PlayerStatsData> statCall = api.getPlayerStats(username);

                            statCall.enqueue(new retrofit2.Callback<>() {

                                @Override
                                public void onResponse(@NonNull Call<PlayerStatsData> statCall, @NonNull Response<PlayerStatsData> response) {
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

                                    } else {
                                        Log.e(TAG, "onResponse: HTTP " + response.code());
                                        Toast.makeText(requireContext(),
                                                "Player's stats not found (" + response.code() + ")",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<PlayerStatsData> statCall, @NonNull Throwable t) {
                                    Log.e(TAG, "onFailure: Error" + t.getMessage());
                                    Toast.makeText(requireContext(),
                                            "Request failed for stats: " + t.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Invalid player to favorite
                            fav_btn.setEnabled(false);

                            Log.e(TAG, "onResponse: HTTP " + response.code());
                            Toast.makeText(requireContext(),
                                    "Player not found (" + response.code() + ")",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PlayerProfileData> call, @NonNull Throwable t) {
                        // Invalid player to favorite
                        fav_btn.setEnabled(false);

                        Log.e(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(requireContext(),
                                "Request failed: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
        });

        endpoints_plr_search.setIconified(false);
        endpoints_plr_search.requestFocus();

        // Force keyboard to appear
        getContext();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(endpoints_plr_search, InputMethodManager.SHOW_IMPLICIT);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }

    private void setAccountTitle(String acc_title) {
        int titleRes = R.string.none;

        if (acc_title != null) {
            titleRes = switch (acc_title) {
                case "GM" -> R.string.grandmaster;
                case "IM" -> R.string.i_master;
                case "FM" -> R.string.fide_master;
                case "CM" -> R.string.fide_cand_master;
                default -> titleRes;
            };
        }

        title.setText(titleRes);
    }

    private void setAccountState(String status) {
        int textRes;
        int iconRes = switch (status) {
            case "closed" -> {
                textRes = R.string.closed;
                yield R.drawable.ic_closed_acc;
            }
            case "closed:fair_play_violations" -> {
                textRes = R.string.banned;
                yield R.drawable.ic_banned_acc;
            }
            case "premium" -> {
                textRes = R.string.premium;
                yield R.drawable.ic_premium_acc;
            }
            case "mod" -> {
                textRes = R.string.mod;
                yield R.drawable.ic_mod_acc;
            }
            case "staff" -> {
                textRes = R.string.staff;
                yield R.drawable.ic_staff;
            }
            default -> {
                textRes = R.string.basic_acc;
                yield R.drawable.ic_basic_acc;
            }
        };

        account_state.setText(textRes);
        Drawable icon = ContextCompat.getDrawable(requireContext(), iconRes);
        account_state.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
    }

    private void setDates(long joinedTimeStamp, long lastOnlTimeStamp) {
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
                                  int rapidLast) {
        String bullet_txt = requireContext().getString(R.string.bullet_score) + "\n"
                + "Best: " + bulletBest + "\n"
                + "Last: " + bulletLast + "\n";


        String blitz_txt = requireContext().getString(R.string.blitz_score) + "\n"
                + "Best: " + blitzBest + "\n"
                + "Last: " + blitzLast + "\n";


        String rapid_txt = requireContext().getString(R.string.rapid_score) + "\n"
                + "Best: " + rapidBest + "\n"
                + "Last: " + rapidLast + "\n";

        blitz_stats.setText(blitz_txt);
        bullet_stats.setText(bullet_txt);
        rapid_stats.setText(rapid_txt);
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