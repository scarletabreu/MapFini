package Visual.Classes;

import backend.Classes.User;

import java.util.List;

public class UserService {
    private List<User> userList; // Lista que contiene todos los usuarios registrados

    public UserService(List<User> userList) {
        this.userList = userList;
    }

    public UserService() {
    }

    // Método para agregar un usuario a la lista
    public void addUser(User user) {

        userList.add(user);
    }

    // Método para obtener un usuario por su nombre de usuario
    public User getUser(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // No se encontró el usuario
    }

    // Método para verificar si un usuario existe
    public boolean userExists(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return true; // El usuario existe
            }
        }
        return false; // El usuario no existe
    }
}

