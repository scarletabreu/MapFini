package backend.Files;

import backend.Classes.Stop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StopJsonManager {
    private static final String FILE_PATH = "src/main/java/Files/stops.json";
    private final Gson gson;

    public StopJsonManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // Guardar un Stop en el archivo JSON
    public void saveStop(Stop stop) {
        List<Stop> stops = loadStops();
        if (stops == null) {
            stops = new ArrayList<>();
        }
        stops.add(stop);
        saveStopsToFile(stops);
    }

    // Cargar todos los Stops desde el archivo JSON
    public List<Stop> loadStops() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Stop>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<>(); // Si no existe el archivo, devuelve una lista vacía
        }
    }

    // Guardar una lista de Stops en el archivo JSON
    private void saveStopsToFile(List<Stop> stops) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(stops, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Buscar un Stop por ID
    public Stop findStopById(int id) {
        List<Stop> stops = loadStops();
        for (Stop stop : stops) {
            if (stop.getId() == id) {
                return stop;
            }
        }
        return null; // Si no se encuentra el Stop
    }

    // Actualizar un Stop existente
    public void updateStop(Stop updatedStop) {
        List<Stop> stops = loadStops();
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getId() == updatedStop.getId()) {
                stops.set(i, updatedStop);
                saveStopsToFile(stops);
                return;
            }
        }
        System.out.println("Stop no encontrado para actualizar.");
    }

    // Eliminar un Stop por ID
    public void deleteStopById(int id) {
        List<Stop> stops = loadStops();
        stops.removeIf(stop -> stop.getId() == id);
        saveStopsToFile(stops);
    }
}