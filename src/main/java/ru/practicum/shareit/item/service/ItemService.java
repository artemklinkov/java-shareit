package ru.practicum.shareit.item.service;

import ru.practicum.shareit.common.CommonCrudInterface;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService extends CommonCrudInterface<ItemDto, ItemDto> {
    ItemDto create(ItemDto item, Long userId);

    ItemDto update(ItemDto item, Long id, Long userId);

    List<ItemDto> getAll(Long userId);

    List<ItemDto> search(String text);

}
