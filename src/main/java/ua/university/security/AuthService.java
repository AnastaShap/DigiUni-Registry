package ua.university.security;

import ua.university.domain.enums.Role;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthService {

    private final Map<String, User> users = new LinkedHashMap<>();

    public AuthService() {
        users.put("admin", new User("admin", "admin123", Role.ADMIN));
        users.put("manager", new User("manager", "manager123", Role.MANAGER));
        users.put("user", new User("user", "user123", Role.USER));
    }

    public User login(String login, String password) {
        User user = users.get(login);

        if (user == null) {
            throw new RuntimeException("Користувача не знайдено");
        }

        if (user.isBlocked()) {
            throw new RuntimeException("Користувач заблокований");
        }

        if (user.getPassword() == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("Неправильний пароль");
        }

        return user;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void createUser(String login, String password, Role role) {
        if (users.containsKey(login)) {
            throw new RuntimeException("Користувач уже існує");
        }
        users.put(login, new User(login, password, role));
    }

    public void blockUser(String login) {
        User user = users.get(login);
        if (user == null) {
            throw new RuntimeException("Користувача не знайдено");
        }
        user.setBlocked(true);
    }

    public void unblockUser(String login) {
        User user = users.get(login);
        if (user == null) {
            throw new RuntimeException("Користувача не знайдено");
        }
        user.setBlocked(false);
    }

    public void changeRole(String login, Role role) {
        User user = users.get(login);
        if (user == null) {
            throw new RuntimeException("Користувача не знайдено");
        }
        user.setRole(role);
    }
}