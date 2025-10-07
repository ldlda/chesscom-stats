package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ldlda.chesscom_stats.api.data.player.Player;
import com.ldlda.chesscom_stats.api.data.playerstats.PlayerStats;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RepoJavaTest {
    ChessRepoAdapterJava repo;

    @Before
    public void setUp() {
        repo = new ChessRepoAdapterJava();
    }

    @After
    public void tearDown() {
        repo.close();
    }

    @Test
    public void playerBlocking() throws Exception {
        Player p = repo.getPlayerBlocking("hikaru");
        assertEquals(15448422, p.getPlayerId());
        assertEquals("https://www.chess.com/member/Hikaru", p.getProfilePage().toString());
    }

    @Test
    public void statsAsync() throws Exception {
        PlayerStats stats = repo.getPlayerStatsAsync("hikaru").get(10, TimeUnit.SECONDS);
        assertTrue(32400 < Objects.requireNonNull(stats.getBlitz()).getRecord().getWin());
        assertTrue(2700 < stats.getFide());
    }
}
