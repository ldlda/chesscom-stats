package com.ldlda.chesscom_stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.ldlda.chesscom_stats.api.data.player.Player;
import com.ldlda.chesscom_stats.api.fetch.ChessApiClient;
import com.ldlda.chesscom_stats.api.fetch.ChessApiException;

import org.intellij.lang.annotations.Language;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class ChessApiClientJavaTest {

    private MockWebServer server;
    private ChessApiClient client;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        System.out.println("[TEST] Mock server at: " + server.url("/"));
        // Important: point the client at the mock server
        client = new ChessApiClient(server.url("/").toString());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
        client.close();
    }

    @Test
    public void baseUrl() throws Exception {
        var cuh = HttpUrl.parse(client.getBaseUrl());
        assertNotNull(cuh);
        var list = cuh.encodedPathSegments();
        assertEquals(List.of(""), list);
    }

    @Test
    public void player_success_sync() throws Exception {
        @Language("json")
        String body = """
                {
                  "player_id": 15448422,
                  "username": "Hikaru",
                  "url": "https://www.chess.com/member/Hikaru",
                  "country": "https://api.chess.com/pub/country/US",
                  "joined": 1500000000,
                  "last_online": 1700000000,
                  "status": "premium",
                  "followers": 1000000
                }
                """;
        server.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        Player p = client.getPlayerSync("hikaru");
        assertNotNull(p);
        assertEquals(15448422L, p.getPlayerId());
        assertEquals(1000000, p.getFollowers());

        RecordedRequest recorded = server.takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(recorded);
        System.out.println("[TEST] Recorded path: " + recorded.getPath());
        assertEquals("/player/hikaru", recorded.getPath());
    }

    @Test
    public void player_404_maps_to_NotFound() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(404).setBody("""
                {"code":404,"message":"Not found"}
                """));

        try {
            client.getPlayerSync("does-not-exist");
            fail("Expected ChessApiException.NotFound");
        } catch (ChessApiException e) {
            System.out.println(e.getClass().getName());
            assertTrue(e instanceof ChessApiException.NotFound);
        }

        RecordedRequest recorded = server.takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(recorded);
        System.out.println("[TEST] Recorded path: " + recorded.getPath());
        assertEquals("/player/does-not-exist", recorded.getPath());
    }

    @Test
    public void player_200_bad_body_maps_to_Serialization() throws Exception {
        // Force type mismatches to trigger kotlinx.serialization failure
        String bad = """
                { "player_id": "oops", "username": 123 }
                """;
        server.enqueue(new MockResponse().setResponseCode(200).setBody(bad));

        try {
            client.getPlayerSync("someone");
            fail("Expected ChessApiException.Serialization");
        } catch (ChessApiException e) {
            assertTrue(e instanceof ChessApiException.Serialization);
        }

        RecordedRequest recorded = server.takeRequest(5, TimeUnit.SECONDS);
        assertNotNull(recorded);
        System.out.println("[TEST] Recorded path: " + recorded.getPath());
        assertEquals("/player/someone", recorded.getPath());
    }
}