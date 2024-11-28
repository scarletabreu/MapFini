import Visual.Login;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new javafx.scene.image.Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/TheMap.png"));
        Login login = new Login();
        login.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
