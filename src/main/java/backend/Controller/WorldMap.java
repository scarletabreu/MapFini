package backend.Controller;

import backend.Classes.*;
import backend.Enum.Priority;
import java.util.*;

public class WorldMap {
    private static int counter = 0;
    private String id;
    private final ArrayList<Stop> stops;
    private final ArrayList<String> vertices = new ArrayList<>();
    private final ArrayList<String> rutas = new ArrayList<>();
    private final ArrayList<Stop> stops;
    private final ArrayList<Route> routes;
    private static WorldMap instance;
    private List<Stop> lastCalculatedPath;

    public WorldMap(ArrayList<Stop> stops, ArrayList<Route> routes) {
        this.id = String.valueOf(++counter);
        for (Stop stop : stops) {
            vertices.add(String.valueOf(stop.getId()));
        }
        for (Route route : routes) {
            rutas.add(String.valueOf(route.getId()));
        }
        this.stops = stops;
        this.routes = routes;
    }

    public WorldMap(){
        this.id = String.valueOf(++counter);
        this.stops = new ArrayList<>();
        this.routes = new ArrayList<>();
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
        vertices.add(String.valueOf(stop.getId()));
    }

    public void createRoute(Route route) {
        routes.add(route);
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }

    public Stop findStopById(int id) {
        for (Stop stop : stops) {
            if (stop.getId() == id) {
                return stop;
            }
        }
        return null;
    }

    public void dijkstra(Stop start, Stop end, Priority priority) {

        if (start == null || end == null) return;

        Map<Stop, Integer> distances = new HashMap<>();
        Map<Stop, Stop> predecessors = new HashMap<>();
        Set<Stop> visited = new HashSet<>();
        PriorityQueue<Stop> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (Stop stop : stops) distances.put(stop, Integer.MAX_VALUE);
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Stop current = queue.poll();
            if (visited.contains(current)) continue;

            if (current.equals(end)) {
                break;
            }

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (Integer i : current.getAdjacencyList()) {
                Stop neighbor = findStopById(i);
                Route route = current.getRoute(neighbor);
                if (route == null || calculatePriority(distances.get(current), route, priority) == -1) continue;

                int newDist = calculatePriority(distances.get(current), route, priority);
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (distances.get(end) == Integer.MAX_VALUE) {
            System.out.println("No existe una ruta desde " + start.getId() + " hasta " + end.getId());
        } else {
            if(priority == null){
                System.out.println("Precio promedio más corto de " + start.getId() + " a " + end.getId() + ": " + distances.get(end));
                System.out.println("Ruta: " + routeList(start, end, predecessors));
            }
            else System.out.println("Distancia más corta de " + start.getId() + " a " + end.getId() + " usando " + priority.name() + ": " + distances.get(end));
            System.out.println("Ruta: " + routeList(start, end, predecessors));
        }


        lastCalculatedPath = reconstructPath(predecessors, start, end);
        printPath(start, end, distances.get(end), lastCalculatedPath);
    }

    public void BellmanFord(Stop start, Priority priority) {
        HashMap<Stop, Integer> distances = new HashMap<>();
        HashMap<Stop, List<Stop>> paths = new HashMap<>();
        List<Stop> allStops = getAllStops();  // Evitar múltiples llamadas

        for (Stop stop : allStops) {
            distances.put(stop, Integer.MAX_VALUE);
            paths.put(stop, new ArrayList<>());
    public void BellmanFord(Stop start, Stop end, Priority priority) {
        if (start == null || end == null) {
            System.out.println("Los nodos de inicio y final no pueden ser nulos.");
            return;

        }

        Map<Stop, Integer> distances = new HashMap<>();
        Map<Stop, Stop> predecessors = new HashMap<>();
        for (Stop stop : stops) distances.put(stop, Integer.MAX_VALUE);
        distances.put(start, 0);

        paths.get(start).add(start);

        for (int i = 1; i < allStops.size(); i++) {
            boolean updated = false;
            for (Stop stop : allStops) {
                int currentDistance = distances.get(stop);
                for (Integer integer : stop.getAdjacencyList()) {
                    Stop neighbor = findStopById(integer);
                    int newDist = calculatePriority(currentDistance, stop.getRoute(neighbor), priority);
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        paths.get(neighbor).clear();
                        paths.get(neighbor).addAll(paths.get(stop));
                        paths.get(neighbor).add(neighbor);

        // Relax edges |V| - 1 times
        for (int i = 1; i < stops.size(); i++) {
            boolean updated = false;
            for (Stop current : stops) {
                for (Stop neighbor : current.getAdjacencyList()) {
                    Route route = current.getRoute(neighbor);
                    if (route == null || calculatePriority(distances.get(current), route, priority) == -1) continue;

                    int newDist = calculatePriority(distances.get(current), route, priority);
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        predecessors.put(neighbor, current);

                        updated = true;
                    }
                }
            }
            if (!updated) {
                break;
            }
        }

        for (Stop stop : allStops) {
            System.out.println("Distancia más corta de " + start.getId() + " a " + stop.getId() + ": " + distances.get(stop));
            System.out.println("Ruta: " + paths.get(stop).stream().map(Stop::getId).toList());

            if (!updated) break;
        }

        // Check for negative-weight cycles
        for (Stop current : stops) {
            for (Stop neighbor : current.getAdjacencyList()) {
                Route route = current.getRoute(neighbor);
                if (route == null || calculatePriority(distances.get(current), route, priority) == -1) continue;

                int newDist = calculatePriority(distances.get(current), route, priority);
                if (newDist < distances.get(neighbor)) {
                    System.out.println("El grafo contiene un ciclo de peso negativo.");
                    return;
                }
            }
        }

        // Reconstruct and print the best path from start to end
        lastCalculatedPath = reconstructPath(predecessors, start, end);
        if (lastCalculatedPath.isEmpty()) {
            System.out.println("No existe un camino entre " + start.getId() + " y " + end.getId() + ".");
        } else {
            printPath(start, end, distances.get(end), lastCalculatedPath);
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
                Route route = stopI.getRoute(stopJ);

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
                if (dist[i][k] < NO_PATH) {
                    for (int j = 0; j < n; j++) {
                        if (dist[k][j] < NO_PATH && dist[i][k] + dist[k][j] < dist[i][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                            next[i][j] = next[i][k];
                        }
                    }
                }
            }
        }

        if(start == null || end == null) {
            System.out.println("Distancias más cortas entre todas las paradas:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] == NO_PATH) {
                        System.out.printf("No hay camino de %d a %d%n", stops.get(i).getId(), stops.get(j).getId());
                    } else {
                        System.out.printf("Distancia más corta de %d a %d: %d%n", stops.get(i).getId(), stops.get(j).getId(), (int)dist[i][j]);
                        System.out.print("Ruta: ");
                        printPath(i, j, next);
                        System.out.println();
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

            if (dist[startIndex][endIndex] == NO_PATH) {
                System.out.println("No path exists from " + start.getId() + " to " + end.getId());
                return;
            }

            System.out.println("The shortest distance from stop " + start.getId() + " to stop " + end.getId() + " is: " + dist[startIndex][endIndex]);

            System.out.print("Path: ");
            printPath(startIndex, endIndex, next);
            System.out.println();
        }
    }



    private void printPath(int i, int j, int[][] next) {
        if (next[i][j] == -1) {
            System.out.print("No existe una ruta");
            return;
        }
        List<Integer> path = new ArrayList<>();
        while (i != j) {
            path.add(stops.get(i).getId());
            i = next[i][j];
        }
        path.add(stops.get(j).getId());
        for (int id : path) {
            System.out.print(id + " ");

            if (dist[startIndex][endIndex] != NO_PATH) {
                printPath(start, end, dist[startIndex][endIndex], reconstructFloydPath(startIndex, endIndex, next));
            }
        }
    }

    public void Prim() {
        Set<Route> mstEdges = new HashSet<>();
        Set<Stop> mstVertices = new HashSet<>();
        PriorityQueue<Route> edgeQueue = new PriorityQueue<>(Comparator.comparingInt(Route::getCost));

        Stop start = stops.get(0);

        Stop start = stops.getFirst();
        mstVertices.add(start);
        edgeQueue.addAll(start.getRoutes());

        while (mstVertices.size() < stops.size() && !edgeQueue.isEmpty()) {
            Route minEdge = edgeQueue.poll();
            Stop nextVertex = findStopById(minEdge.getEnd());

            if (mstVertices.contains(nextVertex)) {
                continue;
            }

            if (mstVertices.contains(nextVertex)) continue;

            mstVertices.add(nextVertex);
            mstEdges.add(minEdge);

            for (Route edge : nextVertex.getRoutes()) {
                if (!mstVertices.contains(findStopById(edge.getEnd()))) {
                    edgeQueue.add(edge);
                }
            }

            System.out.println("Agregando arista: " + minEdge.getStart() + " -> " + minEdge.getEnd() + " Costo: " + minEdge.getCost());
        }

        System.out.println("Árbol de expansión mínima:");
        for (Route edge : mstEdges) {
            System.out.println(edge.getStart() + " -> " + edge.getEnd() + " Costo: " + edge.getCost());
        }

                if (!mstVertices.contains(edge.getEnd())) edgeQueue.add(edge);
            }

            System.out.println("Agregando arista: " + minEdge.getStart().getId() + " -> " + minEdge.getEnd().getId());
        }

        mstEdges.forEach(edge -> System.out.println(edge.getStart().getId() + " -> " + edge.getEnd().getId()));
    }

    public List<Route> Kruskal() {
        List<Route> result = new ArrayList<>();
        List<Route> edges = new ArrayList<>();

        for (Stop stop : stops) {
            edges.addAll(stop.getRoutes());
        }

        Collections.sort(edges, Comparator.comparingInt(Route::getDistance));

        UnionFind uf = new UnionFind();

        stops.forEach(stop -> edges.addAll(stop.getRoutes()));
        edges.sort(Comparator.comparingInt(Route::getDistance));


        UnionFind uf = new UnionFind();
        for (Route edge : edges) {
            int startId = edge.getStart();
            int endId = edge.getEnd();


            int startId = edge.getStart().getId();
            int endId = edge.getEnd().getId();

            if (!uf.connected(startId, endId)) {
                uf.union(startId, endId);
                result.add(edge);
            }
        }

        return result;
    }

    private List<Stop> reconstructPath(Map<Stop, Stop> predecessors, Stop start, Stop end) {
        List<Stop> path = new ArrayList<>();
        Stop current = end;
        while (current != null) {
            path.add(current);
            current = predecessors.get(current);
        }
        Collections.reverse(path);

        if (!path.isEmpty() && path.get(0).equals(start)) {
            return path;
        } else {
            return Collections.emptyList();
        }
    }

    public List<Stop> getLastCalculatedPath() {
        return lastCalculatedPath;
    }


    public List<Stop> getAllStops() {
        return stops;
    }

    private List<Integer> routeList(Stop start, Stop end, Map<Stop, Stop> predecessors) {
        List<Integer> path = new ArrayList<>();
        for (Stop at = end; at != null; at = predecessors.get(at)) {
            path.add(at.getId());
        }
        Collections.reverse(path);
        if (!path.isEmpty() && path.getFirst().equals(start.getId())) {
            return path;
        } else {
            return Collections.emptyList();
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
        return switch (priority) {
            case DISTANCE -> currentDistance + route.getDistance();
            case TIME -> currentDistance + route.getTime();
            case COST -> currentDistance + route.getCost();
            case TRANSPORTS -> currentDistance + route.getTransports();
            default ->
                    currentDistance + route.getDistance() + route.getTime() + route.getTransports() + route.getCost();
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

    public void addStop(Stop stop1) {
        stops.add(stop1);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

}

