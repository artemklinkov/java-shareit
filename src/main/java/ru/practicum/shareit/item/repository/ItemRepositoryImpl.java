package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.generator.IdGenerator;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Item create(Item item) {
        item.setId(idGenerator.getNewId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item, Long id) {
        items.put(id, item);
        return item;
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }

    @Override
    public List<Item> getAll() {
        return items.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getById(Long id) {
        return Optional.ofNullable(items.get(id));
    }
}
