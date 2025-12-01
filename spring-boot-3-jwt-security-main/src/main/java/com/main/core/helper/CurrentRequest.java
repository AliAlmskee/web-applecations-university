package com.main.core.helper;

import com.main.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.main.core.Setup;

import java.util.Optional;

public class CurrentRequest {

    public static boolean isExist() {
        return Setup.getCurrentHttpRequest() != null;
    }

    public static User getUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return null;
        }

        return (User) authentication.getPrincipal();
    }
}
