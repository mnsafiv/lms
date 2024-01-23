package ru.safonoviv.lms.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.safonoviv.lms.entities.RoleType;
import ru.safonoviv.lms.entities.User;

@Component
public class PrivilegeUtil {
    public boolean isAdminRole(UsernamePasswordAuthenticationToken token) {
        return token.getAuthorities().stream().anyMatch(t -> t.getAuthority().equals(RoleType.ADMIN.name()));
    }

    public boolean isCreator(UsernamePasswordAuthenticationToken token, User userCreated) {
        return token.getPrincipal().equals(userCreated);
    }

    public boolean isCreatorOrAdmin(UsernamePasswordAuthenticationToken token, User userCreated) {
        return token.getPrincipal().equals(userCreated) || token.getAuthorities().stream().anyMatch(t -> t.getAuthority().equals(RoleType.ADMIN.name()));
    }
}
