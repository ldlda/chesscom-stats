package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;

import com.ldlda.chesscom_stats.api.data.Player;
import com.ldlda.chesscom_stats.api.data.PlayerStats;
import com.ldlda.chesscom_stats.api.fetch.DefaultChessApi;
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient;
import com.ldlda.chesscom_stats.api.fetch.ChessApiException;
import com.ldlda.chesscom_stats.testutil.NetworkRequestExample;

import org.junit.Test;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class ChessAPIUnitTest {
    private final ChessApiClient instance = DefaultChessApi.INSTANCE;
    @Test
    public void testFetch() throws Throwable {
        String t = "https://api.chess.com/pub/player/imrosen";
        String u = "imrosen";

        Player imRosen = instance.getPlayerSync(u);
        Player imAlsoRosen = Player.fromJSON(NetworkRequestExample.fetchData(t));

        assertEquals(imAlsoRosen, imRosen);
        String t2 = "https://api.chess.com/pub/player/imrosen/stats";

        PlayerStats rosenStats = instance.getPlayerStatsSync(u);
        PlayerStats alsoRosenStats = PlayerStats.fromJSON(NetworkRequestExample.fetchData(t2));

        assertEquals(alsoRosenStats, rosenStats);
    }

    @Test
    public void testFetchThrows() {
        try {
            Player fhsjkfhskjdhf = instance.getPlayerSync("usaghfklawgfkwahf");
            System.out.println("player does exist OK: " + fhsjkfhskjdhf);
        } catch (ChessApiException e) {
            Response r;
            if (e.getCause() instanceof HttpException) {
                r = ((HttpException) e.getCause()).response();
                if (r == null) return;
                try (ResponseBody r2 = r.errorBody()) {
                    System.out.println(r2.string());
                } catch (IOException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }
}
