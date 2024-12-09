package backend.Classes;

import backend.Controller.WorldMap;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Stop {
    private final int id;
    private final List<Integer> adjacencyList;
    private final Point2D location;


    public Stop(double x, double y) {
        this.id = WorldMap.getInstance().getCantStops()+1;
        this.location = new Point2D(x, y); // Set the location using Point2D
        this.adjacencyList = new ArrayList<>();
    }

    public double getX() {
        return location.getX();
    }

    public double getY() {
        return location.getY();
    }

    public int getId() {
        return id;
    }

    public List<Integer> getAdjacencyList() {
        return adjacencyList;
    }

    public void addVertex(Stop vertex) {
        if (!adjacencyList.contains(vertex.id)) {
            adjacencyList.add(vertex.id);
            vertex.getAdjacencyList().add(this.id);
        }
    }

    public void removeVertex(Stop vertex) {
        adjacencyList.remove(Integer.valueOf(vertex.getId()));  // Use Integer.valueOf to avoid issues with index changes
        vertex.getAdjacencyList().remove(Integer.valueOf(this.id));  // Same for the other stop
    }

}
