package backend.Files;

import backend.Classes.Route;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RouteJsonManager {
    private static final String FILE_PATH = "src/main/java/Files/routes.json";
    private final Gson gson;

    public RouteJsonManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // Guardar una Route en el archivo JSON
    public void saveRoute(Route route) {
        List<Route> routes = loadRoutes();
        if (routes == null) {
            routes = new ArrayList<>();
        }
        routes.add(route);
        saveRoutesToFile(routes);
    }

    // Cargar todas las Routes desde el archivo JSON
    public List<Route> loadRoutes() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Route>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<>(); // Si no existe el archivo, devuelve una lista vacía
        }
    }

    // Guardar una lista de Routes en el archivo JSON
    private void saveRoutesToFile(List<Route> routes) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(routes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Buscar una Route por ID
    public Route findRouteById(String id) {
        List<Route> routes = loadRoutes();
        for (Route route : routes) {
            if (route.getId().equals(id)) {
                return route;
            }
        }
        return null; // Si no se encuentra la Route
    }

    // Actualizar una Route existente
    public void updateRoute(Route updatedRoute) {
        List<Route> routes = loadRoutes();
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).getId().equals(updatedRoute.getId())) {
                routes.set(i, updatedRoute);
                saveRoutesToFile(routes);
                return;
            }
        }
        System.out.println("Route no encontrada para actualizar.");
    }

    // Eliminar una Route por ID
    public void deleteRouteById(String id) {
        List<Route> routes = loadRoutes();
        routes.removeIf(route -> route.getId().equals(id));
        saveRoutesToFile(routes);
    }
}