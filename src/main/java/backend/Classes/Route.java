package backend.Classes;

import backend.Enum.Traffic;

public class Route {
    private final Stop start;
    private final Stop end;
    private final int distance;
    private final int time;
    private int cost;
    private int transports;
    private Traffic traffic;


    public Route(Stop start, Stop end, int distance, int time, int cost, int transports) {
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.time = time;
        this.cost = cost;
        this.transports = transports;
        this.traffic = null;
    }

    public int getTransports() {
        return transports;
    }

    public void setTransports(int transports) {
        this.transports = transports;
    }

    public Stop getStart() {
        return start;
    }

    public Stop getEnd() {
        return end;
    }

    public int getDistance() {
        return distance;
    }

    public int getCost(){
        return cost;
    }

    public int setCost(int cost){
        return this.cost = cost;
    }

    public int getTime() {
        return switch (traffic) {
            case LOW -> (int) (time * 1.25);
            case MEDIUM -> (int) (time * 1.5);
            case HIGH -> (int) (time * 1.75);
            case EXTREME -> (int) (time * 2);
            case CARWRECK -> -1;
            case null -> time;
        };
    }
}