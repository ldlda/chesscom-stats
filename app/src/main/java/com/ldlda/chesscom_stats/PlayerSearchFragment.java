package com.ldlda.chesscom_stats;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ldlda.chesscom_stats.java_api.ApiClient;
import com.ldlda.chesscom_stats.java_api.PlayerProfile;
import com.ldlda.chesscom_stats.java_api.PlayerProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;

public class PlayerSearchFragment extends Fragment {
    private SearchView endpoints_plr_search;

    private ProgressBar search_prog;

    private TextView title;

    private TextView account_state;

    private TextView joinDate;
    private TextView lastOnlDate;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_search, container, false);

        queue = Volley.newRequestQueue(requireContext());

        // Components
        endpoints_plr_search = view.findViewById(R.id.all_plr_search);
        search_prog = view.findViewById(R.id.prog_bar);
        title = view.findViewById(R.id.title);
        account_state = view.findViewById(R.id.account_state);
        joinDate = view.findViewById(R.id.join_date);
        lastOnlDate = view.findViewById(R.id.last_onl);

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
                            PlayerProfileData data = response.body();

                            int titleRes = R.string.none;

                            if (data.title != null) {
                                switch (data.title) {
                                    case "GM": titleRes = R.string.grandmaster; break;
                                    case "IM": titleRes = R.string.i_master; break;
                                    case "FM": titleRes = R.string.fide_master; break;
                                    case "CM": titleRes = R.string.fide_cand_master; break;
                                }
                            }

                            setAccountState(data.status);

                            title.setText(titleRes);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

                            // First joined date
                            Date date_joined = new Date(data.joined * 1000L);

                            String joinedDateText = requireContext().getString(R.string.join_date) + "\n" + sdf.format(date_joined);

                            joinDate.setText(joinedDateText);

                            // Last online date
                            Date date_lastOnl = new Date(data.lastOnline * 1000L);

                            String lastOnlDateText = requireContext().getString(R.string.lastOnline_date) + "\n" + sdf.format(date_lastOnl);

                            lastOnlDate.setText(joinedDateText);
                        } else {
                            Log.e("SearchFrag_Error", "HTTP " + response.code());
                            Toast.makeText(requireContext(),
                                    "Player not found (" + response.code() + ")",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlayerProfileData> call, Throwable t) {
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

}