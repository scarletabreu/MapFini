package backend.Mail;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailService {
    private final String username = "scarletabreuofc@gmail.com";
    private final String appPassword = "byrr kvau xonj utyy";

    public void sendEmail(String to, String subject, String body) {
        // Configuración del servidor SMTP
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
            // Crear el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Enviar el correo
            Transport.send(message);
            System.out.println("Email enviado correctamente!");

        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendResetEmail(String recipient, String resetCode) {
        String subject = "Código de Recuperación de Contraseña";
        String body = String.format(
                "Hola %s,\n\n" +
                        "Has solicitado restablecer tu contraseña. Tu código de recuperación es: %s\n\n" +
                        "Por favor, ingresa este código en la aplicación para continuar con el restablecimiento de tu contraseña.\n\n" +
                        "Atentamente,\n" +
                        "El equipo de NodeMap", recipient, resetCode
        );

        sendEmail(recipient, subject, body);
    }
}
