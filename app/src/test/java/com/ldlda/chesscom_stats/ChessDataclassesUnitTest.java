package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.ldlda.chesscom_stats.api.data.Player;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class ChessDataclassesUnitTest {
    @Test
    public void dataclassInitializer() {
        @Language(value = "json")
        String jsonstr = "{\"player_id\":174596871,\"@id\":\"https://api.chess.com/pub/player/ldabsbplef\",\"url\":\"https://www.chess.com/member/ldabsbplef\",\"username\":\"ldabsbplef\",\"followers\":3,\"country\":\"https://api.chess.com/pub/country/VN\",\"last_online\":1758196206,\"joined\":1645845629,\"status\":\"basic\",\"is_streamer\":false,\"verified\":false,\"league\":\"Crystal\",\"streaming_platforms\":[]}";
        Player player = Player.fromJson(jsonstr);

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
}
