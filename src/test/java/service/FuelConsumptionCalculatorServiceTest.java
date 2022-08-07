package service;

import dto.RoutesFuelEfficiency;
import dto.TimedRoute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FuelConsumptionCalculatorServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(FuelConsumptionCalculatorServiceTest.class);

    @Test
    @DisplayName("Test Fuel Consumption Calculation for Instruction")
    void getFuelConsumptionTest() {
        WeatherService weatherService = mock(WeatherService.class);
        FuelConsumptionModelService fuelConsumptionModelService = mock(FuelConsumptionModelService.class);

        when(weatherService.getWeatherForecast(any())).thenReturn(1F);
        when(fuelConsumptionModelService.isModelExist(any())).thenReturn(Boolean.TRUE);
        when(fuelConsumptionModelService.getFuelConsumption(any(),any(),any(),any())).thenReturn(1.32D);

        List<TimedRoute> timedRouteList = new ArrayList<>();
        timedRouteList.add(new TimedRoute("2022-03-02T07:21:00Z",-35.89555555555555,-16.020833333333332));
        timedRouteList.add(new TimedRoute("2022-03-02T22:34:00Z",-34.84582980021234,-13.20614748617757));
        timedRouteList.add(new TimedRoute("2022-03-05T13:07:00Z",-33.721412222776245,-10.617886029950993));

        FuelConsumptionCalculatorService fuelConsumptionCalculatorService = new FuelConsumptionCalculatorService(timedRouteList,11F,123456,weatherService,fuelConsumptionModelService);

        RoutesFuelEfficiency fuelEfficiency = fuelConsumptionCalculatorService.getFuelConsumption();

        Assertions.assertEquals(fuelEfficiency.getFuelConsumptionInMetricTon(),4.277166666666666);
    }


}