package backend.Classes;

import com.google.cloud.firestore.annotation.DocumentId;

public class User {
    @DocumentId
    private String id;
    private String username;
    private String password;
    private String email;


    public User(String id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(){
        this.id = "";
        this.username = "";
        this.password = "";
        this.email = "";
    }

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public void setUsername(String user) {
        this.username = user;
    }
}
