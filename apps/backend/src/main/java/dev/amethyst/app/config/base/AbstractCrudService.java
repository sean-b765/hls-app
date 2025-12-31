package dev.amethyst.app.config.base;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import dev.amethyst.app.config.event.CreationEvent;
import dev.amethyst.app.config.event.DeletionEvent;
import dev.amethyst.app.config.event.UpdateEvent;

public abstract class AbstractCrudService<T extends AbstractBaseEntity, ID, R extends JpaRepository<T, ID>> {
  @Autowired
  protected R repository;
  @Autowired
  protected ApplicationEventPublisher eventPublisher;

  protected void beforeSave(T entity) {
  }

  /* CREATE & UPDATE */

  @Transactional
  public T save(@NonNull T entity) {
    boolean isInsert = entity.getId() == null;
    this.beforeSave(entity);
    T saved = this.repository.save(entity);

    if (isInsert)
      this.eventPublisher.publishEvent(new CreationEvent<T>(saved));
    else
      this.eventPublisher.publishEvent(new UpdateEvent<T>(saved));
    return saved;
  }

  @Transactional
  public List<T> saveAll(List<T> entities) {
    if (entities == null)
      return new ArrayList<>();

    // List of ids being updated. If any T entity has no id, it is a CREATION
    Set<String> updatingIds = entities.stream()
        .filter(e -> e.getId() != null)
        .map(T::getId)
        .collect(Collectors.toSet());

    List<T> saved = this.repository.saveAll(entities);

    saved.forEach(entity -> {
      if (updatingIds.contains(entity.getId())) {
        this.eventPublisher.publishEvent(new UpdateEvent<T>(entity));
      } else {
        this.eventPublisher.publishEvent(new CreationEvent<T>(entity));
      }
    });

    return saved;
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

  @Transactional
  public void deleteById(@NonNull ID id) {
    T deletable = this.findById(id).orElse(null);
    if (deletable == null)
      throw new NoSuchElementException();

    this.repository.delete(deletable);

    this.eventPublisher.publishEvent(new DeletionEvent<T>(deletable));
  }

  @Transactional
  public void deleteAllById(@NonNull Iterable<ID> ids) {
    List<T> deletable = this.findAllByIds(ids);
    if (deletable == null || deletable.isEmpty())
      throw new NoSuchElementException();

    this.repository.deleteAll(deletable);

    deletable.forEach(entity -> {
      this.eventPublisher.publishEvent(new DeletionEvent<T>(entity));
    });
  }
}
