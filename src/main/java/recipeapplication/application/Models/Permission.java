package recipeapplication.application.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    VIEWER_READ("management:read"),
    VIEWER_UPDATE("management:update"),
    VIEWER_CREATE("management:create"),
    VIEWER_DELETE("management:delete");

    @Getter
    private final String permission;
}
