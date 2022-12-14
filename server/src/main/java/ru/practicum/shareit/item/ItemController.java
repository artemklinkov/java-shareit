package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadDataException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.create(item, userId);
    }

    @PatchMapping(path = "/{id}")
    public ItemDto update(@RequestBody ItemDto item,
                          @PathVariable Long id,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.update(item, id, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size <= 0) {
            throw new BadDataException("Невозможно найти вещи - " +
                    "неккоректно переданы параметры поиска - индекс первого элемента не может быть меньше нуля, " +
                    "а размер страницы должен быть больше нуля");
        }
        return itemService.getAll(userId, from, size);
    }

    @GetMapping(path = "/{id}")
    public ItemDto getById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getById(id, userId);
    }

    @GetMapping(path = "/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size <= 0) {
            throw new BadDataException("Невозможно найти вещи - " +
                    "неккоректно переданы параметры поиска - индекс первого элемента не может быть меньше нуля, " +
                    "а размер страницы должен быть больше нуля");
        }
        return itemService.search(text, from, size);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
