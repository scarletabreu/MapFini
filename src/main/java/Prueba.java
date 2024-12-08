import backend.Classes.Route;
import backend.Classes.Stop;
import backend.Controller.WorldMap;
import backend.Enum.Traffic;
import backend.Files.WorldMapJsonManager;

import java.util.ArrayList;

public class Prueba {
    public static void main(String[] args) {
        WorldMapJsonManager manager = new WorldMapJsonManager();

        // Crear un WorldMap
        ArrayList<Stop> stops = new ArrayList<>();
        ArrayList<Route> routes = new ArrayList<>();
        stops.add(new Stop(0,0));
        stops.add(new Stop( 1,1));
        routes.add(new Route(1, 2, 100, 60, 50, 1, Traffic.HIGH));
        WorldMap worldMap = new WorldMap(stops, routes);

        // Guardar el WorldMap
        manager.saveWorldMap(worldMap);

        // Cargar y mostrar todos los WorldMaps
        ArrayList<WorldMap> worldMaps = manager.loadWorldMaps();
        System.out.println("WorldMaps guardados:");
        for (WorldMap wm : worldMaps) {
            System.out.println("ID: " + wm.getId() + ", Stops: " + wm.getStops().size() + ", Routes: " + wm.getStops().size());
        }

        // Buscar un WorldMap por ID
        WorldMap foundMap = manager.findWorldMapById(worldMap.getId());
        if (foundMap != null) {
            System.out.println("WorldMap encontrado: ID - " + foundMap.getId());
        }

        // Actualizar un WorldMap
        worldMap.createStop(new Stop( 1,1));
        manager.updateWorldMap(worldMap);

        // Eliminar un WorldMap por ID
        manager.deleteWorldMapById(worldMap.getId());
    }
}
