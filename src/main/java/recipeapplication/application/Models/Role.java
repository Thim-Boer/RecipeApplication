package recipeapplication.application.Models;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static recipeapplication.application.Models.Permission.ADMIN_CREATE;
import static recipeapplication.application.Models.Permission.ADMIN_READ;
import static recipeapplication.application.Models.Permission.ADMIN_UPDATE;
import static recipeapplication.application.Models.Permission.ADMIN_DELETE;
import static recipeapplication.application.Models.Permission.VIEWER_READ;
import static recipeapplication.application.Models.Permission.VIEWER_UPDATE;
import static recipeapplication.application.Models.Permission.VIEWER_DELETE;
import static recipeapplication.application.Models.Permission.VIEWER_CREATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

  USER(Collections.emptySet()),
  ADMIN(
          Set.of(
                  ADMIN_READ,
                  ADMIN_UPDATE,
                  ADMIN_DELETE,
                  ADMIN_CREATE,
                  VIEWER_READ,
                  VIEWER_UPDATE,
                  VIEWER_DELETE,
                  VIEWER_CREATE
          )
  ),
  VIEWER(
          Set.of(
                  VIEWER_READ,
                  VIEWER_UPDATE,
                  VIEWER_DELETE,
                  VIEWER_CREATE
          )
  );

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}