package Visual.Classes;
/*
import Mail.EmailService;

import java.util.Random;

public class PasswordReset {
    private String username;
    private String resetCode;
    private EmailService emailService;

    public PasswordReset(String username) {
        this.username = username;
        this.resetCode = generateResetCode();
        this.emailService = new EmailService(); // Inicializa EmailService
    }

    private String generateResetCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    public String getUsername() {
        return username;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void sendResetEmail() {
        emailService.sendResetEmail(username, resetCode); // Envía el correo con el código de recuperación
    }
}*/
