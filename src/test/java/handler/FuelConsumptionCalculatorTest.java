package handler;

import dto.RequestData;
import dto.RoutesFuelEfficiency;
import dto.TimedRoute;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.WeatherService;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FuelConsumptionCalculatorTest {
  private static final Logger logger = LoggerFactory.getLogger(FuelConsumptionCalculatorTest.class);

  @Test
  void fuelConsumptionCalculatorTest() {
    logger.info("FuelConsumptionCalculator TEST");
    RequestData event = new RequestData();
    List<TimedRoute> timedRouteList = new ArrayList<>();
    timedRouteList.add(new TimedRoute("2022-03-02T07:21:00Z",-35.89555555555555,-16.020833333333332));
    timedRouteList.add(new TimedRoute("2022-03-02T22:34:00Z",-34.84582980021234,-13.20614748617757));
    timedRouteList.add(new TimedRoute("2022-03-03T13:07:00Z",-33.721412222776245,-10.617886029950993));
    timedRouteList.add(new TimedRoute("2022-03-05T13:07:00Z",-32.615905059923755,-8.025695574040313));
    timedRouteList.add(new TimedRoute("2022-03-04T19:31:00Z",-31.524429216981993,-5.430593693325585));

    List<List<TimedRoute>> instructions = new ArrayList<List<TimedRoute>>();
    instructions.add(timedRouteList);
    event.setRoutes(instructions);
    event.setDraught(11F);
    event.setImo(123456);

    WeatherService weatherService = mock(WeatherService.class);
    when(weatherService.getWeatherForecast(any())).thenReturn(.7F);


    Context context = new TestContext();
    FuelConsumptionCalculator handler = new FuelConsumptionCalculator(weatherService);

    List<RoutesFuelEfficiency> result = handler.handleRequest(event, context);

    assertNotNull(result);
    assertEquals(result.size(),1);
  }

}
