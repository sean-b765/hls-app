package dev.seanboaden.hls.config.base;

import java.net.URI;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public abstract class AbstractCrudController<T extends AbstractBaseEntity, ID, R extends JpaRepository<T, ID>, S extends AbstractCrudService<T, ID, R>> {
  protected S service;

  protected AbstractCrudController(S service) {
    this.service = service;
  }

  /* CREATE */

  @PostMapping
  public ResponseEntity<T> create(@RequestBody @NonNull T body) {
    this.canCreate(body);
    T response = this.service.save(body);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.getId())
        .toUri();
    return ResponseEntity.created(location).body(response);
  }

  /* READ */

  @GetMapping
  public ResponseEntity<List<T>> findAll() {
    this.canRead(null);
    return ResponseEntity.ok(this.service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<T> findById(@PathVariable @NonNull ID id) {
    this.canRead(id);
    T result = this.service.findById(id).orElse(null);
    if (result == null)
      return ResponseEntity.notFound().build();
    return ResponseEntity.ok(result);
  }

  @PostMapping("/fetch")
  public ResponseEntity<List<T>> findByIds(@RequestBody @NonNull List<ID> ids) {
    for (ID id : ids)
      this.canRead(id);
    List<T> result = this.service.findAllByIds(ids);
    return ResponseEntity.ok(result);
  }

  /* UPDATE */

  @PutMapping
  public ResponseEntity<T> upsert(@RequestBody @NonNull T body) {
    this.canUpdate(body);
    T response = this.service.save(body);
    return ResponseEntity.ok(response);
  }

  /* DELTE */

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable ID id) {
    if (id == null)
      return ResponseEntity.badRequest().build();

    this.canDelete(id);

    this.service.deleteById(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<?> deleteByIds(@RequestBody List<ID> ids) {
    if (ids == null)
      return ResponseEntity.badRequest().build();

    for (ID id : ids)
      this.canDelete(id);

    this.service.deleteAllById(ids);
    return ResponseEntity.ok().build();
  }

  /* Authorization */

  protected void canCreate(T body) {
  }

  protected void canUpdate(T body) {
  }

  protected void canRead(ID id) {
  }

  protected void canDelete(ID id) {
  }

  protected boolean hasAuthority(String authority) {
    return SecurityContextHolder.getContext()
        .getAuthentication()
        .getAuthorities()
        .stream()
        .anyMatch(a -> a.getAuthority().equals(authority));
  }
}
