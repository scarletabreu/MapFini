package DataBase;

import backend.Classes.Route;
import com.google.cloud.firestore.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RouteDB {
    private final Firestore db;
    private static final String COLLECTION_NAME = "Route";

    public RouteDB() {
        this.db = FirebaseInitializer.getInstance().getFirestore();
    }

    public boolean create(Route route) {
        if (!isValidUser(route)) return false;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(route.getId())
                    .set(route)
                    .get();
            System.out.println("Ruta creado con ID: " + route.getId());
            System.out.println("Hora de actualización: " + result.getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "crear el usuario");
        }
        return false;
    }

    public void update(Route route) {
        if (!isValidUser(route)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(route.getId())
                    .set(route, SetOptions.merge())
                    .get();
            System.out.println("Ruta actualizado con ID: " + route.getId());
            System.out.println("Hora de actualización: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "actualizar el usuario");
        }
    }

    public void updateFields(String routeId, Map<String, Object> nuevosCampos) {
        if (!isValidId(routeId)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(routeId)
                    .update(nuevosCampos)
                    .get();
            System.out.println("Campos del route con ID " + routeId + " actualizados.");
            System.out.println("Hora de actualización: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "actualizar campos del usuario");
        }
    }

    public void delete(String routeId) {
        if (!isValidId(routeId)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(routeId)
                    .delete()
                    .get();
            System.out.println("Rpute eliminado con ID: " + routeId);
            System.out.println("Hora de eliminación: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "eliminar el usuario");
        }
    }

    public Route get(String routeId) {
        if (!isValidId(routeId)) return null;

        try {
            DocumentSnapshot document = db.collection(COLLECTION_NAME)
                    .document(routeId)
                    .get()
                    .get();
            if (document.exists()) {
                Route route = document.toObject(Route.class);
                System.out.println("Ruta obtenido con ID: " + routeId);
                return route;
            } else {
                System.out.println("No se encontró el usuario con ID: " + routeId);
            }
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "obtener el ruta");
        }
        return null;
    }

    public List<Route> getAll() {
        List<Route> routes = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = db.collection(COLLECTION_NAME).get().get();
            querySnapshot.getDocuments().forEach(document -> {
                Route worldMap1 = document.toObject(Route.class);
                routes.add(worldMap1);
            });
            System.out.println("Usuarios obtenidos correctamente.");
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "obtener todos los usuarios");
        }
        return routes;
    }

    private boolean isValidId(String id) {
        if (id == null || id.isEmpty()) {
            System.err.println("El ID del usuario no puede estar vacío.");
            return false;
        }
        return true;
    }

    private boolean isValidUser(Route route) {
        if (route == null || !isValidId(route.getId())) {
            System.err.println("El route o su ID no pueden estar vacíos.");
            return false;
        }
        return true;
    }

    private void handleException(Exception e, String action) {
        if (e instanceof InterruptedException) {
            System.err.println("La operación fue interrumpida al intentar " + action + ": " + e.getMessage());
            Thread.currentThread().interrupt();
        } else if (e instanceof ExecutionException) {
            System.err.println("Error al intentar " + action + ": " + e.getCause());
        }
    }
}
