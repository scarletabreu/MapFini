package Visual;

import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Objects;

public class Sidebar extends VBox {

    public Sidebar() {
        double width = 250;
        this.setPrefWidth(width);
        this.setMaxWidth(width);

        StackPane.setAlignment(this, Pos.CENTER_LEFT);

        this.setSpacing(20);
        this.setStyle(
                "-fx-background-color: #302836; " +
                        "-fx-padding: 20; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #AA7CFB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        );

        this.setTranslateX(-width);

        // Botón para esconder el Sidebar
        Button closeButton = new Button("≡");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        closeButton.setPrefWidth(200);
        closeButton.setMaxWidth(200);

        // Acción del botón para ocultar el Sidebar
        closeButton.setOnAction(e -> slideOut());
        this.getChildren().add(closeButton); // Añadir el botón al Sidebar

        try {
            Image image = new Image(Objects.requireNonNull(MainDashboard.class.getResource("/Photos/DarkUser.png")).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
            this.getChildren().add(imageView);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }

        Label userNameLabel = new Label("UserName");
        userNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Button accountButton = new Button("Account");
        accountButton.setStyle("-fx-background-color: #AA7CFB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        accountButton.setPrefWidth(200);
        accountButton.setMaxWidth(200);

        Button settingsButton = new Button("Setting");
        settingsButton.setStyle("-fx-background-color: #AA7CFB; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        settingsButton.setPrefWidth(200);
        settingsButton.setMaxWidth(200);

        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(userNameLabel, accountButton, settingsButton);
    }

    public void slideIn() {
        this.setTranslateX(-250);
        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.3), this);
        slideIn.setToX(0);
        slideIn.play();
    }

    public void slideOut() {
        TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.3), this);
        slideOut.setToX(-250); // Usar el mismo valor que setPrefWidth
        slideOut.play();
        slideOut.setOnFinished(event -> this.setVisible(false));
    }
}