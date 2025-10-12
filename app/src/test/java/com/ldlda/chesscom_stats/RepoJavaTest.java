package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static kotlinx.coroutines.SupervisorKt.SupervisorJob;

import androidx.annotation.NonNull;

import com.ldlda.chesscom_stats.api.data.player.Player;
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepository;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class RepoJavaTest {
    ChessRepoAdapterJava<ChessRepository> repo;

    @Before
    public void setUp() {
        repo = ChessRepoAdapterJava.getAdapterJava(new ChessRepositoryTimedCache(), new CoroutineScope() {
            @NonNull
            @Override
            public CoroutineContext getCoroutineContext() {
                return SupervisorJob(null).plus(Dispatchers.getIO());
            }
        });
    }


    @After
    public void tearDown() {
//        repo.close();
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
