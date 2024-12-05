import DataBase.FirebaseInitializer;
import DataBase.UserDB;
import backend.Classes.User;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.*;
import java.util.Map;

public class Prueba {
    public static void main(String[] args) {
        // Inicializar Firestore (asegurarte de haber configurado correctamente Firebase)
        Firestore db = FirebaseInitializer.getInstance().getFirestore();

        // Crear un objeto User
        User user = new User("user123", "johndoe", "password123", "johndoe@example.com");

        // Crear el objeto UserDB para interactuar con la base de datos
        UserDB userDB = new UserDB();

        // Crear un nuevo usuario en Firestore
        boolean userCreated = userDB.create(user);
        if (userCreated) {
            System.out.println("Usuario creado correctamente en Firestore.");
        } else {
            System.err.println("Hubo un problema al crear el usuario.");
        }

        // Recuperar el usuario por ID
        User retrievedUser = userDB.get(user.getId());
        if (retrievedUser != null) {
            System.out.println("Usuario recuperado: " + retrievedUser.getUsername());
            System.out.println("Correo electrónico: " + retrievedUser.getEmail());
        } else {
            System.err.println("No se pudo recuperar el usuario.");
        }

        // Actualizar un campo del usuario (por ejemplo, el email)
        user.setEmail("newemail@example.com");
        userDB.update(user);

        // Verificar la actualización
        User updatedUser = userDB.get(user.getId());
        if (updatedUser != null) {
            System.out.println("Usuario actualizado: " + updatedUser.getUsername());
            System.out.println("Nuevo correo electrónico: " + updatedUser.getEmail());
        }

        // Eliminar el usuario
        //userDB.delete(user.getId());
    }
}
