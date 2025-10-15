package com.ldlda.chesscom_stats.ui.playerdetail;

import com.ldlda.chesscom_stats.api.data.leaderboards.LeaderboardEntry;
import com.ldlda.chesscom_stats.api.data.search.autocomplete.SearchItem;

//everything the activity screen needs (player, country, stats, fav state from db)
// all nullable.
public record PlayerDetailData(String username) {

    public PlayerDetailData(LeaderboardEntry entry) {
        this(entry.getUsername());
    }

    public PlayerDetailData(SearchItem entry) {
        this(entry.getUserView().getUsername());
    }

    // check to do that
    public boolean requireStats() {
        return false;
    }

    public boolean requireCountry() {
        return false;
    }

    public boolean requirePlayer() {
        return false;
    }

    // fucked up
    public boolean requireSearch() {
        // if your Ass decides to implement Online status
        return false;
    }
}

