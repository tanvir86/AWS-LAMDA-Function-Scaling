package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import dto.RequestData;
import dto.RoutesFuelEfficiency;
import service.FuelConsumptionCalculatorService;
import service.WeatherService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FuelConsumptionCalculator implements RequestHandler<RequestData, List<RoutesFuelEfficiency>> {
    private WeatherService weatherService;

    public FuelConsumptionCalculator() {
        this.weatherService = new WeatherService();
    }
    public FuelConsumptionCalculator(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public List<RoutesFuelEfficiency> handleRequest(RequestData event, Context context)
    {
        LambdaLogger logger = context.getLogger();

        try {
            List<RoutesFuelEfficiency> routesFuelEfficiencyList =  event.getRoutes().stream().parallel()
                    .map(routes -> new FuelConsumptionCalculatorService(routes, event.getDraught(), event.getImo(),weatherService))
                    .map(service -> service.getFuelConsumption())
                    .collect(Collectors.toList());

            RoutesFuelEfficiency minFuelConsumption = routesFuelEfficiencyList.parallelStream()
                    .min(Comparator.comparing(RoutesFuelEfficiency::getFuelConsumptionInMetricTon))
                    .get();

            List<RoutesFuelEfficiency> routesFuelEfficiency = routesFuelEfficiencyList.stream()
                    .map(
                        new Function<RoutesFuelEfficiency, RoutesFuelEfficiency>() {
                            @Override
                            public RoutesFuelEfficiency apply(RoutesFuelEfficiency routesFuelEfficiency) {
                                if( routesFuelEfficiency.getFuelConsumptionInMetricTon() == minFuelConsumption.getFuelConsumptionInMetricTon())
                                    routesFuelEfficiency.setEco(Boolean.TRUE);
                                return routesFuelEfficiency;
                            }
                        }
                    ).collect(Collectors.toList());

            return routesFuelEfficiency;
        } catch (NoSuchElementException noSuchElementException) {
            return new ArrayList<RoutesFuelEfficiency>();
        } catch (Exception exception){
            throw exception;
        }

    }
}
