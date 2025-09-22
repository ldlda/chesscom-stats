package com.ldlda.chesscom_stats.testutil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/// thank you google search gemini
public class NetworkRequestExample {
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
