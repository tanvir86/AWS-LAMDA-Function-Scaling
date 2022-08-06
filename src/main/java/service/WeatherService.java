package service;

import Repository.WeatherForecast;
import dto.WeatherForecastRequest;
import dto.WeatherForecastResponse;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import java.time.LocalDate;

public class WeatherService {
    private final String WeatherForecastURI = "https://irgesy5gfg.execute-api.eu-west-1.amazonaws.com";
    private WeatherForecast weatherForecast;

    public WeatherService(){
        this.weatherForecast = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(WeatherForecast.class,WeatherForecastURI);
    }

    public Float getWeatherForecast(LocalDate date){
        WeatherForecastResponse weatherForecastResponse = this.weatherForecast.getWeatherForecastByDate(new WeatherForecastRequest(date.toString()));
        return weatherForecastResponse.getBeaufort();
    }
}
