package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ldlda.chesscom_stats.api.repository.ChessRepositoryJava;
import com.ldlda.chesscom_stats.api.data.Player;
import com.ldlda.chesscom_stats.api.data.PlayerStats;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class RepoJavaTest {
    @Test
    public void playerBlocking() throws Exception {
        ChessRepositoryJava repo = new ChessRepositoryJava();
        try {
            Player p = repo.getPlayerBlocking("hikaru");
            assertEquals(15448422, p.getPlayerId());
            assertEquals("https://www.chess.com/member/Hikaru", p.getProfilePage().toString());
        } finally {
            repo.close();
        }
    }

    @Test
    public void statsAsync() throws Exception {
        ChessRepositoryJava repo = new ChessRepositoryJava();
        try {
            PlayerStats stats = repo.getPlayerStatsAsync("hikaru").get(10, TimeUnit.SECONDS);
            assertTrue(32400 < Objects.requireNonNull(stats.getBlitz()).getRecord().getWin());
            assertTrue(2700 < stats.getFide());
        } finally {
            repo.close();
        }
    }
}
