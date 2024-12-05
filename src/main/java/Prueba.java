import DataBase.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Prueba {
    public static void main(String[] args) {
        Firestore db = FirebaseInitializer.getInstance().getFirestore();
        try {
            // Crear un documento de prueba
            ApiFuture<WriteResult> writeFuture = db.collection("User")
                    .document("userId")
                    .set(Map.of("name", "Leo"));
            System.out.println("Documento creado en: " + writeFuture.get().getUpdateTime());

            // Leer el documento
            ApiFuture<DocumentSnapshot> readFuture = db.collection("User")
                    .document("userId")
                    .get();
            DocumentSnapshot doc = readFuture.get();
            if (doc.exists()) {
                System.out.println("Documento leído: " + doc.getData());
            }
        } catch (Exception e) {
            System.err.println("Error durante la operación de prueba en el main de prueba :0: " + e.getMessage());
           // e.printStackTrace();
        }
    }
}
