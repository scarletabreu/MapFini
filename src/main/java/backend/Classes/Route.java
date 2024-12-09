package backend.Classes;

import backend.Controller.WorldMap;
import backend.Enum.Traffic;

public class Route {
    private String id;
    private final int start;
    private final int end;
    private final int distance;
    private final int time;
    private final int cost;
    private final int transports;
    private final Traffic traffic;

    public Route(Integer start, Integer end, int distance, int time, int cost, int transports, Traffic traffic) {
        this.id = String.valueOf(WorldMap.getInstance().getCantRoutes()+1);
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.time = time;
        this.cost = cost;
        this.transports = transports;
        this.traffic = traffic;
    }

    public Route(Integer start, Integer end, int distance, int time, int cost, int transports, String id, Traffic traffic) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.time = time;
        this.cost = cost;
        this.transports = transports;
        this.traffic = traffic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Traffic getTraffic() {
        return traffic;
    }

    public int getTransports() {
        return transports;
    }


    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public int getDistance() {
        return distance;
    }

    public int getCost(){
        return cost;
    }


    public int getRawTime(){
        return time;
    }

    public int getTime() {
        return switch (traffic) {
            case LOW -> (int) (time * 1.25);
            case MEDIUM -> (int) (time * 1.5);
            case HIGH -> (int) (time * 1.75);
            case EXTREME -> (time * 2);
            case CARWRECK -> -1;
            case null -> time;
        };
    }
}