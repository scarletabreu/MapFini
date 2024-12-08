package Visual;

import backend.Controller.WorldMap;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class MainDashboard extends Application {
    private Scene scene;

    public static void showDashboard(Stage primaryStage) {
        MainDashboard dashboard = new MainDashboard();
        try {

            primaryStage.getIcons().add(new Image(Objects.requireNonNull(MainDashboard.class.getResource("/Photos/TheMap.png")).toExternalForm()));
            dashboard.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {

        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: #302836;");

        VBox rootContainer = new VBox(20);
        rootContainer.setStyle("-fx-background-color: #302836;");
        rootContainer.setPadding(new Insets(20));

        HBox topSection = new HBox(10);
        VBox.setVgrow(topSection, Priority.ALWAYS);

        VBox leftMenu = new VBox(10);
        leftMenu.setStyle("-fx-background-color: #56525C; -fx-background-radius: 10;");
        leftMenu.setPrefWidth(80);
        HBox.setHgrow(leftMenu, Priority.NEVER);

        Button menuButton = new Button("≡");
        menuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        menuButton.setOnAction(e -> {
            Sidebar sidebar = findSidebarInStackPane(mainContainer);

            if (sidebar == null || !sidebar.isVisible()) {
                sidebar = new Sidebar();
                mainContainer.getChildren().add(sidebar);
                StackPane.setAlignment(sidebar, Pos.TOP_LEFT);
                sidebar.slideIn();
            }
        });

        leftMenu.getChildren().add(menuButton);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        leftMenu.getChildren().add(spacer);

        Image image = new Image(Objects.requireNonNull(MainDashboard.class.getResource("/Photos/MainUser.png")).toExternalForm());;
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        leftMenu.getChildren().add(imageView);

        leftMenu.setAlignment(Pos.TOP_CENTER);
        leftMenu.setPadding(new Insets(10));

        VBox centerContainer = new VBox(20);
        HBox.setHgrow(centerContainer, Priority.ALWAYS);

        VBox centerContent = new VBox(20);
        centerContent.setStyle("-fx-background-color: #56525C; -fx-background-radius: 10;");
        centerContent.setPadding(new Insets(20));
        centerContent.setMaxHeight(500); // Altura fija para el contenido central
        centerContent.setAlignment(Pos.TOP_LEFT);

        Label welcomeLabel = new Label("Welcome Back UserName");
        welcomeLabel.setStyle("-fx-text-fill: #FEFEFE; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox buttonContainer = new VBox(20);
        buttonContainer.setAlignment(Pos.CENTER);

        HBox topButtons = new HBox(20);
        topButtons.setAlignment(Pos.CENTER);

        Button openMapBtn = createPurpleButton("Open Map");

        Image photo1 = new Image("file:/C:/Users/Scarlet/Downloads/A-DT/opop/src/main/resources/Photos/worldMap.png");  // Reemplaza con la ruta correcta de tu imagen
        ImageView photoView1 = new ImageView(photo1);

        photoView1.setFitHeight(60);
        photoView1.setFitWidth(60);

        HBox buttonContent1 = new HBox(5);
        buttonContent1.getChildren().addAll(photoView1, new Label());

        openMapBtn.setGraphic(buttonContent1);

        openMapBtn.setOnAction(e -> {
            System.out.println("Abrir el mapa");
            // Cargar los mapas del usuario
            OpenMap.showDashboard(primaryStage);
        });
        Button createMapBtn = createPurpleButton("Create Map");

        Image photo = new Image("file:/C:/Users/Scarlet/Downloads/A-DT/opop/src/main/resources/Photos/MapGPS.png");  // Reemplaza con la ruta correcta de tu imagen
        ImageView photoView = new ImageView(photo);

        photoView.setFitHeight(60);
        photoView.setFitWidth(60);

        HBox buttonContent = new HBox(5);
        buttonContent.getChildren().addAll(photoView, new Label());

        createMapBtn.setGraphic(buttonContent);

        createMapBtn.setOnAction(e -> {
            System.out.println("Crear Mapa");

            MapDashboard.showMapDashboard(primaryStage);
        });
        topButtons.getChildren().addAll(openMapBtn, createMapBtn);

        buttonContainer.getChildren().addAll(topButtons/* add the element (la foto )*/);
        centerContent.getChildren().addAll(welcomeLabel, buttonContainer);

        VBox rightPanel = new VBox(10);
        rightPanel.setStyle("-fx-background-color: #56525C; -fx-background-radius: 10;");
        rightPanel.setPrefWidth(200);
        HBox.setHgrow(rightPanel, Priority.NEVER);

        Button recordButton = new Button("Record");
        recordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        rightPanel.getChildren().add(recordButton);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(10));

        HBox infoCards = new HBox(20);
        infoCards.setAlignment(Pos.CENTER);
        infoCards.setPadding(new Insets(20, 0, 0, 0));

        VBox recentActivity = createInfoCard("Recent Activity",
                "file:/C:/Users/Scarlet/Downloads/A-DT/opop/src/main/resources/Photos/Time.png",
                "Last Map: Today",
                "Last route created: 30 min");

        VBox yourRoutes = createInfoCard("Your Routes",
                "file:/C:/Users/Scarlet/Downloads/A-DT/opop/src/main/resources/Photos/Routes.png",
                "Active: 10 min",
                "Total Distance: 10 km");

        infoCards.getChildren().addAll(recentActivity, yourRoutes);

        centerContainer.getChildren().addAll(centerContent, infoCards);

        topSection.getChildren().addAll(leftMenu, centerContainer, rightPanel);

        rootContainer.getChildren().add(topSection);

        mainContainer.getChildren().add(rootContainer);

        String css =
                ".chart-plot-background { -fx-background-color: transparent; } " +
                        ".chart-vertical-grid-lines { -fx-stroke: transparent; } " +
                        ".chart-horizontal-grid-lines { -fx-stroke: transparent; } " +
                        ".chart-alternative-row-fill { -fx-fill: transparent; } " +
                        ".chart-alternative-column-fill { -fx-fill: transparent; } " +
                        ".chart-series-line { -fx-stroke: #AA7CFB; -fx-stroke-width: 2px; } " +
                        ".chart-line-symbol { -fx-background-color: #AA7CFB, white; } " +
                        ".axis { -fx-tick-label-fill: white; } " +
                        ".axis-label { -fx-text-fill: white; } " +
                        ".chart-title { -fx-text-fill: white; }";

        scene = new Scene(mainContainer, 800, 500);
        scene.getStylesheets().add("data:text/css," + css.replace(" ", "%20"));

        primaryStage.setTitle("Main Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private Sidebar findSidebarInStackPane(StackPane mainContainer) {
        for (javafx.scene.Node node : mainContainer.getChildren()) {
            if (node instanceof Sidebar) {
                return (Sidebar) node;
            }
        }
        return null;
    }

    private Button createPurpleButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(350);
        button.setPrefHeight(90);

        button.setStyle(
                "-fx-background-color: #AA7CFB; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 20px; " +
                        "-fx-padding: 20 40; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #AA7CFB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        );
        button.setMaxWidth(Double.MAX_VALUE);

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #302836; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 20px; " +
                        "-fx-padding: 20 40; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #AA7CFB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #AA7CFB; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 20px; " +
                        "-fx-padding: 20 40; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #AA7CFB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));

        return button;
    }

    private VBox createInfoCard(String title, String imageFilePath, String... content) {
        VBox card = new VBox(20);
        card.setStyle("-fx-background-color: #AA7CFB; " +
                "-fx-padding: 20; " +
                "-fx-background-radius: 10;");
        card.setPrefWidth(500);

        HBox titleAndImage = new HBox(200);
        titleAndImage.setAlignment(Pos.CENTER_LEFT);

        Image image = new Image(imageFilePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #FEFEFE; -fx-font-size: 24px; -fx-font-weight: bold;");

        titleAndImage.getChildren().addAll(titleLabel, imageView);

        VBox contentBox = new VBox(10);
        for (String text : content) {
            Label label = new Label(text);
            label.setStyle("-fx-text-fill: #FEFEFE; -fx-font-size: 16px;");
            contentBox.getChildren().add(label);
        }

        card.getChildren().addAll(titleAndImage, contentBox);

        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}