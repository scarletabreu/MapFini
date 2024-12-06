package backend.Files;

import backend.Controller.WorldMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class WorldMapJsonManager {
    private static final String FILE_PATH = "src/main/java/Files/maps.json";
    private final Gson gson;

    public WorldMapJsonManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // Guardar un WorldMap en el archivo JSON
    public void saveWorldMap(WorldMap worldMap) {
        ArrayList<WorldMap> worldMaps = loadWorldMaps();
        if (worldMaps == null) {
            worldMaps = new ArrayList<>();
        }
        worldMaps.add(worldMap);
        saveWorldMapsToFile(worldMaps);
    }

    // Cargar todos los WorldMaps desde el archivo JSON
    public ArrayList<WorldMap> loadWorldMaps() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<WorldMap>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<>(); // Si no existe el archivo, devuelve una lista vacía
        }
    }

    // Guardar una lista de WorldMaps en el archivo JSON
    private void saveWorldMapsToFile(ArrayList<WorldMap> worldMaps) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(worldMaps, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Buscar un WorldMap por ID
    public WorldMap findWorldMapById(String id) {
        ArrayList<WorldMap> worldMaps = loadWorldMaps();
        for (WorldMap worldMap : worldMaps) {
            if (worldMap.getId().equals(id)) {
                return worldMap;
            }
        }
        return null; // Si no se encuentra el WorldMap
    }

    // Actualizar un WorldMap existente
    public void updateWorldMap(WorldMap updatedWorldMap) {
        ArrayList<WorldMap> worldMaps = loadWorldMaps();
        for (int i = 0; i < worldMaps.size(); i++) {
            if (worldMaps.get(i).getId().equals(updatedWorldMap.getId())) {
                worldMaps.set(i, updatedWorldMap);
                saveWorldMapsToFile(worldMaps);
                return;
            }
        }
        System.out.println("WorldMap no encontrado para actualizar.");
    }

    // Eliminar un WorldMap por ID
    public void deleteWorldMapById(String id) {
        ArrayList<WorldMap> worldMaps = loadWorldMaps();
        worldMaps.removeIf(worldMap -> worldMap.getId().equals(id));
        saveWorldMapsToFile(worldMaps);
    }
}
