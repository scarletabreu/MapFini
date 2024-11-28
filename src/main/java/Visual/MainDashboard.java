package Visual;

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

public class MainDashboard extends Application {
    private Scene scene;

    public static void showDashboard(Stage primaryStage) {
        MainDashboard dashboard = new MainDashboard();
        try {
            primaryStage.getIcons().add(new Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/TheMap.png"));
            dashboard.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {

        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: #302836;");

        // Root container como VBox
        VBox rootContainer = new VBox(20);
        rootContainer.setStyle("-fx-background-color: #302836;");
        rootContainer.setPadding(new Insets(20));

        // Contenedor para la sección superior (menús laterales y contenido central)
        HBox topSection = new HBox(10);
        VBox.setVgrow(topSection, Priority.ALWAYS); // Esto hace que topSection ocupe todo el espacio vertical disponible

        // Left menu setup
        VBox leftMenu = new VBox(10);
        leftMenu.setStyle("-fx-background-color: #56525C; -fx-background-radius: 10;");
        leftMenu.setPrefWidth(80);
        HBox.setHgrow(leftMenu, Priority.NEVER); // Evita que el leftMenu se expanda horizontalmente

        Button menuButton = new Button("≡");
        menuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        menuButton.setOnAction(e -> {
            // Verificar si el Sidebar ya está visible en el contenedor
            Sidebar sidebar = findSidebarInStackPane(mainContainer);

            if (sidebar == null || !sidebar.isVisible()) {
                // Si el Sidebar no está visible, crear uno nuevo y realizar la animación de entrada
                sidebar = new Sidebar();
                mainContainer.getChildren().add(sidebar);  // Agregar el Sidebar al StackPane
                StackPane.setAlignment(sidebar, Pos.TOP_LEFT);  // Alinear el Sidebar al lado izquierdo
                sidebar.slideIn();  // Animación para que el Sidebar se deslice hacia dentro
            }
        });


        leftMenu.getChildren().add(menuButton);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        leftMenu.getChildren().add(spacer);

        Image image = new Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/MainUser.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        leftMenu.getChildren().add(imageView);

        leftMenu.setAlignment(Pos.TOP_CENTER);
        leftMenu.setPadding(new Insets(10));

        // Contenedor central para el contenido y las InfoCards
        VBox centerContainer = new VBox(20);
        HBox.setHgrow(centerContainer, Priority.ALWAYS);

        // Center content
        VBox centerContent = new VBox(20);
        centerContent.setStyle("-fx-background-color: #56525C; -fx-background-radius: 10;");
        centerContent.setPadding(new Insets(20));
        centerContent.setMaxHeight(500); // Altura fija para el contenido central
        centerContent.setAlignment(Pos.TOP_LEFT);

        // Welcome label
        Label welcomeLabel = new Label("Welcome Back UserName");
        welcomeLabel.setStyle("-fx-text-fill: #FEFEFE; -fx-font-size: 24px; -fx-font-weight: bold;");

        // Buttons container
        VBox buttonContainer = new VBox(20);
        buttonContainer.setAlignment(Pos.CENTER);

        // Top buttons
        HBox topButtons = new HBox(20);
        topButtons.setAlignment(Pos.CENTER);

        Button openMapBtn = createPurpleButton("Open Map");

        Image photo1 = new Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/worldMap.png");  // Reemplaza con la ruta correcta de tu imagen
        ImageView photoView1 = new ImageView(photo1);

        photoView1.setFitHeight(60);  // Establecer altura de la imagen
        photoView1.setFitWidth(60);   // Establecer ancho de la imagen

        HBox buttonContent1 = new HBox(5);
        buttonContent1.getChildren().addAll(photoView1, new Label());  // Coloca la imagen y el texto en el HBox

        openMapBtn.setGraphic(buttonContent1);

        openMapBtn.setOnAction(e -> {
            // Acción cuando se hace clic en el botón
            System.out.println("Button clicked!");
        });
        // Crear el botón
        Button createMapBtn = createPurpleButton("Create Map");

        // Crear la imagen para el botón
        Image photo = new Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/MapGPS.png");  // Reemplaza con la ruta correcta de tu imagen
        ImageView photoView = new ImageView(photo);

        // Ajustar el tamaño de la imagen (opcional)
        photoView.setFitHeight(60);  // Establecer altura de la imagen
        photoView.setFitWidth(60);   // Establecer ancho de la imagen

        // Crear un HBox para colocar la imagen y el texto
        HBox buttonContent = new HBox(5);
        buttonContent.getChildren().addAll(photoView, new Label());  // Coloca la imagen y el texto en el HBox

        // Establecer el HBox como el contenido gráfico del botón
        createMapBtn.setGraphic(buttonContent);

        // Establecer la acción del botón
        createMapBtn.setOnAction(e -> {
            // Acción cuando se hace clic en el botón
            System.out.println("Button clicked!");

            MapDashboard.showMapDashboard(primaryStage);
        });
        topButtons.getChildren().addAll(openMapBtn, createMapBtn);

        // Chart setup
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Distance (km)");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Weekly Activity");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: white; -fx-font-size: 28px;");
        lineChart.setCreateSymbols(true);
        lineChart.setAnimated(false);
        lineChart.setPrefHeight(350);
        lineChart.setPrefWidth(600);
        lineChart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 23));
        series.getData().add(new XYChart.Data<>(2, 14));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 24));
        series.getData().add(new XYChart.Data<>(5, 34));
        series.getData().add(new XYChart.Data<>(6, 36));
        series.getData().add(new XYChart.Data<>(7, 22));

        lineChart.getData().add(series);

        // Chart container
        HBox chartContainer = new HBox(20);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.getChildren().add(lineChart);

        // Add elements to buttonContainer
        buttonContainer.getChildren().addAll(topButtons, chartContainer);
        centerContent.getChildren().addAll(welcomeLabel, buttonContainer);

        // Right panel
        VBox rightPanel = new VBox(10);
        rightPanel.setStyle("-fx-background-color: #56525C; -fx-background-radius: 10;");
        rightPanel.setPrefWidth(200);
        HBox.setHgrow(rightPanel, Priority.NEVER); // Evita que el rightPanel se expanda horizontalmente

        Button recordButton = new Button("Record");
        recordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        rightPanel.getChildren().add(recordButton);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(10));

        // Info cards section
        HBox infoCards = new HBox(20);
        infoCards.setAlignment(Pos.CENTER);
        infoCards.setPadding(new Insets(20, 0, 0, 0));

        VBox recentActivity = createInfoCard("Recent Activity",
                "file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/Time.png",
                "Last Map: Yesterday",
                "Last route created: 0 hrs");

        VBox yourRoutes = createInfoCard("Your Routes",
                "file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/Routes.png",
                "Active: 0",
                "Total Distance: 0 km");

        infoCards.getChildren().addAll(recentActivity, yourRoutes);

        // Agregar centerContent e infoCards al centerContainer
        centerContainer.getChildren().addAll(centerContent, infoCards);

        // Agregar todos los paneles al topSection
        topSection.getChildren().addAll(leftMenu, centerContainer, rightPanel);


        // Agregar topSection al rootContainer
        rootContainer.getChildren().add(topSection);

        mainContainer.getChildren().add(rootContainer);  // Agregar el rootContainer al StackPane principal


        // CSS styles for the chart
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

        // Scene setup
        scene = new Scene(mainContainer, 800, 500);
        scene.getStylesheets().add("data:text/css," + css.replace(" ", "%20"));

        // Stage setup
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
        return null;  // No encontrado
    }

    // Los métodos createPurpleButton y createInfoCard permanecen sin cambios
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
        // Crear la tarjeta
        VBox card = new VBox(20);  // Espacio entre los elementos
        card.setStyle("-fx-background-color: #AA7CFB; " +
                "-fx-padding: 20; " +
                "-fx-background-radius: 10;");
        card.setPrefWidth(500);

        // Crear un HBox para alinear el título y la imagen horizontalmente
        HBox titleAndImage = new HBox(200);  // Espacio entre el título y la imagen
        titleAndImage.setAlignment(Pos.CENTER_LEFT);  // Alineamos a la izquierda

        // Crear ImageView para la imagen
        Image image = new Image(imageFilePath);  // Usar la ruta de la imagen
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);  // Ajusta el tamaño de la imagen (ancho)
        imageView.setFitHeight(40); // Ajusta el tamaño de la imagen (alto)
        imageView.setPreserveRatio(true);  // Mantiene la proporción de la imagen

        // Crear el título de la tarjeta
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #FEFEFE; -fx-font-size: 24px; -fx-font-weight: bold;");

        // Añadir el título y la imagen al HBox
        titleAndImage.getChildren().addAll(titleLabel, imageView);

        // Crear contenido de la tarjeta
        VBox contentBox = new VBox(10);
        for (String text : content) {
            Label label = new Label(text);
            label.setStyle("-fx-text-fill: #FEFEFE; -fx-font-size: 16px;");
            contentBox.getChildren().add(label);
        }

        // Añadir el HBox y el contenido a la tarjeta
        card.getChildren().addAll(titleAndImage, contentBox);

        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}