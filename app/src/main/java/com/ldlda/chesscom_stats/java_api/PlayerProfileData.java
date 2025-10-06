package com.ldlda.chesscom_stats.java_api;

import com.google.gson.annotations.SerializedName;

// Represents data from https://api.chess.com/pub/player/{username}
public class PlayerProfileData {

    public String username;

    @SerializedName("player_id")
    public long playerId;

    public String title;
    public String status;

    public String name;
    public String avatar;
    public String location;

    public String country;
    public long joined;
    @SerializedName("last_online")
    public long lastOnline;

    public int followers;

    @SerializedName("is_streamer")
    public boolean isStream;
    public int fide;


}
