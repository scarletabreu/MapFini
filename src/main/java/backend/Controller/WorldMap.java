package backend.Controller;

import backend.Classes.*;
import backend.Enum.Algorithm;
import backend.Enum.Priority;

import java.util.*;

public class WorldMap {
    private static int counter = 0;
    private final String id;
    private final List<Stop> stops;
    private final List<Route> routes;
    private static WorldMap instance;
    private List<Stop> lastCalculatedPath;

    public WorldMap(List<Stop> stops, List<Route> routes) {
        this.id = String.valueOf(++counter);
        this.stops = stops;
        this.routes = routes;
    }

    public String getId() {
        return id;
    }

    public static void setInstance(WorldMap Instance){
        instance = Instance;
    }

    public static WorldMap getInstance() {
        if (instance == null) {
            instance = new WorldMap(new ArrayList<>(), new ArrayList<>());
        }
        return instance;
    }

    public List<Stop> getLastCalculatedPath() {
        return lastCalculatedPath;
    }

    public void createStop(Stop stop) {
        stops.add(stop);
    }

    public void createRoute(Route route) {
        routes.add(route);
    }

    public List<Route> getStopRoutes(int stopId) {
        List<Route> stopRoutes = new ArrayList<>();
        for (Route r : routes) {
            if (r.getStart() == stopId) stopRoutes.add(r);
        }
        return stopRoutes;
    }

    public Route getRoute(int startId, int endId) {
        return routes.stream()
                .filter(r -> r.getStart() == startId && r.getEnd() == endId)
                .findFirst()
                .orElse(null);
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }

    public Stop findStopById(int stopId) {
        return stops.stream()
                .filter(stop -> stop.getId() == stopId)
                .findFirst()
                .orElse(null);
    }

    // General method for pathfinding algorithms to update lastCalculatedPath
    private void updatePath(List<Stop> path, double distance) {
        lastCalculatedPath = path.isEmpty() ? Collections.emptyList() : path;
        if (!path.isEmpty()) {
            System.out.println("Distancia: " + distance);
            System.out.println("Paradas: " + path.stream().map(Stop::getId).toList());
        }
    }

    public void dijkstra(Stop start, Stop end, Priority priority) {
        if (start == null || end == null) return;

        Map<Stop, Integer> distances = new HashMap<>();
        Map<Stop, Stop> predecessors = new HashMap<>();
        Set<Stop> visited = new HashSet<>();
        PriorityQueue<Stop> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        stops.forEach(stop -> distances.put(stop, Integer.MAX_VALUE));
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Stop current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            for (Integer i : current.getAdjacencyList()) {
                Stop neighbor = findStopById(i);
                Route route = getRoute(current.getId(), neighbor.getId());
                if (route == null || calculatePriority(distances.get(current), route, priority) == -1) continue;

                int newDist = calculatePriority(distances.get(current), route, priority);
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        lastCalculatedPath = reconstructPath(predecessors, start, end);
        updatePath(lastCalculatedPath, distances.get(end));
    }

    public void BellmanFord(Stop start, Stop end, Priority priority) {
        if (start == null || end == null) {
            System.out.println("Los nodos de inicio y final no pueden ser nulos.");
            return;
        }

        Map<Stop, Integer> distances = new HashMap<>();
        Map<Stop, Stop> predecessors = new HashMap<>();
        stops.forEach(stop -> distances.put(stop, Integer.MAX_VALUE));
        distances.put(start, 0);

        for (int i = 1; i < stops.size(); i++) {
            boolean updated = false;
            for (Stop current : stops) {
                for (Integer integer : current.getAdjacencyList()) {
                    Stop neighbor = findStopById(integer);
                    Route route = getRoute(current.getId(), neighbor.getId());
                    if (route == null || calculatePriority(distances.get(current), route, priority) == -1) continue;

                    int newDist = calculatePriority(distances.get(current), route, priority);
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        predecessors.put(neighbor, current);
                        updated = true;
                    }
                }
            }
            if (!updated) break;
        }

        lastCalculatedPath = reconstructPath(predecessors, start, end);
        if (lastCalculatedPath.isEmpty()) {
            System.out.println("No existe un camino entre " + start.getId() + " y " + end.getId() + ".");
        } else {
            updatePath(lastCalculatedPath, distances.get(end));
        }
    }

    public void FloydWarshall(Stop start, Stop end, Priority priority) {
        int n = stops.size();
        double[][] dist = new double[n][n];
        Stop[][] next = new Stop[n][n];
        final double NO_PATH = Double.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Stop stopI = stops.get(i);
                Stop stopJ = stops.get(j);
                Route route = getRoute(stopI.getId(), stopJ.getId());

                if (i == j) {
                    dist[i][j] = 0;
                    next[i][j] = stopI;
                } else if (route != null && calculatePriority(0, route, priority) != -1) {
                    dist[i][j] = calculatePriority(0, route, priority);
                    next[i][j] = stopJ;
                } else {
                    dist[i][j] = NO_PATH;
                    next[i][j] = null;
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != NO_PATH && dist[k][j] != NO_PATH && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        if (start != null && end != null) {
            int startIndex = stops.indexOf(start);
            int endIndex = stops.indexOf(end);

            if (dist[startIndex][endIndex] != NO_PATH) {
                updatePath(reconstructFloydPath(startIndex, endIndex, next), dist[startIndex][endIndex]);
            }
        }
    }

    private List<Stop> reconstructPath(Map<Stop, Stop> predecessors, Stop start, Stop end) {
        List<Stop> path = new ArrayList<>();
        Stop current = end;
        while (current != null) {
            path.add(current);
            current = predecessors.get(current);
        }
        Collections.reverse(path);
        return (!path.isEmpty() && path.getFirst().equals(start)) ? path : Collections.emptyList();
    }

    private List<Stop> reconstructFloydPath(int startIndex, int endIndex, Stop[][] next) {
        List<Stop> path = new ArrayList<>();
        int currentIndex = startIndex;
        while (currentIndex != endIndex) {
            if (next[currentIndex][endIndex] == null) return Collections.emptyList();
            path.add(stops.get(currentIndex));
            currentIndex = stops.indexOf(next[currentIndex][endIndex]);
        }
        path.add(stops.get(endIndex));
        return path;
    }

    private int calculatePriority(int currentDistance, Route route, Priority priority) {
        if (route == null) return -1;
        if (priority == null) return currentDistance + route.getDistance() + route.getTime() + route.getTransports() + route.getCost();
        return switch (priority) {
            case DISTANCE -> currentDistance + route.getDistance();
            case TIME -> currentDistance + route.getTime();
            case COST -> currentDistance + route.getCost();
            case TRANSPORTS -> currentDistance + route.getTransports();
        };
    }

    private void printPath(Stop start, Stop end, double distance, List<Stop> path) {
        if (path.isEmpty()) {
            System.out.println("No existe un camino entre " + start.getId() + " y " + end.getId());
        } else {
            System.out.println("Camino desde " + start.getId() + " hasta " + end.getId() + ":");
            System.out.println("Distancia: " + distance);
            System.out.println("Paradas: " + path.stream().map(Stop::getId).toList());
        }
    }

    public void removeStop(Stop selectedStartStop) {
        if (selectedStartStop != null) {
            routes.removeIf(route -> route.getEnd() == selectedStartStop.getId() || route.getStart() == selectedStartStop.getId());
            for (Stop s : stops) {
                s.getAdjacencyList().removeIf(adj -> adj == selectedStartStop.getId());
            }
            stops.remove(selectedStartStop);
        }
    }

    public List<Stop> findBestPath(Stop start, Stop end, Priority priority, Algorithm algorithm) {
        if (algorithm == null) return null;
        switch (algorithm) {
            case DIJKSTRA -> dijkstra(start, end, priority);
            case FLOYD_WARSHALL -> FloydWarshall(start, end, priority);
            case BELLMAN_FORD -> BellmanFord(start, end, priority);
        }
        return lastCalculatedPath;
    }

    public void deleteRoute(Route route){
        for(Stop stop : stops){
            if(stop.getId() == route.getStart()) stop.removeVertex(findStopById(route.getEnd()));
        }
        routes.remove(route);
    }

    public void updateRoute(Route route){
        for(Route r : routes){
            if(Objects.equals(r.getId(), route.getId())){
                routes.remove(r);
                routes.add(route);
                return;
            }
        }
    }

}
