package backend.Classes;

import com.google.cloud.firestore.annotation.DocumentId;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stop {
    @DocumentId
    private static int counter = 0;
    private final int id;
    private final List<Stop> adjacencyList;
    private HashMap<Stop, Integer[]> routeAttributes = new HashMap<>();
    private final Point2D location;

    public Stop(List<Stop> vertices, int[] distance, int[] time, int[] price, int[] transports, double x, double y) {
        this.id = ++counter;
        this.location = new Point2D(x, y);
        adjacencyList = new ArrayList<>();
        for (int ind = 0; ind < distance.length; ind++) {
            adjacencyList.add(vertices.get(ind));
            routeAttributes.put(vertices.get(ind), new Integer[]{distance[ind], time[ind], price[ind], transports[ind]});
        }
    }

    public Stop(double x, double y) {
        this.id = ++counter;
        this.location = new Point2D(x, y); // Set the location using Point2D
        this.adjacencyList = new ArrayList<>();
        this.routeAttributes = new HashMap<>();
    }

    public Stop() {
        this.id = ++counter;
        this.location = new Point2D(0, 0); // Set the location using Point2D
        this.adjacencyList = new ArrayList<>();
        this.routeAttributes = new HashMap<>();
    }

    // Getter for location
    public Point2D getLocation() {
        return location;
    }

    // Getter for X coordinate
    public double getX() {
        return location.getX();
    }

    // Getter for Y coordinate
    public double getY() {
        return location.getY();
    }

    public int getId() {
        return id;
    }

    public List<Stop> getAdjacencyList() {
        return adjacencyList;
    }

    public void addVertex(Stop vertex, int distance, int time, int price, int transports) {
        if (!adjacencyList.contains(vertex)) {
            adjacencyList.add(vertex);
            vertex.getAdjacencyList().add(this);
            vertex.getRouteAttributes().put(this, new Integer[]{distance, time, price, transports});
            routeAttributes.put(vertex, new Integer[]{distance, time, price, transports});
        }
    }

    public void removeVertex(Stop vertex) {
        adjacencyList.remove(vertex);
        routeAttributes.remove(vertex);
    }

    public void clearAdjacencyList() {
        adjacencyList.clear();
        routeAttributes.clear();
    }

    public Route getRoute(Stop vertex) {
        for (Stop stop : adjacencyList) {
            if (stop.id == vertex.id) {
                return new Route(this, stop, routeAttributes.get(vertex)[0],
                        routeAttributes.get(vertex)[1], routeAttributes.get(vertex)[2],
                        routeAttributes.get(vertex)[3]);
            }
        }
        System.out.println("Route not found");
        return null;
    }

    public List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();
        for (Stop stop : adjacencyList) {
            routes.add(new Route(this, stop, routeAttributes.get(stop)[0],
                    routeAttributes.get(stop)[1], routeAttributes.get(stop)[2],
                    routeAttributes.get(stop)[3]));
        }
        return routes;
    }

    public HashMap<Stop, Integer[]> getRouteAttributes() {
        return routeAttributes;
    }
}
