package org.example.weatherforecast;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.Scanner;

public class MainboardController {
    @FXML
    private TextField AddressText;
    @FXML
    private TextField TemperatureText;
    @FXML
    private TextField HumidityText;
    @FXML
    private TextField WindspeedText;
    @FXML
    private Button searchButton;
    @FXML
    private ImageView weatherImage;
    public void initialize() {
        searchButton.setCursor(Cursor.HAND);
        AddressText.setText("");
        TemperatureText.setText("");
        HumidityText.setText("");
        WindspeedText.setText("");
        weatherImage.setSmooth(true);
        weatherImage.setPreserveRatio(true);
        updateSearchButtonImage("/assets/search.png");
    }

    private void updateSearchButtonImage(String source) {
        ImageView buttonImage = new ImageView(new Image(getClass().getResourceAsStream(source)));
        buttonImage.setPreserveRatio(true);
        searchButton.setGraphic(buttonImage);
    }

    public void search() {
        String address = AddressText.getText().replace(" ","+");
        if (Objects.equals(address, "")) return ;
        HttpURLConnection getConn = GeoConnection.getConnection(address);
        InputStream geoInputStream;
        try {
            if (getConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException();
            }
            geoInputStream = getConn.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Can't connect to the Geography API");
        }
        JSONObject geoResult = getGeoResponse(geoInputStream);
        double latitude = (double) geoResult.get("latitude");
        double longitude = (double) geoResult.get("longitude");
        HttpURLConnection weatherConn = WeatherConnection.getConnection(latitude,longitude);
        InputStream weatherInputStream;
        try {
            if (weatherConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException();
            }
            weatherInputStream = weatherConn.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Can't connect to the Weather API ");
        }
        JSONObject weatherResult = (JSONObject) getWeatherResponse(weatherInputStream).get("current");
        String temperature = String.valueOf(weatherResult.get("temperature_2m"));
        String humidity = String.valueOf(weatherResult.get("relative_humidity_2m"));
        String windspeed = String.valueOf(weatherResult.get("wind_speed_10m"));
        displayWeather(temperature,humidity,windspeed);
    }

    public JSONObject getGeoResponse(InputStream inputStream) {
        StringBuilder content = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNext()) {
            content.append(scanner.next());
        }
        scanner.close();
        JSONParser parser = new JSONParser();
        try {
            JSONArray results = (JSONArray) ((JSONObject) parser.parse(content.toString())).get("results");
            return (JSONObject) results.get(0);
        } catch (ParseException e) {
            throw new RuntimeException("Can't retrieve data from the Geography API");
        }
    }

    public JSONObject getWeatherResponse(InputStream inputStream) {
        StringBuilder content = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while(scanner.hasNext()) {
            content.append(scanner.next());
        }
        scanner.close();
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(content.toString());
        } catch (ParseException e) {
            throw new RuntimeException("Can't retrieve data from the Weather API");
        }
    }

    public void displayWeather(String temperature, String humidity, String windspeed) {
        TemperatureText.setText(temperature + " °C");
        HumidityText.setText(humidity + " %");
        WindspeedText.setText(windspeed + " km/h");
        displayWeatherImage(temperature,humidity,windspeed);
    }

    public void displayWeatherImage(String temperature, String humidity, String windspeed) {
        String weatherStatus = getWeatherStatus(temperature, humidity, windspeed);
        Image image = switch (weatherStatus) {
            case "Clear" -> new Image(getClass().getResourceAsStream("/assets/clear.png"));
            case "Cloudy" -> new Image(getClass().getResourceAsStream("/assets/cloudy.png"));
            case "Rain" -> new Image(getClass().getResourceAsStream("/assets/rain.png"));
            default -> new Image(getClass().getResourceAsStream("/assets/snow.png"));
        };
        weatherImage.setImage(image);
    }

    public String getWeatherStatus(String temperature, String humidity, String windspeed) {
        double temp = Double.parseDouble(temperature);
        double hum = Double.parseDouble(humidity);
        double wind = Double.parseDouble(windspeed);
        if (temp > 0 && hum < 70 && wind < 15) {
            return "Clear";
        }
        if (temp > 0 && hum >= 70 && wind < 15) {
            return "Cloudy";
        }
        if (temp > 0 && hum >= 70 && wind >= 15) {
            return "Rain";
        }
        return "Snow";
    }
}