package ua.university.security;

import ua.university.domain.enums.Role;
import ua.university.exception.AccessDeniedException;

import java.util.Set;

public class AccessManager {

    public void requireAnyRole(User user, Set<Role> allowedRoles) {
        if (user == null) {
            throw new AccessDeniedException("User is not authenticated.");
        }

        if (!allowedRoles.contains(user.getRole())) {
            throw new AccessDeniedException(
                    "Assess to the role " + user.getRole() +
                            ". Is allowed: " + allowedRoles
            );
        }
    }
}