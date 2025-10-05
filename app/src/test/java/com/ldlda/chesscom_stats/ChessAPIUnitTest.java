package com.ldlda.chesscom_stats;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ldlda.chesscom_stats.api.data.Player;
import com.ldlda.chesscom_stats.api.data.PlayerStats;
import com.ldlda.chesscom_stats.api.data.search.ChessSearchItem;
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient;
import com.ldlda.chesscom_stats.api.fetch.ChessApiException;
import com.ldlda.chesscom_stats.testutil.NetworkRequestExample;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class ChessAPIUnitTest {
    private final ChessApiClient instance = new ChessApiClient();

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

    @Test
    public void search() throws Exception {
        // please dont change your ahh in front of my face
        List<@NotNull ChessSearchItem> contains_ldabsbplef =
                instance.searchPlayersAsync("ldabsbplef").get();

        boolean ldabsbplefFound = false;
        for (ChessSearchItem i : contains_ldabsbplef) {
            if (i.getUserView().getUsername().equals("ldabsbplef")) {
                ldabsbplefFound = true;
                System.out.println(i);
                break;
            }
        }
        assertTrue("i (ldabsbplef) cannot be seen on chess.com", ldabsbplefFound);
    }
}

