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
        List<User> users = userJsonManager.loadUsers();
        ObservableList<String> mapNames = FXCollections.observableArrayList();

        for (User user : users) {
            mapNames.addAll(user.getMaps());
        }

        ListView<String> mapListView = new ListView<>(mapNames);
        mapListView.setStyle(
                "-fx-background-color: #302836;" +
                        "-fx-control-inner-background: white;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;"
        );

        mapListView.setPlaceholder(new Label("No hay mapas disponibles"));

        Button editButton = getButton(primaryStage, mapListView);
        Button closeButton = closeButton(primaryStage);

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

        layout.getChildren().addAll(tituloLabel, mapListView, editButton, closeButton);
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        Scene scene = new Scene(layout, screenWidth, screenHeight - 20);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.setTitle("Selección de Mapas");

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private Button closeButton(Stage primaryStage) {
        Button button = new Button("Cerrar");
        button.setStyle(
                "-fx-background-color: #AA7CFB; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #302836; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        );

        button.setOnMouseEntered(_ -> button.setStyle(
                "-fx-background-color: #302836; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #AA7CFB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));

        button.setOnMouseExited(_ -> button.setStyle(
                "-fx-background-color: #AA7CFB; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #302836; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));

        button.setOnAction(_ -> {
            MainDashboard.showDashboard(primaryStage);

            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
        });
        return button;
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

        editButton.setOnMouseEntered(_ -> editButton.setStyle(
                "-fx-background-color: #302836; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #AA7CFB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));

        editButton.setOnMouseExited(_ -> editButton.setStyle(
                "-fx-background-color: #AA7CFB; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #302836; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));

        editButton.setOnAction(_ -> {
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

    private void openMapEditor(String idMap) {
        System.out.println("Abriendo editor para el mapa: " + idMap);
        MapDashboard.showEditMap(idMap);
    }

    public static void main(String[] args) {
        launch(args);
    }
}