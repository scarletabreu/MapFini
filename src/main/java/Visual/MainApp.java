package Visual;

//import DataBase.FirebaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/NodeMap/MapDashboard.fxml"));

//        FirebaseInitializer.getInstance().getFirestore().collection("User").document().get();
        AnchorPane root = loader.load();

        // Create the Scene
        Scene scene = new Scene(root);

        // Set up the Stage
        primaryStage.setTitle("Map Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // Start the JavaFX application
    }
}
