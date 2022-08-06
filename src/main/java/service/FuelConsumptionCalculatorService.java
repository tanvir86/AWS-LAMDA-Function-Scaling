package service;

import dto.RoutesFuelEfficiency;
import dto.TimedRoute;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FuelConsumptionCalculatorService {

    private final Integer R = 6371; // earth’s mean radius in Km.
    private final Float FuelConsumptionToCo2ProductionRate = 3.114F;
    private List<TimedRoute> instructions;
    private Float draught;
    private Integer imo;

    private WeatherService weatherService;

    public FuelConsumptionCalculatorService(List<TimedRoute> instructions, Float draught, Integer imo, WeatherService weatherService) {
        this.instructions = instructions;
        this.draught = draught;
        this.imo = imo;
        this.weatherService = weatherService;
    }

    /**
     * Get FuelConsumption Efficiency For Given Instruction List
     * @return RoutesFuelEfficiency
     */
    public RoutesFuelEfficiency getFuelConsumption(){
        Double fuelConsumption =  IntStream.range(1,this.instructions.size())
                .parallel()
                .mapToDouble(i -> getFuelConsumptionBetweenTwoTimedRoutes(this.instructions.get(i-1),this.instructions.get(i)))
                .reduce(0D, Double::sum);

        RoutesFuelEfficiency routesFuelEfficiency = new RoutesFuelEfficiency();
        routesFuelEfficiency.setFuelConsumptionInMetricTon(fuelConsumption);
        routesFuelEfficiency.setCo2EmissionInMetricTon(fuelConsumption * FuelConsumptionToCo2ProductionRate);
        routesFuelEfficiency.setEco(Boolean.FALSE);

        return routesFuelEfficiency;
    }

    /**
     * @param fromTimedRoute - From TimedRoute
     * @param toTimedRoute - To TimedRoute
     * @return Double - Fuel Consumption in Metric Ton
     */
    private Double getFuelConsumptionBetweenTwoTimedRoutes(TimedRoute fromTimedRoute, TimedRoute toTimedRoute){
        Double distanceInNauticalMile = getDistanceBetweenTwoTimedRoutes(fromTimedRoute, toTimedRoute);

        if( distanceInNauticalMile == 0) return 0D;

        ZonedDateTime startDateTime, endDateTime;

        if( fromTimedRoute.getDateTime().isBefore(toTimedRoute.getDateTime())) {
            startDateTime = fromTimedRoute.getDateTime();
            endDateTime = toTimedRoute.getDateTime();
        } else {
            startDateTime = toTimedRoute.getDateTime();
            endDateTime = fromTimedRoute.getDateTime();
        }

        Double durationInHour = Duration.between(startDateTime,endDateTime).toMillis() / (3600 * 1000D);
        Double speedInKnots = distanceInNauticalMile / durationInHour;

        return Stream.iterate(startDateTime, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate().plusDays(1)))
                .parallel()
                .map(
                        new Function<ZonedDateTime, Double>() {
                            @Override
                            public Double apply(ZonedDateTime s) {
                                Double duration;
                                if(s == startDateTime && endDateTime.toLocalDate().isEqual(s.toLocalDate())){
                                    duration = Duration.between( startDateTime, endDateTime).toMillis() / (3600 * 1000D);
                                } else if (s == startDateTime) {
                                    duration = Duration.between(s, s.plusDays(1).truncatedTo(ChronoUnit.DAYS)).toMillis() / (3600 * 1000D);
                                } else if (s.toLocalDate().isEqual(endDateTime.toLocalDate())) {
                                    duration = Duration.between(endDateTime.truncatedTo(ChronoUnit.DAYS), endDateTime).toMillis() / (3600 * 1000D);
                                } else {
                                    duration = 24D;
                                }
                                return getFuelConsumptionByDateAndDuration(s.toLocalDate(),duration,speedInKnots);
                            }
                        }
                )
                .reduce(0D, Double::sum);

    }

    private Double getFuelConsumptionByDateAndDuration(LocalDate date, Double durationInHour, Double speedInKnots){
        Float beaufort = weatherService.getWeatherForecast(date);

        // ToDo: Call Model To Get FuelConsumption
        return new Double(beaufort);
    }


    /**
     * This uses the ‘haversine’ formula to calculate the great-circle distance between two points – that is,
     * the shortest distance over the earth’s surface – giving an ‘as-the-crow-flies’ distance between the points.
     * a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
     * c = 2 ⋅ atan2( √a, √(1−a) )
     * d = R ⋅ c
     * where φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
     *       note that angles need to be in radians to pass to trig functions!
     *
     * @param fromTimedRoute
     * @param toTimedRoute
     * @return Double - Distance in Nautical Mile
     */
    private Double getDistanceBetweenTwoTimedRoutes(TimedRoute fromTimedRoute, TimedRoute toTimedRoute){
        if (fromTimedRoute.getLatitude() == toTimedRoute.getLatitude() && fromTimedRoute.getLongitude() == fromTimedRoute.getLongitude())
            return 0D;

        double latDistance = Math.toRadians(toTimedRoute.getLatitude() - fromTimedRoute.getLatitude()); // Δφ
        double lonDistance = Math.toRadians(toTimedRoute.getLongitude() - fromTimedRoute.getLongitude()); // Δλ

        double a = Math.pow(Math.sin(latDistance / 2),2)
                + Math.cos(Math.toRadians(fromTimedRoute.getLatitude())) * Math.cos(Math.toRadians(toTimedRoute.getLatitude()))
                * Math.pow(Math.sin(lonDistance / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 0.539957; // convert to Nautical Mile
    }
}
