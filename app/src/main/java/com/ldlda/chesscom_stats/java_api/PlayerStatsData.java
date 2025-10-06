package com.ldlda.chesscom_stats.java_api;

import com.google.gson.annotations.SerializedName;

// Represents data from https://api.chess.com/pub/player/{username}
public class PlayerStatsData {

    @SerializedName("chess_blitz")
    public ChessStat blitz;

    @SerializedName("chess_bullet")
    public ChessStat bullet;

    @SerializedName("chess_rapid")
    public ChessStat rapid;

    @SerializedName("puzzle_rush")
    public PuzzleRush puzzleRush;

    public static class ChessStat {
        public int last;
        public int best;
        public Record record;
    }

    public static class Record {
        public int win;
        public int loss;
        public int draw;
    }

    public static class PuzzleRush {
        public Best best;

        public static class Best {
            @SerializedName("total_attempts")
            public int totalAttempts;

            public int score;
        }
    }


}
