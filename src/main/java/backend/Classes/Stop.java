package backend.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.DocumentId;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stop {
    @DocumentId
    private static int counter = 0;
    private final int id;
    private final List<Integer> adjacencyList;
    private final String name;
    private final Point2D location;

    //  QUITAR ESTA POPO
    private HashMap<String, Integer[]> routeAttributes;

    public Stop(List<Integer> vertices, int[] distance, int[] time, int[] price, int[] transports, String name, double x, double y) {
        this.name = name;
        this.id = ++counter;
        this.location = new Point2D(x, y);
        adjacencyList = new ArrayList<>();
        for (int ind = 0; ind < distance.length; ind++) {
            adjacencyList.add(vertices.get(ind));
            routeAttributes.put(vertices.get(ind).toString(), new Integer[]{distance[ind], time[ind], price[ind], transports[ind]});
        }
    }

    public Stop(String name, double x, double y) {
        this.name = name;
        this.id = ++counter;
        this.location = new Point2D(x, y); // Set the location using Point2D
        this.adjacencyList = new ArrayList<>();
        this.routeAttributes = new HashMap<>();
    }

    public Stop(String name) {
        this.name = name;
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

    public List<Integer> getAdjacencyList() {
        return adjacencyList;
    }

    public void addVertex(Stop vertex, int distance, int time, int price, int transports) {
        if (!adjacencyList.contains(vertex.id)) {
            adjacencyList.add(vertex.id);
            vertex.getAdjacencyList().add(this.id);
            vertex.getRouteAttributes().put(String.valueOf(this.id), new Integer[]{distance, time, price, transports});
            routeAttributes.put(String.valueOf(String.valueOf(vertex.id)), new Integer[]{distance, time, price, transports});
        }
    }

    public void removeVertex(Stop vertex) {
        adjacencyList.remove(vertex.id);
        routeAttributes.remove(String.valueOf(vertex.id));
    }

    public void clearAdjacencyList() {
        adjacencyList.clear();
        routeAttributes.clear();
    }

    public Route getRoute(Stop vertex) {
        for (Integer stop : adjacencyList) {
            if (stop == vertex.id) {
                return new Route(this.id, stop, routeAttributes.get(String.valueOf(vertex.id))[0],
                        routeAttributes.get(String.valueOf(vertex.id))[1], routeAttributes.get(String.valueOf(vertex.id))[2],
                        routeAttributes.get(String.valueOf(vertex.id))[3]);
            }
        }
        System.out.println("Route not found");
        return null;
    }

    public List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();
        for (Integer stop : adjacencyList) {
            routes.add(new Route(this.id, stop, routeAttributes.get(String.valueOf(stop))[0],
                    routeAttributes.get(String.valueOf(stop))[1], routeAttributes.get(String.valueOf(stop))[2],
                    routeAttributes.get(String.valueOf(stop))[3]));
        }
        return routes;
    }

    public HashMap<String, Integer[]> getRouteAttributes() {
        return routeAttributes;
    }

}
