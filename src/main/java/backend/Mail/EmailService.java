package backend.Mail;
/*
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    private final String username = "scarletabreuofc@gmail.com"; // Reemplaza con tu correo
    private final String appPassword = "byrr kvau xonj utyy"; // Reemplaza con tu contraseña de aplicación

    public void sendEmail(String to, String subject, String body) {
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
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email enviado correctamente!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendResetEmail(String username, String resetCode) {
        String subject = "Código de Recuperación de Contraseña";
        String body = String.format(
                "Hola %s,\n\n" +
                        "Has solicitado restablecer tu contraseña. Tu código de recuperación es: %s\n\n" +
                        "Por favor, ingresa este código en la aplicación para continuar con el restablecimiento de tu contraseña.\n\n" +
                        "Atentamente,\n" +
                        "El equipo de NodeMap", username, resetCode
        );

        sendEmail(username, subject, body); // Envía el correo con el código de recuperación
    }
}*/
