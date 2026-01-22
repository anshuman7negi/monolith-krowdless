package com.krowdless.usersmangement.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.krowdless.usersmangement.entity.UserEntity;

public final class SecurityUtil {

    private SecurityUtil() {
        // utility class → no object
    }

    // 🔐 logged-in user
    public static UserEntity getCurrentUser() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        Object principal = auth.getPrincipal();

        if (!(principal instanceof UserEntity)) {
            throw new RuntimeException("Invalid authentication principal");
        }

        return (UserEntity) principal;
    }

    // 🔐 logged-in user id
    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    // 🔐 logged-in username/email (future use)
    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }
}
