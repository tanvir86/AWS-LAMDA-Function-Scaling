package service;

import Repository.WeatherForecast;
import dto.WeatherForecastRequest;
import dto.WeatherForecastResponse;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

public class WeatherService {
    private final String WeatherForecastURI = "https://irgesy5gfg.execute-api.eu-west-1.amazonaws.com";
    private WeatherForecast weatherForecast;
    private ConcurrentHashMap<String, Float> cache ;

    public WeatherService(){
        this.weatherForecast = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(WeatherForecast.class,WeatherForecastURI);

        this.cache = new ConcurrentHashMap<>();
    }

    public Float getWeatherForecast(String date){
        if(this.cache.get(date) != null)
            return this.cache.get(date);

        WeatherForecastResponse weatherForecastResponse = this.weatherForecast.getWeatherForecastByDate(new WeatherForecastRequest(date));

        this.cache.put(date,weatherForecastResponse.getBeaufort());
        return weatherForecastResponse.getBeaufort();
    }
}
