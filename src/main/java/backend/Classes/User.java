package backend.Classes;

import com.google.cloud.firestore.annotation.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class User {
    @DocumentId
    private String id;
    private String username;
    private String password;
    private String email;
    private List<Stop> stops;
    private List<Route> routes;

    public User(String id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, List<Stop> stops, List<Route> routes) {
        this.id = "";
        this.username = username;
        this.password = password;
        this.email = email;
        this.stops = new ArrayList<>();
        this.routes = new ArrayList<>();
    }

    public User(){
        this.id = "";
        this.username = "";
        this.password = "";
        this.email = "";
        this.stops = null;
        this.routes = null;
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
