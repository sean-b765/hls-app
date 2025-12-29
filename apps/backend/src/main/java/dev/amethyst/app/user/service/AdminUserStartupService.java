package dev.amethyst.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import dev.amethyst.app.configuration.model.Configuration;
import dev.amethyst.app.configuration.service.ConfigurationService;
import dev.amethyst.app.user.model.Role;
import dev.amethyst.app.user.model.User;

@Service
public class AdminUserStartupService {
  @Value("${admin.username}")
  private String adminUsername;
  @Value("${admin.password}")
  private String adminPassword;

  @Autowired
  private ConfigurationService configurationService;
  @Autowired
  private AuthService authService;

  @EventListener(ApplicationReadyEvent.class)
  public void createAdminUser() {
    Configuration configuration = this.configurationService.getConfiguration();
    if (configuration.isAdminUserCreated())
      return;

    User adminUser = User.builder()
        .roles(List.of(Role.ALL_ROLES))
        .username(adminUsername)
        .password(adminPassword)
        .build();

    User createdAdminUser = this.authService.signup(adminUser);
    if (createdAdminUser == null)
      return;

    configuration.setAdminUserCreated(true);
    configurationService.save(configuration);
  }
}
