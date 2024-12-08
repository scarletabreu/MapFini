package Visual;

import backend.Classes.User;
import backend.Files.UserJsonManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class OpenMap extends Application {

    private final UserJsonManager userJsonManager;

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
            mapNames.addAll(user.getMaps());
        }

        // En el método start(), modifica la configuración de mapListView
        ListView<String> mapListView = new ListView<>(mapNames);
        mapListView.setStyle(
                "-fx-background-color: #302836;" +
                        "-fx-control-inner-background: white;" +
                        "-fx-background-radius: 20px;" + // Añade bordes redondeados
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;"
        );

        // Opcional: si quieres efectos de selección más suaves
        mapListView.setPlaceholder(new Label("No hay mapas disponibles"));

        Button editButton = getButton(primaryStage, mapListView);

        // Layout con márgenes y espaciado mejorado
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setStyle("-fx-background-color: #302836;");

        // Título
        Label tituloLabel = new Label("Mis Mapas");
        tituloLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        VBox.setVgrow(mapListView, Priority.ALWAYS);

        layout.getChildren().addAll(tituloLabel, mapListView, editButton);
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        Scene scene = new Scene(layout, screenWidth, screenHeight - 20);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Selección de Mapas");

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen(); // Centrar en la pantalla
        primaryStage.show();
    }


    private @NotNull Button getButton(Stage primaryStage, ListView<String> mapListView) {
        Button editButton = new Button("Editar Mapa");
        editButton.setStyle(
                "-fx-background-color: #AA7CFB; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #302836; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        );

        editButton.setOnMouseEntered(e -> editButton.setStyle(
                "-fx-background-color: #302836; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #AA7CFB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));

        editButton.setOnMouseExited(e -> editButton.setStyle(
                "-fx-background-color: #AA7CFB; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #302836; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));

        editButton.setOnAction(e -> {
            String selectedMap = mapListView.getSelectionModel().getSelectedItem();
            if (selectedMap != null) {
                openMapEditor(selectedMap);
            } else {
                mostrarAlertaSeleccion(primaryStage);
            }
        });
        return editButton;
    }

    private void mostrarAlertaSeleccion(Stage owner) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(owner);
        alert.setTitle("Selección Requerida");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, selecciona un mapa para editar.");
        alert.showAndWait();
    }

    private void openMapEditor(String mapName) {
        System.out.println("Abriendo editor para el mapa: " + mapName);
    }

    public static void main(String[] args) {
        launch(args);
    }
}