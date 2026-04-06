package ua.university.security;

import ua.university.domain.enums.Role;

public class User {
    private String login;
    private String password;
    private Role role;
    private boolean blocked;

    public User(String login, String password, Role role, boolean blocked) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.blocked = blocked;
    }

    public User(String login, String password, Role role) {
        this(login, password, role, false);
    }

    public User(String login, Role role) {
        this(login, "", role, false);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", role=" + role +
                ", blocked=" + blocked +
                '}';
    }
}