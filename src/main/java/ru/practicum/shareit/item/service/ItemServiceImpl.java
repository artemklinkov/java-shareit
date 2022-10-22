package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto create(ItemDto item) {
        return toItemDto(itemRepository.create(toItem(item)));
    }

    @Override
    public ItemDto create(ItemDto item, Long userId) {
        User user = userRepository.getById(userId)
                .orElseThrow(()-> new NotFoundException("Пользователь с id %s не найден".formatted(userId)));
        item.setOwner(user);
        ItemDto createdItem = create(item);
        return createdItem;
    }

    @Override
    public ItemDto update(ItemDto item, Long id) {
        return toItemDto(itemRepository.update(toItem(item), id));
    }

    @Override
    public ItemDto update(ItemDto item, Long id, Long userId) {
        User user = userRepository.getById(userId)
                .orElseThrow(()-> new NotFoundException("Пользователь с id %s не найден".formatted(userId)));
        Item updatedItem = itemRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id %s не найдена".formatted(id)));
        if (updatedItem.getOwner() != null && !updatedItem.getOwner().equals(user)) {
            throw new NotFoundException("Вещь %s не принадлежит пользователю %s".formatted(id, userId));
        }

        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        return update(toItemDto(updatedItem), id);
    }

    @Override
    public void delete(Long id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> getAll() {
        return itemRepository.getAll().stream().map(item -> toItemDto(item)).toList();
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return itemRepository.getAll()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(item -> toItemDto(item))
                .toList();
    }

    @Override
    public Optional<ItemDto> getById(Long id) {
        Item foundItem = itemRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("".formatted(id)));
        return Optional.of(toItemDto(foundItem));
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository.getAll()
                .stream()
                .filter(item -> item.getAvailable() && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(item -> toItemDto(item))
                .toList();
    }
}
