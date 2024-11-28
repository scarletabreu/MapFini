package backend.Controller;

import backend.Classes.*;
import backend.Enum.Priority;

import java.util.*;

public class WorldMap {
    private ArrayList<Stop> stops;
    private ArrayList<Route> routes;
    private static WorldMap instance;
    private List<Stop> lastCalculatedPath;

    public WorldMap(ArrayList<Stop> stops, ArrayList<Route> routes) {
        this.stops = stops;
        this.routes = routes;
    }

    public void createStop(Stop stop) {
        stops.add(stop);
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void createRoute(Route route) {
        routes.add(route);
    }

    public static WorldMap getInstance() {
        if (instance == null) {
            instance = new WorldMap(new ArrayList<>(), new ArrayList<>());
        }
        return instance;
    }

    public void dijkstra(Stop start, Stop end, Priority priority) {
        // Inicializar distancias y predecesores
        Map<Stop, Integer> distances = new HashMap<>();
        Map<Stop, Stop> predecessors = new HashMap<>();
        Set<Stop> visited = new HashSet<>();

        for (Stop stop : stops) {
            distances.put(stop, Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        PriorityQueue<Stop> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(start);

        while (!queue.isEmpty()) {
            Stop current = queue.poll();

            // Si llegamos al destino, dejamos de buscar
            if (current.equals(end)) {
                break;
            }

            if (visited.contains(current)) {
                continue; // Evitar procesamiento de nodos visitados
            }
            visited.add(current);

            for (Stop neighbor : current.getAdjacencyList()) {
                Route route = current.getRoute(neighbor);
                if (route == null || route.getTime() == -1) {
                    System.out.println("No es posible llegar de " + current.getId() + " a " + neighbor.getId() + " debido a un accidente.");
                    continue;
                }

                int currentDist = distances.get(current);
                int newDist = calculatePriority(currentDist, route, priority);

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current); // Guardar el predecesor
                    queue.add(neighbor); // Añadir vecino a la cola para futuras evaluaciones
                }
            }
        }

        // Verificar si hay una ruta desde el inicio hasta el destino
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
    }


    public void BellmanFord(Stop start, Priority priority) {
        HashMap<Stop, Integer> distances = new HashMap<>();
        HashMap<Stop, List<Stop>> paths = new HashMap<>();
        List<Stop> allStops = getAllStops();  // Evitar múltiples llamadas

        // Inicializar distancias y rutas
        for (Stop stop : allStops) {
            distances.put(stop, Integer.MAX_VALUE);
            paths.put(stop, new ArrayList<>());
        }
        distances.put(start, 0);
        paths.get(start).add(start);

        // Relajación de las aristas
        for (int i = 1; i < allStops.size(); i++) {
            boolean updated = false;  // Para detectar si hubo cambios
            for (Stop stop : allStops) {
                int currentDistance = distances.get(stop);
                for (Stop neighbor : stop.getAdjacencyList()) {
                    int newDist = calculatePriority(currentDistance, stop.getRoute(neighbor), priority);
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        paths.get(neighbor).clear();  // Limpiar la ruta anterior
                        paths.get(neighbor).addAll(paths.get(stop));  // Agregar la ruta más corta
                        paths.get(neighbor).add(neighbor);  // Agregar el vecino
                        updated = true;  // Se realizó una actualización
                    }
                }
            }
            if (!updated) {
                break;  // Si no hubo actualizaciones, salir del bucle
            }
        }

        // Imprimir resultados
        for (Stop stop : allStops) {
            System.out.println("Distancia más corta de " + start.getId() + " a " + stop.getId() + ": " + distances.get(stop));
            System.out.println("Ruta: " + paths.get(stop).stream().map(Stop::getId).toList());
        }
    }


    public void FloydWarshall(Stop start, Stop end, Priority priority) {
        int n = stops.size();
        double [][] dist = new double[n][n];
        int[][] next = new int[n][n];
        final int NO_PATH = Integer.MAX_VALUE;

        // Inicializar matrices de distancia y siguiente
        for (int i = 0; i < n; i++) {
            Stop stopI = stops.get(i);
            for (int j = 0; j < n; j++) {
                Stop stopJ = stops.get(j);
                if (i == j) {
                    dist[i][j] = 0;
                    next[i][j] = i;
                } else {
                    double weight = calculatePriority(0, stopI.getRoute(stopJ), priority);
                    if (weight != -1) {
                        dist[i][j] = weight;
                        next[i][j] = j;
                    } else {
                        dist[i][j] = NO_PATH;
                        next[i][j] = -1;
                    }
                }
            }
        }

        // Algoritmo de Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] < NO_PATH) { // Solo evaluar si hay un camino a través de k
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
            // Imprimir resultados de distancias más cortas y caminos
            System.out.println("Distancias más cortas entre todas las paradas:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] == NO_PATH) {
                        System.out.printf("No hay camino de %d a %d%n", stops.get(i).getId(), stops.get(j).getId());
                    } else {
                        System.out.printf("Distancia más corta de %d a %d: %d%n", stops.get(i).getId(), stops.get(j).getId(), dist[i][j]);
                        System.out.print("Ruta: ");
                        printPath(i, j, next);
                        System.out.println();
                    }
                }
            }
        }
        else{
            int startIndex = stops.indexOf(start);
            int endIndex = stops.indexOf(end);

            // If no path exists between start and end, return
            if (dist[startIndex][endIndex] == NO_PATH) {
                System.out.println("No path exists from " + start.getId() + " to " + end.getId());
                return;
            }

            // Print the shortest distance
            System.out.println("The shortest distance from stop " + start.getId() + " to stop " + end.getId() + " is: " + dist[startIndex][endIndex]);

            // Print the path
            System.out.print("Path: ");
            printPath(startIndex, endIndex, next);
            System.out.println();
        }
    }



    // Método auxiliar para imprimir la ruta de i a j
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
        }
    }

    public void Prim() {
        Set<Route> mstEdges = new HashSet<>();
        Set<Stop> mstVertices = new HashSet<>();
        PriorityQueue<Route> edgeQueue = new PriorityQueue<>(Comparator.comparingInt(Route::getCost));

        // Seleccionar el primer vértice y agregar sus aristas a la cola de prioridad
        Stop start = stops.get(0);
        mstVertices.add(start);
        edgeQueue.addAll(start.getRoutes());

        while (mstVertices.size() < stops.size() && !edgeQueue.isEmpty()) {
            Route minEdge = edgeQueue.poll();
            Stop nextVertex = minEdge.getEnd();

            // Si el destino de la arista ya está en el árbol, omitir
            if (mstVertices.contains(nextVertex)) {
                continue;
            }

            // Agregar arista y vértice al árbol de expansión mínima
            mstVertices.add(nextVertex);
            mstEdges.add(minEdge);

            // Agregar nuevas aristas a la cola de prioridad
            for (Route edge : nextVertex.getRoutes()) {
                if (!mstVertices.contains(edge.getEnd())) {
                    edgeQueue.add(edge);
                }
            }

            // Impresión de la arista añadida
            System.out.println("Agregando arista: " + minEdge.getStart().getId() + " -> " + minEdge.getEnd().getId() + " Costo: " + minEdge.getCost());
        }

        // Imprimir el árbol de expansión mínima
        System.out.println("Árbol de expansión mínima:");
        for (Route edge : mstEdges) {
            System.out.println(edge.getStart().getId() + " -> " + edge.getEnd().getId() + " Costo: " + edge.getCost());
        }
    }


    public List<Route> Kruskal() {
        List<Route> result = new ArrayList<>(); // Para almacenar el resultado
        List<Route> edges = new ArrayList<>(); // Para almacenar todas las rutas (aristas)

        // Reunir todas las aristas
        for (Stop stop : stops) {
            for (Route route : stop.getRoutes()) {
                edges.add(route);
            }
        }

        // Ordenar las aristas por distancia (o costo)
        Collections.sort(edges, Comparator.comparingInt(Route::getDistance));

        UnionFind uf = new UnionFind(); // Crear un objeto UnionFind

        for (Route edge : edges) {
            int startId = edge.getStart().getId();
            int endId = edge.getEnd().getId();

            // Si no están conectados, añadir la ruta al resultado y unir los conjuntos
            if (!uf.connected(startId, endId)) {
                uf.union(startId, endId);
                result.add(edge);
            }
        }

        return result; // Devuelve las rutas del árbol de expansión mínima
    }

    private List<Stop> reconstructPath(Map<Stop, Stop> predecessors, Stop start, Stop end) {
        List<Stop> path = new ArrayList<>();
        Stop current = end;

        while (current != null) {
            path.add(current);
            current = predecessors.get(current);
        }

        Collections.reverse(path);

        // Verify that the path starts with our start node
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
        // Cambiar la verificación a:
        if (!path.isEmpty() && path.getFirst().equals(start.getId())) {
            return path; // Retornar la ruta si es válida
        } else {
            return Collections.emptyList(); // Retornar lista vacía si no hay ruta
        }
    }


    private int calculatePriority(int currentDistance, Route route, Priority priority) {
        int result = 0;
        if(route == null){
            return -1;
        }
        if(priority == null){
            return currentDistance + route.getTransports() + (int) route.getCost() + route.getTime() + route.getDistance();
        }
        switch (priority) {
            case DISTANCE:
                result = currentDistance + route.getDistance();
                break;
            case TIME:
                result = currentDistance + route.getTime();
                break;
            case COST:
                result = currentDistance + (int) route.getCost();
                break;
            case TRANSPORTS:
                result = currentDistance + route.getTransports();
                break;
            default:
                result = currentDistance + route.getTransports() + (int) route.getCost() + route.getTime() + route.getDistance();
        }
        return result;
    }

    public Stop findStopById(int id){
        for(Stop stop : stops){
            if(stop.getId() == id){
                return stop;
            }
        }
        return null;
    }

    public void addStop(Stop stop1) {
        stops.add(stop1);
    }
    

}