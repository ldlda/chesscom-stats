package com.ldlda.chesscom_stats.java_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// This interface defines the Chess.com player profile endpoint
public interface PlayerProfile {

    @GET("player/{username}")
    Call<PlayerProfileData> getPlayerProfile(@Path("username") String username);

    @GET("pub/player/{username}/stats")
    Call<PlayerStatsData> getPlayerStats(@Path("username") String username);
}
