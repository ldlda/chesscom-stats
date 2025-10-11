package com.ldlda.chesscom_stats.java_api;

import com.google.gson.annotations.SerializedName;

public class StreamerData {
    public String username;
    @SerializedName("avatar")
    public String avatarURL;
    @SerializedName("twitch_url")
    public String twitchURL;
    public boolean is_live;
}
