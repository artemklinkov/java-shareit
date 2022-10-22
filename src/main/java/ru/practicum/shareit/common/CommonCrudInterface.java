package ru.practicum.shareit.common;

import java.util.List;
import java.util.Optional;

public interface CommonCrudInterface<T,I> {
    T create(I entity);
    T update(I entity, Long id);

    void delete(Long id);

    List<T> getAll();

    Optional<T> getById(Long id);
}
