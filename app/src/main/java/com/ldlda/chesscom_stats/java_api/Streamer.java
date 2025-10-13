package com.ldlda.chesscom_stats.java_api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Streamer {
    @GET("streamers")
    Call<StreamerData> getStreamerData();
}
