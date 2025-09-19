package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.ldlda.chesscom_stats.api.data.Player;
import com.ldlda.chesscom_stats.api.data.TitleEnum;

import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ChessDataclassesUnitTest {
    @Test
    public void testPlayer() {
        @Language(value = "json")
        String jsonstr = """
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
        @Language(value = "json")
        String jsonstr;
        try {
            jsonstr = NetworkRequestExample.fetchData("https://api.chess.com/pub/player/hikaru");
        } catch (Exception e) {
            fail(e.getMessage());
            return;
        }
        Player hikaru = Player.fromJSON(jsonstr);
        assertEquals(TitleEnum.GM, hikaru.getTitle());
        assertEquals("Hikaru Nakamura", hikaru.getName());
        /// https://www.timestamp-converter.com/ // "2014-01-06T21:20:58Z"
        assertEquals(1389043258, hikaru.getJoined().getEpochSecond());
    }


}

/// thank you google search gemini
class NetworkRequestExample {
    public static String fetchData(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000); // 15 seconds
            connection.setReadTimeout(15000); // 15 seconds

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            } else {
                throw new Exception("HTTP error code: " + responseCode);
            }
        } finally {
            connection.disconnect();
        }
    }
}
