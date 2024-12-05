package DataBase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.InputStream;

public class FirebaseInitializer {
    private static FirebaseInitializer instance;
    private final Firestore firestore;

    private FirebaseInitializer() {
        try {
            // Cargar el archivo credentials.json desde el classpath
            InputStream serviceAccount = getClass().getResourceAsStream("/environment/credentials.json");
            if (serviceAccount == null) {
                throw new RuntimeException("Archivo credentials.json no encontrado en el classpath.");
            }

            // Configurar Firebase con las credenciales
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Inicializar FirebaseApp si aún no está inicializado
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            // Obtener instancia de Firestore
            firestore = FirestoreClient.getFirestore();
            System.out.println("Firestore inicializado correctamente.");
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar Firebase: " + e.getMessage(), e);
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
