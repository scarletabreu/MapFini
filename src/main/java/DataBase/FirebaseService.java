package DataBase;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseService {
    private final Firestore firestore;

    public FirebaseService() {
        this.firestore = FirebaseInitializer.getInstance().getFirestore();
    }

    // Método para guardar datos de usuario
    public void saveUser(String userId, String email, String username) {
        // Crea un mapa con los datos del usuario
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", username);

        try {
            // Guarda el documento en la colección "users"
            DocumentReference docRef = firestore.collection("users").document(userId);
            WriteResult result = docRef.set(userData).get();
            System.out.println("Datos guardados en Firebase: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error al guardar datos en Firebase: " + e.getMessage());
        }
    }
}
