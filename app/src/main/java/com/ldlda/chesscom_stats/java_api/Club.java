package com.ldlda.chesscom_stats.java_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Club {
    @GET("club/{urlID}")
    Call<ClubData> getClubProfile(@Path("urlID") String urlID);
}
