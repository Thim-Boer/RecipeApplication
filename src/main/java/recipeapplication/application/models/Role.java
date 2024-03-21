package recipeapplication.application.models;

import static recipeapplication.application.models.Permission.ADMIN_CREATE;
import static recipeapplication.application.models.Permission.ADMIN_DELETE;
import static recipeapplication.application.models.Permission.ADMIN_READ;
import static recipeapplication.application.models.Permission.ADMIN_UPDATE;
import static recipeapplication.application.models.Permission.USER_CREATE;
import static recipeapplication.application.models.Permission.USER_DELETE;
import static recipeapplication.application.models.Permission.USER_READ;
import static recipeapplication.application.models.Permission.USER_UPDATE;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

        USER,
        ADMIN
}