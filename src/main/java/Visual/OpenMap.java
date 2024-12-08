package Visual;

import backend.Classes.User;
import backend.Files.UserJsonManager; // Asegúrate de tener acceso al manager de usuarios
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class OpenMap extends Application {

    private UserJsonManager userJsonManager;

    public OpenMap() {
        userJsonManager = new UserJsonManager();
    }

    public static void showDashboard(Stage primaryStage) {
        OpenMap openMap = new OpenMap();
        openMap.start(primaryStage);
    }

    @Override
    public void start(Stage primaryStage) {
        // Cargar los mapas de usuario
        List<User> users = userJsonManager.loadUsers();
        ObservableList<String> mapNames = FXCollections.observableArrayList();

        for (User user : users) {
            // Suponiendo que User tiene una lista de mapas, por ejemplo `user.getMaps()`
            mapNames.addAll(user.getMaps());
        }

        // Crear un ListView para mostrar los mapas
        ListView<String> mapListView = new ListView<>(mapNames);

        // Crear el botón para editar el mapa seleccionado
        Button editButton = new Button("Editar mapa");

        editButton.setOnAction(e -> {
            String selectedMap = mapListView.getSelectionModel().getSelectedItem();
            if (selectedMap != null) {
                // Aquí puedes abrir una nueva ventana de edición para el mapa
                openMapEditor(selectedMap);
            } else {
                System.out.println("Selecciona un mapa para editar.");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(mapListView, editButton);

        Scene scene = new Scene(layout, 300, 400);
        primaryStage.setTitle("Seleccionar Mapa");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openMapEditor(String mapName) {
        // Lógica para abrir el editor de mapas. Puede ser otro stage con un formulario o algo similar.
        System.out.println("Abriendo editor para el mapa: " + mapName);
        // Aquí puedes agregar la lógica para la edición del mapa seleccionado.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
