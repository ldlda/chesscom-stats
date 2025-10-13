package com.ldlda.chesscom_stats.java_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClubData {
    @SerializedName("@id")
    public String id;

    public String name;

    @SerializedName("club_id")
    public long clubId;

    public String icon;

    public String country;

    @SerializedName("average_daily_rating")
    public int averageDailyRating;

    @SerializedName("members_count")
    public int membersCount;

    public long created;
    @SerializedName("last_activity")
    public long lastActivity;

    public String visibility;

    @SerializedName("join_request")
    public String joinRequest; // URL to join page

    public List<String> admin; // list of player profile URLs

    public String description; // plain text or HTML description
}
