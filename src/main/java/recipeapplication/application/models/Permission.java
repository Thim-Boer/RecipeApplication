package recipeapplication.application.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    USER_READ("management:read"),
    USER_UPDATE("management:update"),
    USER_CREATE("management:create"),
    USER_DELETE("management:delete");

    @Getter
    private final String permission;
}
