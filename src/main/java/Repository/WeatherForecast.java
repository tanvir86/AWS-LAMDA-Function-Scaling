package Repository;

import dto.WeatherForecastRequest;
import dto.WeatherForecastResponse;
import feign.Headers;
import feign.RequestLine;

public interface WeatherForecast {

    @RequestLine("POST /weather")
    @Headers({"accept: application/json","x-api-key: d14bc5fb5ebb","Content-Type: application/json"})
    WeatherForecastResponse getWeatherForecastByDate(WeatherForecastRequest weatherForecastRequest);
}
