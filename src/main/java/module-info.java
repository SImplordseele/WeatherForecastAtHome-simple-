module org.example.weatherforecast {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens org.example.weatherforecast to javafx.fxml;
    exports org.example.weatherforecast;
}