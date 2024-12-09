package backend.Classes;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String id;
    private String username;
    private String password;
    private String email;
    private List<String> maps;

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
        this.maps = new ArrayList<>();
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

    public List<String> getMaps() {
        return maps;
    }

    public void setMaps(List<String> maps) {
        this.maps = maps;
    }

    public void addMap(String mapId) {
        if (this.maps == null) {
            this.maps = new ArrayList<>();  // Asegura que la lista no sea nula
        }
        this.maps.add(mapId);  // Agrega el ID del mapa a la lista
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