import Visual.Login;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        File file = new File("C:\\Users\\Scarlet\\Downloads\\TheMap.png");

        // Convert the file path to a URI
        URI uri = file.toURI();

        // Convert the URI to a URL
        URL url = null;
        try {
            url = uri.toURL();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        assert url != null;
        primaryStage.getIcons().add(new javafx.scene.image.Image(url.toString()));
        Login login = new Login();
        login.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
