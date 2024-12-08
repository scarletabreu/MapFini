package backend.Files;

import backend.Controller.WorldMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldMapJsonManager {
    private static final String FILE_PATH = "src/main/java/Files/maps.json";
    private static final Logger LOGGER = Logger.getLogger(WorldMapJsonManager.class.getName());
    private final Gson gson;

    public WorldMapJsonManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // Guardar un WorldMap en el archivo JSON
    public boolean saveWorldMap(WorldMap worldMap) {
        if (worldMap == null) {
            LOGGER.log(Level.WARNING, "El WorldMap no puede ser nulo.");
            return false;
        }

        List<WorldMap> worldMaps = loadWorldMaps();
        worldMaps.add(worldMap);
        return saveWorldMapsToFile(worldMaps);
    }

    // Cargar todos los WorldMaps desde el archivo JSON
    public List<WorldMap> loadWorldMaps() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    LOGGER.log(Level.INFO, "Archivo de mapas creado.");
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "No se pudo crear el archivo de mapas.", e);
            }
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<WorldMap>>() {}.getType();
            List<WorldMap> worldMaps = gson.fromJson(reader, listType);
            return worldMaps != null ? worldMaps : new ArrayList<>();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo de mapas.", e);
            return new ArrayList<>();
        }
    }

    // Guardar una lista de WorldMaps en el archivo JSON
    private boolean saveWorldMapsToFile(List<WorldMap> worldMaps) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(worldMaps, writer);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar los mapas en el archivo.", e);
            return false;
        }
    }

    // Buscar un WorldMap por ID
    public Optional<WorldMap> findWorldMapById(String id) {
        return loadWorldMaps().stream()
                .filter(worldMap -> worldMap.getId().equals(id))
                .findFirst();
    }

    // Actualizar un WorldMap existente
    public boolean updateWorldMap(WorldMap updatedWorldMap) {
        List<WorldMap> worldMaps = loadWorldMaps();
        Optional<WorldMap> existingWorldMap = findWorldMapById(updatedWorldMap.getId());

        if (existingWorldMap.isPresent()) {
            worldMaps = new ArrayList<>(worldMaps); // Para evitar ConcurrentModificationException
            worldMaps.replaceAll(worldMap -> worldMap.getId().equals(updatedWorldMap.getId()) ? updatedWorldMap : worldMap);
            return saveWorldMapsToFile(worldMaps);
        } else {
            LOGGER.log(Level.WARNING, "WorldMap con ID {0} no encontrado para actualizar.", updatedWorldMap.getId());
            return false;
        }
    }

    // Eliminar un WorldMap por ID
    public boolean deleteWorldMapById(String id) {
        List<WorldMap> worldMaps = loadWorldMaps();
        boolean removed = worldMaps.removeIf(worldMap -> worldMap.getId().equals(id));
        if (removed) {
            return saveWorldMapsToFile(worldMaps);
        } else {
            LOGGER.log(Level.WARNING, "WorldMap con ID {0} no encontrado para eliminar.", id);
            return false;
        }
    }
}
