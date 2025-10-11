package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.ldlda.chesscom_stats.api.data.player.Player;
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats;
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HybridTest {

    ChessRepoAdapterJava repo;

    @Before
    public void setUp() {
        repo = new ChessRepoAdapterJava();
    }

    @After
    public void tearDown() {
//        repo.close();
    }

    @Test
    public void playerBlocking() throws Exception {
        Player ldabsbplef = repo.getPlayerBlocking("ldabsbplef");
        assertEquals(174596871, ldabsbplef.getPlayerId());
        repo.playerUpdateStatsAsync(ldabsbplef).get();
        PlayerStats ldaStats = ldabsbplef.getPlayerStats();
        assertNotNull(ldaStats);

        var VN = ldabsbplef.getCountryCode(ChessApiClient.CHESS_API_URL, true);
        assertEquals("VN", VN);


    }
}
