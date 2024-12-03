import backend.Mail.Welcome;

public class Prueba {
    public static void main(String[] args) {
        String to = "xionilcaalquila@gmail.com";
        String subject = "Bienvenido a NodeMap";
        String userName = "Scarlet";
        String password = "1234abcd";

        Welcome.sendEmail(to, subject, userName, password);
    }
}
