package backend.Mail;
/*
import java.io.File;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Welcome {
    public static void sendEmail(String to, String subject, String userName, String password) {
        final String username = "scarletabreuofc@gmail.com";  // Reemplaza con tu correo
        final String appPassword = "byrr kvau xonj utyy"; // Reemplaza con tu contraseña de aplicación

        // Construir el cuerpo del correo en HTML
        String body = """
                <h1>¡Gracias por registrarte en NodeMap!</h1>" +
                                        "<p>Hola %s,</p>" +
                                        "<p>Estamos emocionados de que te unas a nuestra comunidad. NodeMap es un innovador sistema de gestión de transporte público fundado por Scarlet Abreu e Isaac Peña el 22 de octubre de 2024. " +
                                        "Nuestra misión es hacer que la experiencia de viajar en transporte público sea más eficiente y accesible para todos. " +
                                        "A través de nuestra plataforma, podrás planificar tus rutas, recibir actualizaciones en tiempo real sobre horarios y trayectos, y mucho más.</p>" +
                                        "<p>Este proyecto no hubiera sido posible sin la inspiración y el apoyo de nuestro profesor, Freddy Peña, quien nos impulsó a transformar nuestras ideas en esta útil herramienta.</p>" +
                                        "<p>Tu contraseña es: <strong>%s</strong></p>" +
                                        "<p>Te animamos a explorar todas las funcionalidades de NodeMap y a darnos tu feedback. ¡Bienvenido a bordo!</p>" +
                                        "<p>Atentamente,<br>El equipo de NodeMap</p>" +
                                        "<img src=\\"cid:mapImage\\">"
                """ .formatted(userName, password);


        // Configuración del servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
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

            // Crea el cuerpo del mensaje
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");

            // Crea la parte de la imagen
            MimeBodyPart imagePart = new MimeBodyPart();
            String filePath = "C:/Users/Scarlet/Downloads/A - DT/MapApp/src/main/java/Photos/TheMap.png";
            imagePart.attachFile(new File(filePath));
            imagePart.setHeader("Content-ID", "<mapImage>");

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
            e.printStackTrace();
        }
    }
}*/
