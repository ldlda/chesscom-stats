package com.ldlda.chesscom_stats.java_api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Puzzle {
    @GET("puzzle")
    Call<PuzzleData> getDailyPuzzleData();

    @GET("puzzle/random")
    Call<PuzzleData> getRandomPuzzleData();
}
