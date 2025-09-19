package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;

import com.ldlda.chesscom_stats.api.data.Player;
import com.ldlda.chesscom_stats.api.fetch.ChessApi;

import org.junit.Test;

public class ChessAPIUnitTest {

    @Test
    public void testFetch() throws Exception {
        String t = "https://api.chess.com/pub/player/imrosen";
        String u = "imrosen";

        Player imRosen = ChessApi.INSTANCE.getPlayerAsync(u).get();

        Player imAlsoRosen = Player.fromJSON(NetworkRequestExample.fetchData(t));

        assertEquals(imAlsoRosen, imRosen);
    }
}
