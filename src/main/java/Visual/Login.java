package Visual;

import DataBase.*;
import DataBase.FirebaseService;
import backend.Classes.User;
import backend.Mail.Welcome;
//import com.google.firebase.internal.FirebaseService;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Login extends Application {
    private StackPane mainContainer;
    private final UserDB userDB = new UserDB();

    @Override
    public void start(Stage primaryStage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.getIcons().add(new Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/TheMap.png"));

        // Crea el contenedor principal y establece el fondo de gradiente
        mainContainer = new StackPane();
        addGradientBackground(screenBounds);

        // Carga el formulario de inicio de sesión al iniciar
        mainContainer.getChildren().add(createLoginContainer(screenBounds));

        Scene scene = new Scene(mainContainer);
        primaryStage.setTitle("NodeMap Login");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    // Método para crear y agregar el fondo de gradiente al contenedor principal
    private void addGradientBackground(Rectangle2D screenBounds) {
        Rectangle gradientBackground = new Rectangle();
        gradientBackground.setWidth(screenBounds.getWidth());
        gradientBackground.setHeight(screenBounds.getHeight());

        gradientBackground.setFill(new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.REPEAT,
                new Stop(0, Color.web("#302836")),
                new Stop(0.875, Color.web("#AA7CFB"))
        ));

        mainContainer.getChildren().add(0, gradientBackground); // Añadir al fondo
    }

    public VBox createLoginContainer(Rectangle2D screenBounds) {
        VBox container = new VBox(30);
        container.setMaxWidth(screenBounds.getWidth() * 0.35);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));
        container.setStyle(
                "-fx-background-color: rgba(48, 40, 54, 0.9);" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 20, 0, 0, 10);" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: rgba(48, 40, 54, 0.5);" +
                        "-fx-border-width: 2;"
        );

        // Logo section
        Image logo = new Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/TheMap.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(150);
        logoView.setFitHeight(150);
        logoView.setPreserveRatio(true);

        VBox logoContainer = new VBox(10);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 20, 0));
        logoContainer.getChildren().addAll(logoView);

        Label titleLabel = new Label("NodeMap");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40)); //Cambiar a Spicy Rice
        titleLabel.setStyle("-fx-text-fill: #FFFFFF;");

        TextField usernameField = createField("Username");
        StackPane passwordField = createPasswordField("Password");
        CheckBox rememberMe = new CheckBox("Remember me");
        rememberMe.setStyle("-fx-text-fill: #FFFFFF;");
        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        forgotPassword.setOnAction(e -> System.out.println("Forgot password clicked"));

        HBox rememberForgotContainer = new HBox(10);
        rememberForgotContainer.setAlignment(Pos.CENTER);
        rememberForgotContainer.getChildren().addAll(rememberMe, forgotPassword);

        Label questionLabel = new Label("Don't have an account?");
        questionLabel.setTextFill(Color.WHITE);
        Hyperlink signUpLink = new Hyperlink("Sign up");
        signUpLink.setOnAction(e -> showSignUpForm(screenBounds));

        // HBox para alinear los elementos horizontalmente
        HBox signUpContainer = new HBox(10);
        signUpContainer.setAlignment(Pos.CENTER);
        signUpContainer.getChildren().addAll(questionLabel, signUpLink);

        Button loginButton = new Button("Login");
        loginButton.setPrefHeight(50);
        loginButton.setPrefWidth(300);
        loginButton.setStyle(
                "-fx-background-color: #AA7CFB;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 16px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(140,82,242,0.3), 10, 0, 0, 4);"
        );
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = ((PasswordField) ((StackPane) passwordField).getChildren().get(1)).getText();

            UserDB userDB = new UserDB();

            if (userDB.checkLogin(username, password)) {
                System.out.println("Login exitoso.");
                Stage dashboardStage = new Stage();
                MainDashboard.showDashboard(dashboardStage);
                ((Stage) container.getScene().getWindow()).close();
            } else {
                System.out.println("Nombre de usuario o contraseña incorrectos.");
            }
        });

        container.getChildren().addAll(
                logoContainer, titleLabel, usernameField, passwordField, rememberForgotContainer, loginButton, signUpContainer
        );

        return container;
    }

    private void showSignUpForm(Rectangle2D screenBounds) {
        VBox signUpContainer = new VBox(30);
        signUpContainer.setMaxWidth(screenBounds.getWidth() * 0.35);
        signUpContainer.setAlignment(Pos.CENTER);
        signUpContainer.setPadding(new Insets(40));
        signUpContainer.setStyle(
                "-fx-background-color: rgba(48, 40, 54, 0.9);" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 20, 0, 0, 10);" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: rgba(48, 40, 54, 0.5);" +
                        "-fx-border-width: 2;"
        );

        // Logo section
        Image logo = new Image("file:/C:/Users/Scarlet/Downloads/A%20-%20DT/MapApp/src/main/java/Photos/TheMap.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(150);
        logoView.setFitHeight(150);
        logoView.setPreserveRatio(true);


        VBox logoContainer = new VBox(10);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 20, 0));
        logoContainer.getChildren().addAll(logoView);

        // Título
        Label titleLabel = new Label("NodeMap");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        titleLabel.setStyle("-fx-text-fill: #FFFFFF;");

        // Campos del formulario de registro
        TextField emailField = createField("Email");
        TextField usernameField = createField("Username");
        StackPane passwordField = createPasswordField("Password"); // Cambia a PasswordField
        Button signUpButton = new Button("Sign Up");

        Label questionLabel = new Label("Have an account?");
        questionLabel.setTextFill(Color.WHITE);
        Hyperlink loginLink = new Hyperlink("Login");
        loginLink.setOnAction(e -> showLoginForm(screenBounds));

        // HBox para alinear los elementos horizontalmente
        HBox loginContainer = new HBox(10);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.getChildren().addAll(questionLabel, loginLink);

        signUpButton.setPrefHeight(50);
        signUpButton.setPrefWidth(300);
        signUpButton.setStyle(
                "-fx-background-color: #AA7CFB;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 16px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(140,82,242,0.3), 10, 0, 0, 4);"
        );

        signUpButton.setOnAction(e -> {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = ((PasswordField) ((StackPane) passwordField).getChildren().get(1)).getText();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                System.out.println("Todos los campos son obligatorios.");
                return;
            }

            String userId = FirebaseService.generateId();
            User newUser = new User(userId, username, password, email);

            // Guarda en Firestore
            if (userDB.create(newUser)) {
                System.out.println("Usuario registrado con éxito.");

                String subject = "¡Bienvenido a NodeMap!";
                Welcome.sendEmail(email, subject, username, password);
                System.out.println("Correo enviado correctamente.");

                Stage dashboardStage = new Stage();
                MainDashboard.showDashboard(dashboardStage);

                ((Stage) signUpContainer.getScene().getWindow()).close();
            } else {
                System.out.println("Error al registrar el usuario.");
            }
        });

        // Agrega los elementos al contenedor de registro
        signUpContainer.getChildren().addAll(logoContainer, titleLabel, emailField, usernameField, passwordField, signUpButton,
                loginContainer);

        // Actualiza el contenedor principal
        mainContainer.getChildren().clear();
        addGradientBackground(screenBounds);
        mainContainer.getChildren().add(signUpContainer);
    }


    private void showLoginForm(Rectangle2D screenBounds) {
        mainContainer.getChildren().clear(); // Limpiar el contenedor principal
        addGradientBackground(screenBounds); // Agregar el fondo de gradiente
        mainContainer.getChildren().add(createLoginContainer(screenBounds)); // Agregar el contenedor de inicio de sesión
    }

    private TextField createField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setPrefHeight(50);
        field.setPrefWidth(300);
        field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 0 20 0 20;" +
                        "-fx-font-size: 14px;"
        );
        return field;
    }

    private StackPane createPasswordField(String promptText) {
        // Crea el campo de contraseña
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        passwordField.setPrefHeight(50);
        passwordField.setPrefWidth(300);
        passwordField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 0 20 0 20;" +
                        "-fx-font-size: 14px;"
        );

        // Campo de texto alternativo para mostrar la contraseña sin encriptar
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefHeight(50);
        textField.setPrefWidth(300);
        textField.setStyle(passwordField.getStyle());
        textField.setVisible(false);  // Inicialmente oculto

        // Sincroniza el contenido entre passwordField y textField
        passwordField.textProperty().addListener((obs, oldText, newText) -> textField.setText(newText));
        textField.textProperty().addListener((obs, oldText, newText) -> passwordField.setText(newText));

        // Crea el botón para mostrar/ocultar la contraseña
        Button toggleButton = new Button("👁");
        toggleButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        toggleButton.setOnAction(e -> {
            boolean isPasswordVisible = textField.isVisible();
            textField.setVisible(!isPasswordVisible);
            passwordField.setVisible(isPasswordVisible);
        });

        // Coloca el botón dentro de un contenedor para que se vea al lado derecho
        StackPane buttonContainer = new StackPane(toggleButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setMaxHeight(50);
        buttonContainer.setMaxWidth(50);

        // Contenedor que combina el PasswordField/TextField y el botón de alternar
        StackPane container = new StackPane();
        container.getChildren().addAll(textField, passwordField, buttonContainer);
        StackPane.setAlignment(buttonContainer, Pos.CENTER_RIGHT); // Alinea el botón a la derecha
        StackPane.setMargin(buttonContainer, new Insets(0, 10, 0, 0)); // Margen derecho para el botón

        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
