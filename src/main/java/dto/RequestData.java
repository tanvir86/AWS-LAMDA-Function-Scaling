package dto;

import java.util.List;

public class RequestData {
    private Integer imo;
    private Float draught;
    private List<List<TimedRoute>> routes;

    public Integer getImo() {
        return imo;
    }

    public void setImo(Integer imo) {
        this.imo = imo;
    }

    public Float getDraught() {
        return draught;
    }

    public void setDraught(Float draught) {
        this.draught = draught;
    }

    public List<List<TimedRoute>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<TimedRoute>> routes) {
        this.routes = routes;
    }
}