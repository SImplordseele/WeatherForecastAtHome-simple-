package org.example.weatherforecast;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class GeoConnection {
    public static HttpURLConnection getConnection(String address) {
        String formattedAddress = address.replace(" ", "+");
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                formattedAddress + "&count=1&language=en&format=json";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return conn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
