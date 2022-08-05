package dto;

public class RoutesFuelEfficiency {
    private Double fuelConsumptionInMetricTon;
    private Double co2EmissionInMetricTon;
    private Boolean eco;

    public Double getFuelConsumptionInMetricTon() {
        return fuelConsumptionInMetricTon;
    }

    public void setFuelConsumptionInMetricTon(Double fuelConsumptionInMetricTon) {
        this.fuelConsumptionInMetricTon = fuelConsumptionInMetricTon;
    }

    public Double getCo2EmissionInMetricTon() {
        return co2EmissionInMetricTon;
    }

    public void setCo2EmissionInMetricTon(Double co2EmissionInMetricTon) {
        this.co2EmissionInMetricTon = co2EmissionInMetricTon;
    }

    public Boolean getEco() {
        return eco;
    }

    public void setEco(Boolean eco) {
        this.eco = eco;
    }
}
