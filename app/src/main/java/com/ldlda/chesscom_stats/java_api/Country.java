package com.ldlda.chesscom_stats.java_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface Country {

    @GET("country/{iso}/players")
    Call<CountryPlayers> getPlayersFromCountry(@Path("iso")String iso);

    @GET("country/{iso}/clubs")
    Call<CountryClubs> getClubsFromCountry(@Path("iso")String iso);
}
