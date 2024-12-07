package backend.Files;

import backend.Classes.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserJsonManager {
    private static final String FILE_PATH = "src/main/java/Files/users.json";
    private final Gson gson;

    public UserJsonManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // Guardar un usuario en el archivo JSON
    public boolean saveUser(User user) {
        List<User> users = loadUsers();
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
        saveUsersToFile(users);
        return true;
    }

    // Cargar todos los usuarios desde el archivo JSON
    public List<User> loadUsers() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<User>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            // Si no existe el archivo o hay un error, devolvemos una lista vacía
            return new ArrayList<>();
        }
    }

    // Guardar una lista de usuarios en el archivo JSON
    private void saveUsersToFile(List<User> users) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Buscar un usuario por su ID
    public User findUserById(String id) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null; // Si no se encuentra el usuario
    }

    // Actualizar un usuario existente
    public void updateUser(User updatedUser) {
        List<User> users = loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                saveUsersToFile(users);
                return;
            }
        }
        System.out.println("Usuario no encontrado para actualizar.");
    }

    // Eliminar un usuario por ID
    public void deleteUserById(String id) {
        List<User> users = loadUsers();
        users.removeIf(user -> user.getId().equals(id));
        saveUsersToFile(users);
    }

    public boolean checkLogin(String janeSmith, String mypassword456) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(janeSmith) && user.getPassword().equals(mypassword456)) {
                return true;
            }
        }
        return false;
    }

    public String generateId() {
        List<User> users = loadUsers();
        int maxId = 0;
        for (User user : users) {
            int id = Integer.parseInt(user.getId());
            if (id > maxId) {
                maxId = id;
            }
        }
        return String.valueOf(maxId + 1);
    }
}
