package dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimedRoute {
    private String date;
    private Double longitude;
    private Double latitude;

    public TimedRoute() {
    }

    public TimedRoute(String date, Double longitude, Double latitude) {
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public ZonedDateTime getDateTime(){
        return date == null ? null : ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
