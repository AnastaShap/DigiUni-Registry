package ua.university.security;

import ua.university.domain.Person;
import ua.university.domain.enums.Role;
import ua.university.util.Logging.ILogger;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class AuthService {

    private final Map<String, User> users = new LinkedHashMap<>();
    private Person currentUser;

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


    //public void login(Person person) { this.currentUser = person; }
    public Person getCurrentUser() { return currentUser; }

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

    // REFLECTIONS USAGE
    public boolean canExecute(Object target, String methodName, Person user) {
        try {
            Method method = target.getClass().getMethod(methodName);

            // Перевіряємо, чи є над методом наша анотація
            if (method.isAnnotationPresent(RequiresPermission.class)) {
                RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);
                int requiredBit = annotation.value();

                // Перевіряємо бітову маску користувача
                return user.hasPermission(requiredBit);
            }
        } catch (NoSuchMethodException e) {
           // return e.getMessage();
            return false;

        }
        return true;
    }

    public void assignPermissions(Person person, Role role) {
        int mask = switch (role) {
            case ADMIN -> Permissions.ADMIN_FULL | Permissions.DELETE_DATA | Permissions.EDIT_DATA | Permissions.VIEW_ALL;
            case MANAGER -> Permissions.EDIT_DATA | Permissions.VIEW_ALL;
            case USER -> Permissions.VIEW_ALL;
            default -> 0;
        };
        person.addPermissions(mask);
    }


    public boolean checkAccess(Object menu, String methodName, ILogger logger) {
        Person user = getCurrentUser();
        if (!canExecute(menu, methodName, user)) {
            logger.info("Access Denied: You don't have the required permission for " + methodName);
            return false;
        }
        return true;
    }
}