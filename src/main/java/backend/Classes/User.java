package backend.Classes;

import backend.Controller.WorldMap;
import java.util.List;

public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private List<WorldMap> maps;

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
        this.maps = null;
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
