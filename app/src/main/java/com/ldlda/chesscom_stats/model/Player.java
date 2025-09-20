package com.ldlda.chesscom_stats.model;

public class Player {
    private String username;
    private String name;
    private String country;
    private int rank;
    private int score;
    private String avatarUrl;

    public Player(String username, String name, String country, int rank, int score, String avatarUrl) {
        this.username = username;
        this.name = name;
        this.country = country;
        this.rank = rank;
        this.score = score;
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public int getRank() { return rank; }
    public int getScore() { return score; }
    public String getAvatarUrl() { return avatarUrl; }
}

