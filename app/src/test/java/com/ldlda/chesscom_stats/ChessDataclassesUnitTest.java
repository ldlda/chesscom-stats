package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.ldlda.chesscom_stats.api.data.Player;
import com.ldlda.chesscom_stats.api.data.PlayerStats;
import com.ldlda.chesscom_stats.utils.NetworkRequestExample;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class ChessDataclassesUnitTest {
    @Test
    public void testPlayer() {
        @Language(value = "json") String jsonstr = """
                {
                  "player_id": 174596871,
                  "@id": "https://api.chess.com/pub/player/ldabsbplef",
                  "url": "https://www.chess.com/member/ldabsbplef",
                  "username": "ldabsbplef",
                  "followers": 3,
                  "country": "https://api.chess.com/pub/country/VN",
                  "last_online": 1758268910,
                  "joined": 1645845629,
                  "status": "basic",
                  "is_streamer": false,
                  "verified": false,
                  "league": "Crystal",
                  "streaming_platforms": []
                }
                """;
        Player player = Player.fromJSON(jsonstr);

        URI playerProfilePage;
        try {
            playerProfilePage = new URI("https://www.chess.com/member/ldabsbplef");
        } catch (URISyntaxException e) {
            fail("shit");
            return;
        }

        assertEquals(174596871, player.getPlayerId());

        assertEquals(playerProfilePage, player.getProfilePage());
        assertNull(player.getProfilePictureResource());
    }


    @Test
    public void testPlayer2() {
        @Language(value = "json") String jsonstr;
        try {
            jsonstr = NetworkRequestExample.fetchData("https://api.chess.com/pub/player/hikaru");
        } catch (Exception e) {
            fail(e.getMessage());
            return;
        }
        Player hikaru = Player.fromJSON(jsonstr);
        assertEquals("GM", hikaru.getTitle());
        assertEquals("Hikaru Nakamura", hikaru.getName());
        /// https://www.timestamp-converter.com/ // "2014-01-06T21:20:58Z"
        assertEquals(1389043258, hikaru.getJoined().getEpochSecond());
    }

    @Test
    public void testStats() {
        @Language(value = "json") String jsonstr;
        try {
            jsonstr = NetworkRequestExample.fetchData("https://api.chess.com/pub/player/hikaru/stats");
        } catch (Exception e) {
            fail(e.getMessage());
            return;
        }
        PlayerStats hikaruStats = PlayerStats.fromJSON(jsonstr);
        assertTrue(123 <= Objects.requireNonNull(hikaruStats.getPuzzleRush()).getBest().getScore());
        assertTrue(hikaruStats.getFide() > 2700); // i hope he dont fall off
        /// https://www.timestamp-converter.com/ // "2014-01-06T21:20:58Z"
    }
}
