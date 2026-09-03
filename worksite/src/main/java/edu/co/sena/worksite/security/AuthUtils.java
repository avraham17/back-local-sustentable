package edu.co.sena.worksite.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    /** Devuelve el id del usuario autenticado (extraído del JWT), o null si no hay sesión. */
    public static Integer getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Integer)) {
            return null;
        }
        return (Integer) auth.getPrincipal();
    }

    /** Revisa si el usuario autenticado tiene el rol indicado (sin el prefijo "ROLE_"). */
    public static boolean tieneRol(String rol) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + rol));
    }
}
