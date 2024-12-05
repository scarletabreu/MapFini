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

    public static String generateId() {
        return FirebaseInitializer.getInstance().getFirestore().collection("User").document().getId();
    }

    public void saveUser(String userId, String email, String username, String password) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("name", username);
        userData.put("password", password);

        try {
            DocumentReference docRef = firestore.collection("User").document(userId);
            WriteResult result = docRef.set(userData).get();
            System.out.println("Datos guardados en Firebase: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error al guardar datos en Firebase: " + e.getMessage());
        }
    }
}
