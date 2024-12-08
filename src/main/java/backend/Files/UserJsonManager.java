package backend.Files;

import Visual.Classes.Point2DAdapter;
import backend.Classes.User;
import backend.Controller.WorldMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Point2D;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UserJsonManager {
    private static final String FILE_PATH = "src/main/java/Files/users.json";
    private static final String MAPFILE_PATH = "src/main/java/Files/maps.json";
    private static final Logger LOGGER = Logger.getLogger(UserJsonManager.class.getName());
    private final Gson gson;

    public UserJsonManager() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Point2D.class, new Point2DAdapter())
                .setPrettyPrinting()
                .create();
    }

    // Cargar los usuarios una sola vez
    private List<User> loadUsersOnce() {
        return loadUsers();
    }

    /**
     * Guarda un usuario en el archivo JSON.
     */
    public boolean saveUser(User user) {
        if (user == null) {
            LOGGER.log(Level.WARNING, "El usuario no puede ser nulo.");
            return false;
        }

        List<User> users = loadUsersOnce();
        if (users.stream().anyMatch(existingUser -> existingUser.getId().equals(user.getId()))) {
            LOGGER.log(Level.WARNING, "El usuario con ID {0} ya existe.", user.getId());
            return false;
        }

        users.add(user);
        return saveUsersToFile(users);
    }

    /**
     * Carga todos los usuarios desde el archivo JSON.
     */
    public List<User> loadUsers() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, listType);
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "No se pudo leer el archivo o no existe. Retornando lista vacía.", e);
            return new ArrayList<>();
        }
    }

    /**
     * Guarda una lista de usuarios en el archivo JSON.
     */
    private boolean saveUsersToFile(List<User> users) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar los usuarios en el archivo.", e);
            return false;
        }
    }

    /**
     * Busca un usuario por su ID.
     */
    public Optional<User> findUserById(String id) {
        return loadUsersOnce().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    /**
     * Actualiza un usuario existente.
     */
    public boolean updateUser(User updatedUser) {
        List<User> users = loadUsersOnce();
        Optional<User> existingUser = findUserById(updatedUser.getId());

        if (existingUser.isPresent()) {
            users = users.stream()
                    .map(user -> user.getId().equals(updatedUser.getId()) ? updatedUser : user)
                    .collect(Collectors.toList());
            return saveUsersToFile(users);
        } else {
            LOGGER.log(Level.WARNING, "Usuario con ID {0} no encontrado para actualizar.", updatedUser.getId());
            return false;
        }
    }

    /**
     * Elimina un usuario por su ID.
     */
    public boolean deleteUserById(String id) {
        List<User> users = loadUsersOnce();
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            return saveUsersToFile(users);
        } else {
            LOGGER.log(Level.WARNING, "Usuario con ID {0} no encontrado para eliminar.", id);
            return false;
        }
    }

    /**
     * Verifica las credenciales de inicio de sesión.
     */
    public boolean checkLogin(String username, String password) {
        return loadUsersOnce().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    /**
     * Genera un nuevo ID único basado en los existentes.
     */
    public String generateId() {
        List<User> users = loadUsersOnce();
        OptionalInt maxIdOpt = users.stream()
                .map(User::getId)
                .filter(id -> id.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max();

        return maxIdOpt.isPresent() ? String.valueOf(maxIdOpt.getAsInt() + 1) : "1";
    }

    /**
     * Guarda un mapa para el usuario actual.
     */
    public void saveMap(WorldMap worldMap) {
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            LOGGER.log(Level.WARNING, "No hay usuario actualmente.");
            return;
        }

        boolean isMapSaved = saveWorldMapToFile(worldMap);

        if (isMapSaved) {
            currentUser.addMap(worldMap.getId());

            boolean isUserUpdated = updateUser(currentUser);

            if (isUserUpdated) {
                LOGGER.log(Level.INFO, "Mapa guardado correctamente con ID: " + worldMap.getId());
            } else {
                LOGGER.log(Level.SEVERE, "Error al actualizar el usuario con el nuevo mapa.");
            }
        } else {
            LOGGER.log(Level.SEVERE, "Error al guardar el mapa.");
        }
    }

    /**
     * Guarda un WorldMap en el archivo correspondiente.
     */
    private boolean saveWorldMapToFile(WorldMap worldMap) {
        WorldMapJsonManager worldMapJsonManager = new WorldMapJsonManager();
        return worldMapJsonManager.saveWorldMap(worldMap);
    }

    /**
     * Obtiene el usuario actual. Aquí deberías implementar la lógica de obtención del usuario activo.
     */
    private User getCurrentUser() {
        // Implementar lógica real de obtención del usuario actual
        return loadUsersOnce().stream().findFirst().orElse(null);
    }
}
