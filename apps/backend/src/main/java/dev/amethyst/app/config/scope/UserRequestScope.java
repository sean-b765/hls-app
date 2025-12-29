package dev.amethyst.app.config.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import dev.amethyst.app.user.model.User;
import lombok.Data;

/**
 * For the duration of the request, this user will be available for
 * authenticated requests
 */
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@Data
public class UserRequestScope {
  private User user;
}
