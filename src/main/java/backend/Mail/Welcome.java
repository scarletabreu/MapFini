package backend.Mail;

import Visual.MainDashboard;
import java.nio.file.Files;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class Welcome {
    public static void sendEmail(String to, String subject, String userName, String password) {
        final String username = "scarletabreuofc@gmail.com";
        final String appPassword = "byrr kvau xonj utyy";

        String body = """
                <h1>¡Gracias por registrarte en NodeMap!</h1>
                <p>Hola %s,</p>
                <p>Estamos emocionados de que te unas a nuestra comunidad. NodeMap es un innovador sistema de gestión de transporte público fundado por Scarlet Abreu e Isaac Peña el 22 de octubre de 2024.\s
                Nuestra misión es hacer que la experiencia de viajar en transporte público sea más eficiente y accesible para todos.\s
                A través de nuestra plataforma, podrás planificar tus rutas, recibir actualizaciones en tiempo real sobre horarios y trayectos, y mucho más.</p>
                <p>Este proyecto no hubiera sido posible sin la inspiración y el apoyo de nuestro profesor, Freddy Peña, quien nos impulsó a transformar nuestras ideas en esta útil herramienta.</p>
                <p>Tu contraseña es: <strong>%s</strong></p>
                <p>Te animamos a explorar todas las funcionalidades de NodeMap y a darnos tu feedback. ¡Bienvenido a bordo!</p>
                <p>Atentamente,<br>El equipo de NodeMap</p>
                <img src="cid:mapImage">
               \s""".formatted(userName, password);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            // Crea un nuevo mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Crea la parte del cuerpo del mensaje en HTML
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");

            // Crea la parte de la imagen adjunta
            InputStream resourceStream = MainDashboard.class.getResourceAsStream("/Photos/TheMap.png");
            if (resourceStream == null) {
                throw new IllegalArgumentException("Resource not found: /Photos/TheMap.png");
            }

            // Create a temporary file
            File tempFile = File.createTempFile("TheMap", ".png");
            tempFile.deleteOnExit(); // Ensure the file is deleted on JVM exit

            // Copy resource to the temporary file
            Files.copy(resourceStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Create the MimeBodyPart
            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(tempFile);
            imagePart.setDataHandler(new DataHandler(dataSource));
            imagePart.setContentID("<mapImage>");
            imagePart.setDisposition(MimeBodyPart.INLINE);

            // Combina las partes en un multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(imagePart);

            // Establece el contenido del mensaje
            message.setContent(multipart);

            // Envía el mensaje
            Transport.send(message);
            System.out.println("Email enviado correctamente!");

        } catch (MessagingException | java.io.IOException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
