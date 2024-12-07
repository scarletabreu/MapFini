
import backend.Classes.User;
import backend.Files.UserJsonManager;

public class Prueba {
    public static void main(String[] args) {
        UserJsonManager userJsonManager = new UserJsonManager();

        User user1 = new User("1", "john_doe", "password123", "john.doe@example.com");
        User user2 = new User("2", "jane_smith", "mypassword456", "jane.smith@example.com");

        System.out.println("Guardando usuario 1...");
        boolean result1 = userJsonManager.saveUser(user1);
        if (result1) {
            System.out.println("Usuario 1 guardado con éxito.");
        } else {
            System.out.println("Error al guardar el usuario 1.");
        }

        System.out.println("Guardando usuario 2...");
        boolean result2 = userJsonManager.saveUser(user2);
        if (result2) {
            System.out.println("Usuario 2 guardado con éxito.");
        } else {
            System.out.println("Error al guardar el usuario 2.");
        }

        System.out.println("\nCargando usuarios del archivo...");
        for (User user : userJsonManager.loadUsers()) {
            System.out.println("Usuario cargado: " + user.getUsername() + " - " + user.getEmail());
        }

        String newUserId = userJsonManager.generateId();
        System.out.println("\nNuevo ID generado para un nuevo usuario: " + newUserId);

        // Crear un nuevo usuario con el ID generado y guardarlo
        User user3 = new User(newUserId, "new_user", "password789", "new.user@example.com");
        System.out.println("Guardando nuevo usuario...");
        if (userJsonManager.saveUser(user3)) {
            System.out.println("Nuevo usuario guardado con éxito.");
        } else {
            System.out.println("Error al guardar el nuevo usuario.");
        }

        // Eliminar un usuario por ID
        System.out.println("\nEliminando usuario con ID 1...");
        userJsonManager.deleteUserById("1");

        // Verificar si el usuario fue eliminado
        System.out.println("\nCargando usuarios después de la eliminación...");
        for (User user : userJsonManager.loadUsers()) {
            System.out.println("Usuario cargado: " + user.getUsername() + " - " + user.getEmail());
        }

        // Probar login (esto puede ser para verificar la autenticación de un usuario)
        System.out.println("\nProbando login...");
        boolean loginSuccess = userJsonManager.checkLogin("jane_smith", "mypassword456");
        if (loginSuccess) {
            System.out.println("Login exitoso.");
        } else {
            System.out.println("Login fallido.");
        }
    }
}
