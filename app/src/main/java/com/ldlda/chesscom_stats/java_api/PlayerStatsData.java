package com.ldlda.chesscom_stats.java_api;

import com.google.gson.annotations.SerializedName;

public class PlayerStatsData {

    @SerializedName("chess_rapid")
    public ChessType rapid;

    @SerializedName("chess_bullet")
    public ChessType bullet;

    @SerializedName("chess_blitz")
    public ChessType blitz;

    @SerializedName("chess960_daily")
    public ChessType chess960Daily;

    @SerializedName("puzzle_rush")
    public PuzzleRush puzzleRush;

    public int fide;

    public static class ChessType {
        public Rating last;
        public Rating best;
        public Record record;
    }

    public static class Rating {
        public int rating;
        public long date;
        public int rd;          // may be missing for 'best'
        public String game;     // may be null except best.rating
    }

    public static class Record {
        public int win;
        public int loss;
        public int draw;
        public long time_per_move;      // only for chess960_daily
        public int timeout_percent;     // only for chess960_daily
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
