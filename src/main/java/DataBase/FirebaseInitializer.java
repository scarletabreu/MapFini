package DataBase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FirebaseInitializer {
    private static FirebaseInitializer instance;
    private final Firestore firestore;

    private FirebaseInitializer() {
        try {
            InputStream serviceAccount = getClass().getResourceAsStream("/credentials.json");
            File file = new File("C:\\Users\\Scarlet\\Documents\\test.json");
            assert serviceAccount != null;
            Files.copy(serviceAccount, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Paths.get(serviceAccount.toString()); // Check if the file exists. This line will throw an exception if the file does not exist.

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            firestore = FirestoreClient.getFirestore();
            System.out.println("Firestore inicializado correctamente.");
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar Firebase: " + e.getMessage());
        }
    }

    public static synchronized FirebaseInitializer getInstance() {
        if (instance == null) {
            instance = new FirebaseInitializer();
        }
        return instance;
    }

    public Firestore getFirestore() {
        return firestore;
    }
}