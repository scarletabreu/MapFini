package Visual;

import backend.Classes.Stop;
import backend.Controller.WorldMap;
import backend.Enum.Priority;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class MapDashboard {
    @FXML private VBox stopListVBox;
    @FXML private VBox routeListVBox;
    @FXML private Pane mapPane;
    @FXML private Button addStopButton;
    @FXML private Button enrouteButton;
    @FXML private Button findPathButton;
    @FXML private ComboBox<String> priorityComboBox;

    private Priority selectedPriority;
    private Circle hoverCircle;
    private final Map<Stop, Circle> stopCircles = new HashMap<>();
    private final Map<Pair<Stop, Stop>, Line> routeLines = new HashMap<>();
    private final List<Line> highlightedPath = new ArrayList<>();
    private final WorldMap worldMap = WorldMap.getInstance();

    private boolean isAddingStop = false;
    private boolean isRouting = false;
    private boolean isPathFinding = false;
    private Stop selectedStartStop;
    private Stop pathStart;

    @FXML
    public void initialize() {
        setupUI();
        setupPriorityComboBox();
    }

    private void setupUI() {
        configureHoverCircle();
        configureVBoxes();
        configureButtons();
        configureMapPane();
    }

    private void configureHoverCircle() {
        hoverCircle = new Circle(10, Color.BLUE);
        hoverCircle.setOpacity(0.5);
        mapPane.getChildren().add(hoverCircle);
        hoverCircle.setVisible(false);
    }

    private void configureVBoxes() {
        stopListVBox.setPadding(new Insets(5));
        stopListVBox.setSpacing(5);
        stopListVBox.setStyle(createDarkStyle());

        routeListVBox.setPadding(new Insets(5));
        routeListVBox.setSpacing(5);
        routeListVBox.setStyle(createDarkStyle());
    }

    private void configureButtons() {
        styleButton(addStopButton);
        styleButton(findPathButton);
        styleButton(enrouteButton);

        addStopButton.setOnAction(e -> handleAddStop());
        findPathButton.setOnAction(e -> handleFindPath());
        enrouteButton.setOnAction(e -> handleEnroute());
    }

    private void configureMapPane() {
        mapPane.setPrefSize(1000, 1000);
        mapPane.setStyle("-fx-background-color: #56525C; -fx-background-radius: 15;");
    }

    private void setupPriorityComboBox() {
        priorityComboBox.setStyle(createDarkStyle());
        priorityComboBox.setItems(FXCollections.observableArrayList("DISTANCE", "TIME", "COST", "TRANSPORTS"));

        priorityComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                selectedPriority = Priority.valueOf(newValue)
        );
    }

    private String createDarkStyle() {
        return "-fx-background-color: #302836; " +
                "-fx-text-fill: #FEFEFE; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #AA7CFB; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10;";
    }

    private void styleButton(Button button) {
        button.setStyle(createDarkStyle());
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #AA7CFB; " +
                        "-fx-text-fill: #FEFEFE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #302836; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10;"
        ));
        button.setOnMouseExited(e -> button.setStyle(createDarkStyle()));
    }

    public static void showMapDashboard(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(MapDashboard.class.getResource("/org/example/NodeMap/MapDashboard.fxml"));
            Pane root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Map Dashboard");
            primaryStage.setScene(scene);
            primaryStage.setWidth(primaryStage.getScene().getWindow().getWidth());
            primaryStage.setHeight(primaryStage.getScene().getWindow().getHeight());
            primaryStage.getIcons().add(new javafx.scene.image.Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/TheMap.png"));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMouseMove(MouseEvent event) {
        if (!isAddingStop) return;

        Bounds mapBounds = mapPane.getBoundsInLocal();
        double x = Math.min(Math.max(event.getX(), hoverCircle.getRadius()),
                mapBounds.getWidth() - hoverCircle.getRadius());
        double y = Math.min(Math.max(event.getY(), hoverCircle.getRadius()),
                mapBounds.getHeight() - hoverCircle.getRadius());

        hoverCircle.setCenterX(x);
        hoverCircle.setCenterY(y);
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (isAddingStop) handleStopPlacement(event);
    }

    private void handleStopPlacement(MouseEvent event) {
        Bounds mapBounds = mapPane.getBoundsInLocal();
        if (!mapBounds.contains(event.getX(), event.getY())) return;

        double x = hoverCircle.getCenterX();
        double y = hoverCircle.getCenterY();
        Stop newStop = new Stop(x, y);
        worldMap.addStop(newStop);

        Circle stopCircle = createStopCircle(newStop, x, y);
        Button stopButton = createStopButton(String.format("Stop %d (%.1f, %.1f)", newStop.getId(), x, y), newStop);

        stopListVBox.getChildren().remove(addStopButton);
        stopListVBox.getChildren().add(stopButton);
        stopListVBox.getChildren().add(addStopButton);

        isAddingStop = false;
        hoverCircle.setVisible(false);
    }

    private Circle createStopCircle(Stop stop, double x, double y) {
        Circle stopCircle = new Circle(x, y, 10, Color.RED);
        stopCircle.setOnMouseClicked(e -> handleStopCircleClick(stop));
        mapPane.getChildren().add(stopCircle);
        stopCircles.put(stop, stopCircle);
        return stopCircle;
    }

    private void handleStopCircleClick(Stop stop) {
        if (isRouting) handleRoutingClick(stop);
        else if (isPathFinding) handlePathFindingClick(stop);
    }

    private void handleRoutingClick(Stop stop) {
        if (selectedStartStop == null) {
            selectedStartStop = stop;
            stopCircles.get(stop).setFill(Color.GREEN);
        } else if (selectedStartStop != stop) {
            showRouteDialog(selectedStartStop, stop);
        }
    }

    private void handlePathFindingClick(Stop stop) {
        if (pathStart == null) {
            pathStart = stop;
            stopCircles.get(stop).setFill(Color.GREEN);
        } else if (pathStart != stop) {
            findAndHighlightPath(pathStart, stop);
            stopCircles.get(pathStart).setFill(Color.RED);
            resetPathFinding();
        }
    }

    private void resetPathFinding() {
        pathStart = null;
        isPathFinding = false;
        findPathButton.setStyle("");
    }

    @FXML
    private void handleFindPath() {
        isPathFinding = !isPathFinding;
        isRouting = false;
        findPathButton.setStyle(isPathFinding ? "-fx-background-color: #ff8080;" : "");
        enrouteButton.setStyle("");
        pathStart = null;

        clearHighlightedPath();
        updateStopCircleCursors(isPathFinding);
    }

    @FXML
    private void handleEnroute() {
        isRouting = !isRouting;
        enrouteButton.setStyle(isRouting ? "-fx-background-color: #ef2121;" : "");
        selectedStartStop = null;

        updateStopCircleCursors(isRouting);
    }

    private void updateStopCircleCursors(boolean active) {
        stopCircles.values().forEach(circle ->
                circle.setStyle(active ? "-fx-cursor: hand;" : ""));
    }

    private void findAndHighlightPath(Stop start, Stop end) {
        clearHighlightedPath();
        worldMap.dijkstra(start, end, selectedPriority);

        List<Stop> path = worldMap.getLastCalculatedPath();
        if (path == null || path.isEmpty()) {
            showAlert("No path found between selected stops.");
            return;
        }

        highlightPath(path);
    }

    private void highlightPath(List<Stop> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Stop current = path.get(i);
            Stop next = path.get(i + 1);

            Line line = routeLines.get(new Pair<>(current, next));
            if (line != null) {
                line.setStroke(Color.YELLOW);
                line.setStrokeWidth(4);
                highlightedPath.add(line);
            }
        }
    }

    private void clearHighlightedPath() {
        for (Line line : highlightedPath) {
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
        }
        highlightedPath.clear();
    }

    @FXML
    private void handleAddStop() {
        isAddingStop = true;
        hoverCircle.setVisible(true);
        mapPane.requestFocus();
    }

    private void createRoute(Stop start, Stop end, int distance, int time, int cost, int transport) {
        Line line = createRouteLine(start, end);

        // Store the line in routes map (both directions)
        routeLines.put(new Pair<>(start, end), line);
        routeLines.put(new Pair<>(end, start), line);

        // Add route to WorldMap
        start.addVertex(end, distance, time, cost, transport);
        worldMap.createRoute(start.getRoute(end));

        // Create route button
        Button routeButton = new Button(String.format("Route %d→%d: %dm, %ds, $%d, Type %d",
                start.getId(), end.getId(), distance, time, cost, transport));
        routeButton.setMaxWidth(Double.MAX_VALUE);
        routeListVBox.getChildren().add(routeButton);
    }

    private Line createRouteLine(Stop start, Stop end) {
        Circle startCircle = stopCircles.get(start);
        Circle endCircle = stopCircles.get(end);

        Line line = new Line(
                startCircle.getCenterX(),
                startCircle.getCenterY(),
                endCircle.getCenterX(),
                endCircle.getCenterY()
        );
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2);
        mapPane.getChildren().add(line);

        return line;
    }

    private Button createStopButton(String text, Stop stop) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> {
            if (isRouting) handleStopCircleClick(stop);
        });
        return button;
    }

    private void showRouteDialog(Stop start, Stop end) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/NodeMap/RouteDialog.fxml"));
            DialogPane dialogPane = loader.load();

            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Create Route");

            TextField distanceField = (TextField) dialogPane.lookup("#distanceField");
            TextField timeField = (TextField) dialogPane.lookup("#timeField");
            TextField costField = (TextField) dialogPane.lookup("#costField");
            ComboBox<Integer> transportComboBox = (ComboBox<Integer>) dialogPane.lookup("#transportComboBox");


            transportComboBox.getItems().addAll(1, 2, 3, 4);

            ButtonType confirmButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    try {
                        int distance = Integer.parseInt(distanceField.getText());
                        int time = Integer.parseInt(timeField.getText());
                        int cost = Integer.parseInt(costField.getText());
                        int transport = transportComboBox.getValue();

                        createRoute(start, end, distance, time, cost, transport);
                        return new Pair<>(String.valueOf(start.getId()), String.valueOf(end.getId()));
                    } catch (NumberFormatException e) {
                        showAlert("Invalid input. Please enter valid numbers.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();
            stopCircles.get(selectedStartStop).setFill(Color.RED);
            selectedStartStop = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}