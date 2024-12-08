package backend.Classes;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Stop {
    private static int counter = 0;
    private final int id;
    private final List<Integer> adjacencyList;
    private final Point2D location;


    public Stop(double x, double y) {
        this.id = ++counter;
        this.location = new Point2D(x, y); // Set the location using Point2D
        this.adjacencyList = new ArrayList<>();
    }

    public Point2D getLocation() {
        return location;
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
        for(int i = 0; i < adjacencyList.size(); i++){
            if(adjacencyList.get(i) == vertex.getId()){
                adjacencyList.remove(i);
                break;
            }
        }
        for(int i = 0; i < vertex.adjacencyList.size(); i++){
            if(vertex.adjacencyList.get(i) == id){
                vertex.adjacencyList.remove(i);
                break;
            }
        }
    }

    public void clearAdjacencyList() {
        adjacencyList.clear();
    }

}
