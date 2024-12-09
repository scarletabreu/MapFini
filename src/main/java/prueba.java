import backend.Classes.User;
import backend.Files.UserJsonManager;

import java.util.List;
import java.util.Optional;

public class prueba {
    public static void main(String[] args) {
        UserJsonManager userJsonManager = new UserJsonManager();

        // Crear un nuevo usuario con mapas
        User newUser = new User();
        newUser.setId("2");
        newUser.setUsername("testUser");
        newUser.setPassword("testPass");
        newUser.addMap("map1");
        newUser.addMap("map2");

        // Guardar el usuario
        boolean isSaved = userJsonManager.saveUser(newUser);
        System.out.println("Usuario guardado: " + isSaved);

        // Cargar usuarios y mostrar información
        List<User> users = userJsonManager.loadUsers();
        for (User user : users) {
            System.out.println("Usuario ID: " + user.getId());
            System.out.println("Nombre de usuario: " + user.getUsername());
            System.out.println("Mapas asociados: " + user.getMaps());
        }

        // Buscar usuario por ID y mostrar sus mapas
        Optional<User> foundUser = userJsonManager.findUserById("2");
        foundUser.ifPresent(user -> {
            System.out.println("Usuario encontrado: " + user.getUsername());
            System.out.println("Mapas asociados: " + user.getMaps());
        });
    }
}

