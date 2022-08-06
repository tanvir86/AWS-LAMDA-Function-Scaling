package dto;

import java.time.LocalDate;

public class WeatherForecastRequest {
    public String Date;

    public WeatherForecastRequest(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
