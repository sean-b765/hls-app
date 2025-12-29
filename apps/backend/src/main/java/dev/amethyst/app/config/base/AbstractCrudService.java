package dev.amethyst.app.config.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public abstract class AbstractCrudService<T extends AbstractBaseEntity, ID, R extends JpaRepository<T, ID>> {
  protected final R repository;

  protected AbstractCrudService(R repository) {
    this.repository = repository;
  }

  protected void beforeSave(T entity) {
  }

  /* CREATE & UPDATE */

  public T save(@NonNull T entity) {
    this.beforeSave(entity);
    return this.repository.save(entity);
  }

  public List<T> saveAll(List<T> entities) {
    if (entities == null)
      return new ArrayList<>();
    return this.repository.saveAll(entities);
  }

  /* READ */

  public List<T> findAll() {
    return this.repository.findAll();
  }

  public List<T> findAllByIds(@NonNull Iterable<ID> ids) {
    return this.repository.findAllById(ids);
  }

  public Optional<T> findById(@NonNull ID id) {
    return this.repository.findById(id);
  }

  public boolean exists(@NonNull ID id) {
    return this.repository.existsById(id);
  }

  /* DELETE */

  public void deleteById(@NonNull ID id) {
    this.repository.deleteById(id);
  }

  public void deleteAllById(@NonNull Iterable<ID> id) {
    this.repository.deleteAllById(id);
  }
}
