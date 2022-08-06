package dto;

public class WeatherForecastResponse {
    public float Beaufort;

    public WeatherForecastResponse(float beaufort) {
        Beaufort = beaufort;
    }

    public float getBeaufort() {
        return Beaufort;
    }

    public void setBeaufort(float beaufort) {
        Beaufort = beaufort;
    }
}
