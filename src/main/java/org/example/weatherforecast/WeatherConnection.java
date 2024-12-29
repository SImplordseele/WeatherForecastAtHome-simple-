package org.example.weatherforecast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class WeatherConnection {
    public static HttpURLConnection getConnection(double latitude, double longitude) {
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m";
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
