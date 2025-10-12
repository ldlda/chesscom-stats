package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static kotlinx.coroutines.SupervisorKt.SupervisorJob;

import androidx.annotation.NonNull;

import com.ldlda.chesscom_stats.api.data.player.Player;
import com.ldlda.chesscom_stats.api.data.player.stats.PlayerStats;
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient;
import com.ldlda.chesscom_stats.api.repository.ChessRepoAdapterJava;
import com.ldlda.chesscom_stats.api.repository.ChessRepositoryTimedCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class HybridTest {

    ChessRepoAdapterJava<ChessRepositoryTimedCache> repo;

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
        Player ldabsbplef = repo.getPlayerBlocking("ldabsbplef");
        assertEquals(174596871, ldabsbplef.getPlayerId());
        repo.playerUpdateStatsAsync(ldabsbplef).get();
        PlayerStats ldaStats = ldabsbplef.getPlayerStats();
        assertNotNull(ldaStats);

        var VN = ldabsbplef.getCountryCode(ChessApiClient.CHESS_API_URL, true);
        assertEquals("VN", VN);


    }
}
