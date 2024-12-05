package DataBase;

import backend.Classes.Stop;
import backend.Classes.User;
import com.google.cloud.firestore.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class StopDB {
    private final Firestore db;
    private static final String COLLECTION_NAME = "Stop";

    public StopDB() {
        this.db = FirebaseInitializer.getInstance().getFirestore();
    }

    /**
     * Crea un usuario en Firestore.
     *
     * @param stop Objeto usuario que se desea crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    public boolean create(Stop stop) {
        if (!isValidUser(stop)) return false;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(String.valueOf(stop.getId()))
                    .set(stop)
                    .get();
            System.out.println("Usuario creado con ID: " + stop.getId());
            System.out.println("Hora de actualización: " + result.getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "crear el usuario");
        }
        return false;
    }

    /**
     * Actualiza un usuario completo en Firestore.
     *
     * @param stop Objeto usuario que se desea actualizar.
     */
    public void update(Stop stop) {
        if (!isValidUser(stop)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(String.valueOf(stop.getId()))
                    .set(stop, SetOptions.merge())
                    .get();
            System.out.println("Usuario actualizado con ID: " + stop.getId());
            System.out.println("Hora de actualización: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "actualizar el usuario");
        }
    }

    /**
     * Actualiza campos específicos de un usuario en Firestore.
     *
     * @param stopId      ID del usuario a actualizar.
     * @param nuevosCampos Mapa con los campos a actualizar.
     */
    public void updateFields(String stopId, Map<String, Object> nuevosCampos) {
        if (!isValidId(stopId)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(stopId)
                    .update(nuevosCampos)
                    .get();
            System.out.println("Campos del stop con ID " + stopId + " actualizados.");
            System.out.println("Hora de actualización: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "actualizar campos del usuario");
        }
    }

    /**
     * Elimina un usuario de Firestore.
     *
     * @param stopId ID del usuario a eliminar.
     */
    public void delete(String stopId) {
        if (!isValidId(stopId)) return;

        try {
            WriteResult result = db.collection(COLLECTION_NAME)
                    .document(stopId)
                    .delete()
                    .get();
            System.out.println("Stop eliminado con ID: " + stopId);
            System.out.println("Hora de eliminación: " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "eliminar el usuario");
        }
    }

    /**
     * Obtiene un usuario por ID desde Firestore.
     *
     * @param stopId ID del usuario.
     * @return Objeto User si se encuentra, null en caso contrario.
     */
    public User get(String stopId) {
        if (!isValidId(stopId)) return null;

        try {
            DocumentSnapshot document = db.collection(COLLECTION_NAME)
                    .document(stopId)
                    .get()
                    .get();
            if (document.exists()) {
                User user = document.toObject(User.class);
                System.out.println("Stop obtenido con ID: " + stopId);
                return user;
            } else {
                System.out.println("No se encontró el usuario con ID: " + stopId);
            }
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "obtener el usuario");
        }
        return null;
    }

    /**
     * Obtiene todos los usuarios de Firestore.
     *
     * @return Lista de usuarios.
     */
    public List<Stop> getAll() {
        List<Stop> stops = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = db.collection(COLLECTION_NAME).get().get();
            querySnapshot.getDocuments().forEach(document -> {
                Stop stop = document.toObject(Stop.class);
                stops.add(stop);
            });
            System.out.println("Paradas obtenidos correctamente.");
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "obtener todos los usuarios");
        }
        return stops;
    }

    /**
     * Valida si el ID es válido.
     *
     * @param id ID del usuario.
     * @return true si es válido, false en caso contrario.
     */
    private boolean isValidId(String id) {
        if (id == null || id.isEmpty()) {
            System.err.println("El ID de la parada no puede estar vacío.");
            return false;
        }
        return true;
    }

    /**
     * Valida si un usuario es válido.
     *
     * @param stop Objeto User.
     * @return true si es válido, false en caso contrario.
     */
    private boolean isValidUser(Stop stop) {
        if (stop == null || !isValidId(String.valueOf(stop.getId()))) {
            System.err.println("El usuario o su ID no pueden estar vacíos.");
            return false;
        }
        return true;
    }

    public boolean checkLogin(String username, String password) {
        try {
            QuerySnapshot querySnapshot = db.collection(COLLECTION_NAME)
                    .whereEqualTo("username", username)
                    .get()
                    .get();

            if (!querySnapshot.isEmpty()) {
                // Obtiene el primer documento que coincida con el username
                DocumentSnapshot document = querySnapshot.getDocuments().get(0); // usa get(0) en vez de getFirst()

                User user = document.toObject(User.class);

                // Si las contraseñas están cifradas, usa un método de comparación de hash como BCrypt
                return user.getPassword().equals(password); // Si las contraseñas no están cifradas
            }
        } catch (InterruptedException | ExecutionException e) {
            handleException(e, "verificar login");
        }
        return false;
    }


    /**
     * Maneja excepciones comunes.
     *
     * @param e     Excepción ocurrida.
     * @param action Acción realizada.
     */
    private void handleException(Exception e, String action) {
        if (e instanceof InterruptedException) {
            System.err.println("La operación fue interrumpida al intentar " + action + ": " + e.getMessage());
            Thread.currentThread().interrupt();
        } else if (e instanceof ExecutionException) {
            System.err.println("Error al intentar " + action + ": " + e.getCause());
        }
    }
}
