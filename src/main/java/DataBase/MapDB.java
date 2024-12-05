package DataBase;

import backend.Classes.User;
import backend.Controller.WorldMap;
import com.google.cloud.firestore.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MapDB {
    private final Firestore db;
    private static final String COLLECTION_NAME = "Map";

    public MapDB() {
        this.db = FirebaseInitializer.getInstance().getFirestore();
    }

    public boolean create(WorldMap worldMap) {
        if (!isValidUser(worldMap)) return false;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(worldMap.getId())
                    .set(worldMap)
                    .get();
            System.out.println("worldMap creado con ID: " + worldMap.getId());
            System.out.println("Hora de actualización: " + result.getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "crear el usuario");
        }
        return false;
    }

    public void update(WorldMap worldMap) {
        if (!isValidUser(worldMap)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(worldMap.getId())
                    .set(worldMap, SetOptions.merge())
                    .get();
            System.out.println("Worldmap actualizado con ID: " + worldMap.getId());
            System.out.println("Hora de actualización: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "actualizar el usuario");
        }
    }

    public void updateFields(String mapId, Map<String, Object> nuevosCampos) {
        if (!isValidId(mapId)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(mapId)
                    .update(nuevosCampos)
                    .get();
            System.out.println("Campos del worldmap con ID " + mapId + " actualizados.");
            System.out.println("Hora de actualización: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "actualizar campos del usuario");
        }
    }

    public void delete(String mapId) {
        if (!isValidId(mapId)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(mapId)
                    .delete()
                    .get();
            System.out.println("Worldmap eliminado con ID: " + mapId);
            System.out.println("Hora de eliminación: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "eliminar el usuario");
        }
    }

    public WorldMap get(String mapId) {
        if (!isValidId(mapId)) return null;

        try {
            DocumentSnapshot document = db.collection(COLLECTION_NAME)
                    .document(mapId)
                    .get()
                    .get();
            if (document.exists()) {
                WorldMap worldMap = document.toObject(WorldMap.class);
                System.out.println("Usuario obtenido con ID: " + mapId);
                return worldMap;
            } else {
                System.out.println("No se encontró el usuario con ID: " + mapId);
            }
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "obtener el usuario");
        }
        return null;
    }

    public List<WorldMap> getAll() {
        List<WorldMap> worldMap = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = db.collection(COLLECTION_NAME).get().get();
            querySnapshot.getDocuments().forEach(document -> {
                WorldMap worldMap1 = document.toObject(WorldMap.class);
                worldMap.add(worldMap1);
            });
            System.out.println("Usuarios obtenidos correctamente.");
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "obtener todos los usuarios");
        }
        return worldMap;
    }

    private boolean isValidId(String id) {
        if (id == null || id.isEmpty()) {
            System.err.println("El ID del usuario no puede estar vacío.");
            return false;
        }
        return true;
    }

    private boolean isValidUser(WorldMap worldMap) {
        if (worldMap == null || !isValidId(worldMap.getId())) {
            System.err.println("El usuario o su ID no pueden estar vacíos.");
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
