package Visual;

import backend.Classes.Route;
import backend.Classes.Stop;
import backend.Controller.WorldMap;
import backend.Enum.Algorithm;
import backend.Enum.Priority;
import backend.Enum.Traffic;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.*;

@SuppressWarnings({"CallToPrintStackTrace", "unchecked"})
public class MapDashboard {
    @FXML
    private VBox stopListVBox;
    @FXML
    private VBox routeListVBox;
    @FXML
    private Pane mapPane;
    @FXML
    private Button addStopButton;
    @FXML
    private Button enrouteButton;
    @FXML
    private Button findPathButton;
    @FXML
    private ComboBox<String> priorityComboBox = new ComboBox<>();
    @FXML
    private ComboBox<String> algorithmComboBox = new ComboBox<>();
    @FXML
    private Button deleteStopButton;

    private Priority selectedPriority;
    private Algorithm selectedAlgorithm;
    private Circle hoverCircle;
    private final Map<Stop, Circle> stopCircles;
    private boolean isAddingStop = false;
    private boolean isRouting = false;
    private boolean isDeleting = false;
    private Stop selectedStartStop = null;
    private final WorldMap worldMap;
    private boolean isPathFinding = false;
    private Stop pathStart = null;
    private final Map<Pair<Stop, Stop>, Line> routeLines;
    private final List<Line> highlightedPath;
    private final Set<Pair<Stop, Stop>> createdRoutes = new HashSet<>();
    private final String buttonStyle = "-fx-background-color: #302836; -fx-text-fill: #FEFEFE; -fx-font-size: 14px; -fx-background-radius: 10; -fx-border-color: #AA7CFB; -fx-border-width: 2px; -fx-border-radius: 10;";
    private final String buttonHoverStyle = "-fx-background-color: #AA7CFB; -fx-text-fill: #FEFEFE; -fx-font-size: 14px; -fx-background-radius: 10; -fx-border-color: #302836; -fx-border-width: 2px; -fx-border-radius: 10;";

    public MapDashboard() {
        stopCircles = new HashMap<>();
        routeLines = new HashMap<>();
        highlightedPath = new ArrayList<>();
        worldMap = WorldMap.getInstance();
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
    public void initialize() {
        hoverCircle = new Circle(10, Color.BLUE);
        hoverCircle.setOpacity(0.5);
        mapPane.getChildren().add(hoverCircle);
        hoverCircle.setVisible(false);
        setupButtonStyles();
        setupPriorityComboBox();
        mapPane.setPrefHeight(1000);
        mapPane.setPrefWidth(1000);
        mapPane.setStyle("-fx-background-color: #56525C; -fx-background-radius: 15;");
    }

    private void setupButtonStyles() {
        deleteStopButton.setStyle(buttonStyle);
        addStopButton.setStyle(buttonStyle);
        findPathButton.setStyle(buttonStyle);
        enrouteButton.setStyle(buttonStyle);

        addStopButton.setOnMouseExited(_ -> {
            if (!isAddingStop) addStopButton.setStyle(buttonStyle);
        });
        addStopButton.setOnMouseEntered(_ -> {
            if (!isAddingStop) addStopButton.setStyle(buttonHoverStyle);
        });

        deleteStopButton.setOnMouseExited(_ -> {
            if (!isDeleting) deleteStopButton.setStyle(buttonStyle);
        });
        deleteStopButton.setOnMouseEntered(_ -> {
            if (!isDeleting) deleteStopButton.setStyle(buttonHoverStyle);
        });
        findPathButton.setOnMouseEntered(_ -> {
            if (!isPathFinding) findPathButton.setStyle(buttonHoverStyle);
        });
        findPathButton.setOnMouseExited(_ -> {
            if (!isPathFinding) findPathButton.setStyle(buttonStyle);
        });
        enrouteButton.setOnMouseEntered(_ -> {
            if (!isRouting) enrouteButton.setStyle(buttonHoverStyle);
        });
        enrouteButton.setOnMouseExited(_ -> {
            if (!isRouting) enrouteButton.setStyle(buttonStyle);
        });

    }

    private void setupPriorityComboBox() {
        priorityComboBox.setItems(FXCollections.observableArrayList("MIXED", "DISTANCE", "TIME", "COST", "TRANSPORTS"));
        priorityComboBox.setValue("MIXED");
        priorityComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String enumValue) {
                return enumValue;
            }

            @Override
            public String fromString(String stringValue) {
                return stringValue;
            }
        });
        priorityComboBox.valueProperty().addListener((_, _, newValue) -> selectedPriority = Priority.valueOf(newValue));
        algorithmComboBox.setItems(FXCollections.observableArrayList("DIJKSTRA", "FLOYD_WARSHALL", "BELLMAN_FORD"));
        algorithmComboBox.setValue("DIJKSTRA");
        priorityComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String enumValue) {
                return enumValue;
            }

            @Override
            public String fromString(String stringValue) {
                return stringValue;
            }
        });
        algorithmComboBox.valueProperty().addListener((_, _, newValue) -> selectedAlgorithm = Algorithm.valueOf(newValue));
    }

    @FXML
    private void handleFindPath() {
        if(isPathFinding){
            untoggleButtons();
            return;
        }
        untoggleButtons(); // Untoggle all buttons
        isPathFinding = !isPathFinding;
        toggleButtonStyle(findPathButton); // Toggle the find path button
        enrouteButton.setStyle(buttonStyle); // Reset enroute button style
        pathStart = null;
        clearHighlightedPath();
        stopCircles.values().forEach(circle -> circle.setStyle(isPathFinding ? "-fx-cursor: hand;" : ""));
    }

    public void deleteStopButtonById(Integer id){
        for(Node n: stopListVBox.getChildren()){
            if(n instanceof Button){
                String comparable = ((Button) n).getText().replace("Stop ", "");
                int referenceIndex = comparable.indexOf('(');
                comparable = comparable.substring(0,referenceIndex-1);
                if(Integer.parseInt(comparable) == id) {
                    stopListVBox.getChildren().remove(n);
                    break;
                }
            }
        }
    }

    public void deleteRouteButtonByStops(int start, int end){
        for(Node n: routeListVBox.getChildren()){
            if(n instanceof Button){
                String stops = ((Button) n).getText().replace("Route ", "");
                int referenceIndex = stops.lastIndexOf('→');
                int stop1 = Integer.parseInt(stops.substring(0, referenceIndex));
                int stop2 = Integer.parseInt(stops.substring(referenceIndex+1));
                System.out.println(stop1 + " " + stop2);
                if(start == stop1 && stop2 == end) {
                    routeListVBox.getChildren().remove(n);
                    break;
                }
            }
        }
    }

    private void handleStopCircleClick(Stop stop) {
        if(isDeleting){
            if (stop != null) {
                // Remove the stop's circle and label
                Circle stopCircle = stopCircles.get(stop);
                if (stopCircle != null) {
                    mapPane.getChildren().remove(stopCircle); // Remove the circle
                }

                Text stopIdText = getStopIdText(stop); // Find the label for the stop
                if (stopIdText != null) {
                    mapPane.getChildren().remove(stopIdText); // Remove the label
                }

                // Remove routes connected to the stop
                Set<Pair<Stop, Stop>> routesToRemove = new HashSet<>();
                for (Map.Entry<Pair<Stop, Stop>, Line> entry : routeLines.entrySet()) {
                    Pair<Stop, Stop> route = entry.getKey();
                    if (route.getKey().equals(stop) || route.getValue().equals(stop)) {
                        Line routeLine = entry.getValue();
                        mapPane.getChildren().remove(routeLine); // Remove the route line
                        routesToRemove.add(route); // Mark route for removal
                    }
                }

                // Remove the routes from routeLines map
                for (Pair<Stop, Stop> route : routesToRemove) {
                    routeLines.remove(route);
                }

                deleteStopButtonById(stop.getId());

                // Remove the stop from the worldMap
                worldMap.removeStop(stop);

                // Also update stopCircles map to remove the stop
                stopCircles.remove(stop);
            } else {
                showAlert("No stop selected for deletion.");
            }
        }
        else if (isRouting) {
            if (selectedStartStop == null) {
                selectedStartStop = stop;
                stopCircles.get(stop).setFill(Color.GREEN);
            } else if (selectedStartStop != stop) {
                // Ensure that the same two stops are not used twice in a route
                Pair<Stop, Stop> routePair1 = new Pair<>(selectedStartStop, stop);
                Pair<Stop, Stop> routePair2 = new Pair<>(stop, selectedStartStop);

                if (createdRoutes.contains(routePair1) || createdRoutes.contains(routePair2)) {
                    showAlert("This route has already been created.");
                    return; // Do not proceed with creating the route
                }

                if(showRouteDialog(selectedStartStop, stop, null)) {
                    createdRoutes.add(routePair1); // Add the route to the created routes set
                    createdRoutes.add(routePair2); // Add the reverse route as well
                }
            }
        } else if (isPathFinding) {
            if (pathStart == null) {
                pathStart = stop;
                stopCircles.get(stop).setFill(Color.GREEN);
            } else if (pathStart != stop) {
                findAndHighlightPath(pathStart, stop);
                stopCircles.get(pathStart).setFill(Color.BLUE);
                stopCircles.get(stop).setFill(Color.BLUE);
                pathStart = null;
                isPathFinding = false;
                findPathButton.setStyle(buttonStyle);
            }
        }
    }

    private Text getStopIdText(Stop stop) {
        for (javafx.scene.Node node : mapPane.getChildren()) {
            if (node instanceof Text textNode) {
                if (textNode.getText().startsWith("Stop No. " + stop.getId())) {
                    return textNode;
                }
            }
        }
        return null;
    }


    private void findAndHighlightPath(Stop start, Stop end) {
        clearHighlightedPath();
        List <Stop> path = worldMap.findBestPath(start,end,selectedPriority,selectedAlgorithm);
        if (path == null || path.isEmpty()) {
            showAlert("No path found between selected stops.");
            return;
        }
        for (int i = 0; i < path.size() - 1; i++) {
            Stop current = path.get(i);
            Stop next = path.get(i + 1);
            Pair<Stop, Stop> routePair = new Pair<>(current, next);
            Line line = routeLines.get(routePair);
            if (line != null) {
                line.setStroke(Color.YELLOW);
                line.setStrokeWidth(4);
                highlightedPath.add(line);
            }
        }
    }

    private void clearHighlightedPath() {
        highlightedPath.forEach(line -> {
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
        });
        highlightedPath.clear();
    }

    @FXML
    private void handleEnroute() {
        if(isRouting){
            untoggleButtons();
            return;
        }
        untoggleButtons(); // Untoggle all buttons
        isRouting = !isRouting;
        toggleButtonStyle(enrouteButton); // Toggle the enroute button
        selectedStartStop = null;
        stopCircles.values().forEach(circle -> circle.setStyle(isRouting ? "-fx-cursor: hand;" : ""));
    }

    @FXML
    private void handleAddStop() {
        if(isAddingStop){
            untoggleButtons();
            return;
        }
        untoggleButtons();
        isAddingStop = true;
        hoverCircle.setVisible(true);
        mapPane.requestFocus();
        toggleButtonStyle(addStopButton); // Toggle the "Add Stop" button
    }

    @FXML
    private void handleDeleteStop() {
        if(isDeleting){
            untoggleButtons();
            return;
        }
        untoggleButtons();
        isDeleting = true;
        hoverCircle.setVisible(true);
        mapPane.requestFocus();
        toggleButtonStyle(deleteStopButton); // Toggle the "Add Stop" button
        stopCircles.values().forEach(circle -> circle.setStyle(isDeleting ? "-fx-cursor: hand;" : ""));
    }


    @FXML
    private void handleMouseMove(MouseEvent event) {
        if (!isAddingStop) return;
        Bounds mapBounds = mapPane.getBoundsInLocal();
        double x = Math.min(Math.max(event.getX(), hoverCircle.getRadius()), mapBounds.getWidth() - hoverCircle.getRadius());
        double y = Math.min(Math.max(event.getY(), hoverCircle.getRadius()), mapBounds.getHeight() - hoverCircle.getRadius());
        hoverCircle.setCenterX(x);
        hoverCircle.setCenterY(y);
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (isAddingStop) {
            handleStopPlacement(event);
        }
    }

    private void handleStopPlacement(MouseEvent event) {
        Bounds mapBounds = mapPane.getBoundsInLocal();
        if (!mapBounds.contains(event.getX(), event.getY())) return;
        double x = hoverCircle.getCenterX();
        double y = hoverCircle.getCenterY();
        Stop newStop = new Stop(x, y);
        worldMap.addStop(newStop);
        Circle stopCircle = new Circle(x, y, 10, Color.RED);
        stopCircle.setOnMouseClicked(_ -> handleStopCircleClick(newStop));
        mapPane.getChildren().add(stopCircle);
        stopCircles.put(newStop, stopCircle);

        // Create the label for the stop's ID above the stop circle
        String idStop = "Stop No. " + newStop.getId();
        Text stopIdText = new Text(idStop);
        stopIdText.setFill(Color.WHITE);
        stopIdText.setStyle("-fx-font-weight: bold;");
        stopIdText.setX(x - idStop.length() * 3); // Centered horizontally (adjust the offset as needed)
        stopIdText.setY(y - 15); // Position above the circle (adjust the offset as needed)
        mapPane.getChildren().add(stopIdText);

        Button stopButton = createStopButton(String.format("Stop %d (%.1f, %.1f)", newStop.getId(), x, y), newStop);
        stopListVBox.getChildren().remove(addStopButton);
        stopListVBox.getChildren().add(stopButton);
        stopListVBox.getChildren().add(addStopButton);
        untoggleButtons();
        isAddingStop = false;
        hoverCircle.setVisible(false);
    }


    private void createRoute(Stop start, Stop end, int distance, int time, int cost, int transport, Traffic traffic, boolean isUpdating) {
        if(!isUpdating) {
            Circle startCircle = stopCircles.get(start);
            Circle endCircle = stopCircles.get(end);
            Line line = new Line(startCircle.getCenterX(), startCircle.getCenterY(), endCircle.getCenterX(), endCircle.getCenterY());
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            mapPane.getChildren().add(line);
            routeLines.put(new Pair<>(start, end), line);
            routeLines.put(new Pair<>(end, start), line);
            start.addVertex(end);
            Route newRoute = new Route(start.getId(), end.getId(), distance, time, cost, transport, traffic);
            worldMap.createRoute(newRoute);
            Button routeButton = new Button(String.format("Route %d→%d", start.getId(), end.getId()));
            routeButton.setStyle(buttonStyle);
            routeButton.setOnMouseEntered(_ -> routeButton.setStyle(buttonHoverStyle));
            routeButton.setOnMouseExited(_ -> routeButton.setStyle(buttonStyle));
            routeButton.setOnMouseClicked(_ -> {
                if (showRouteDialog(start, end, worldMap.getRoute(start.getId(),end.getId()))) {
                    createRoute(start,end,worldMap.getRoute(start.getId(),end.getId()).getDistance(),
                            worldMap.getRoute(start.getId(),end.getId()).getTime(),
                            worldMap.getRoute(start.getId(),end.getId()).getCost(),
                            worldMap.getRoute(start.getId(),end.getId()).getTransports(),
                            worldMap.getRoute(start.getId(),end.getId()).getTraffic(),true);
                }
            });
            routeButton.setMaxWidth(Double.MAX_VALUE);
            routeListVBox.getChildren().add(routeButton);
        }
        else{
            worldMap.updateRoute(new Route(start.getId(),end.getId(),distance,time,cost,transport,worldMap.getRoute(start.getId(),end.getId()).getId(), traffic));
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void untoggleButtons() {
        hoverCircle.setVisible(false);
        isAddingStop = false;
        isRouting = false;
        isPathFinding = false;
        isDeleting = false;
        resetButtonStyle(deleteStopButton);
        resetButtonStyle(addStopButton);
        resetButtonStyle(enrouteButton);
        resetButtonStyle(findPathButton);
    }

    private void resetButtonStyle(Button button) {
        String defaultStyle = "-fx-background-color: #302836; -fx-text-fill: #FEFEFE; -fx-font-size: 14px; -fx-background-radius: 10; -fx-border-color: #AA7CFB; -fx-border-width: 2px; -fx-border-radius: 10;";
        button.setStyle(defaultStyle);
    }

    private void toggleButtonStyle(Button button) {
        // Invert the style of the button when toggled
        String toggledStyle = "-fx-background-color: #AA7CFB; -fx-text-fill: #302836; -fx-font-size: 14px; -fx-background-radius: 10; -fx-border-color: #302836; -fx-border-width: 2px; -fx-border-radius: 10;";
        button.setStyle(toggledStyle);
    }


    private boolean showRouteDialog(Stop start, Stop end, Route updatingRoute) {
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
            ComboBox<String> trafficComboBox = (ComboBox<String>) dialogPane.lookup("#trafficComboBox");
            ButtonType confirmButtonType;
            ButtonType cancelButtonType;
            if(updatingRoute != null){
                confirmButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                cancelButtonType = new ButtonType("Delete", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.setTitle("Update Route");
                distanceField.setText(String.valueOf(updatingRoute.getDistance()));
                timeField.setText(String.valueOf(updatingRoute.getTime()));
                costField.setText(String.valueOf(updatingRoute.getCost()));
                transportComboBox.setValue(updatingRoute.getTransports());
                trafficComboBox.setValue(updatingRoute.getTraffic().name());
            } else {
                confirmButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
                cancelButtonType = ButtonType.CANCEL;
            }
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    try {
                        int distance = Integer.parseInt(distanceField.getText());
                        int time = Integer.parseInt(timeField.getText());
                        int cost = Integer.parseInt(costField.getText());
                        int transport = (transportComboBox.getValue() != null) ? transportComboBox.getValue() : 1;
                        Traffic traffic = Traffic.valueOf(trafficComboBox.getValue());
                        createRoute(start, end, distance, time, cost, transport, traffic, (updatingRoute != null));
                        return new Pair<>(start.getId() + "", end.getId() + "");
                    } catch (NumberFormatException e) {
                        showAlert("Invalid input. Please enter valid numbers.");
                        return null;
                    }
                }
                else if(dialogButton == cancelButtonType){
                    if(Objects.equals(cancelButtonType.getText(), "Delete")) {
                        System.out.println("wazaaaa");
                        worldMap.deleteRoute(updatingRoute);
                        deleteRouteButtonByStops(start.getId(),end.getId());
                        Line line = routeLines.get(new Pair<>(start,end));
                        mapPane.getChildren().remove(line);
                        routeLines.remove(new Pair<>(start,end));
                    }
                    return null;
                }
                return null;
            });
            Optional<Pair<String,String>> result = dialog.showAndWait();
            if(selectedStartStop != null) stopCircles.get(selectedStartStop).setFill(Color.RED);
            selectedStartStop = null;
            if(updatingRoute != null) return false;
            return (result.isPresent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Button createStopButton(String text, Stop stop) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(_ -> {
            if (isRouting) {
                handleStopCircleClick(stop);
            }
        });
        return button;
    }
}
