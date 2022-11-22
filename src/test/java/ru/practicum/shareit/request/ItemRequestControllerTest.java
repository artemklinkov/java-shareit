package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.BadDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestControllerTest {
    @Autowired
    private ItemRequestController itemRequestController;

    @Autowired
    private UserController userController;

    private ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("description")
            .build();

    private UserDto userDto = UserDto.builder()
            .name("name")
            .email("user@email.com")
            .build();

    @Test
    void createTest() {
        UserDto user = userController.create(userDto);
        ItemRequestDto item = itemRequestController.create(itemRequestDto, 1L);
        assertEquals(item.getId(), itemRequestController.getById(item.getId(), user.getId()).getId());
    }

    @Test
    void createWithWrongUserTest() {
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> itemRequestController.create(itemRequestDto, 1L));
        assertEquals("Невозможно создать запрос - не найден пользователь с id 1", thrown.getMessage());
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(itemRequestDto, user.getId());
        assertEquals(1, itemRequestController.getAllByUser(user.getId()).size());
    }

    @Test
    void getAllByUserWithWrongUserTest() {
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> itemRequestController.getAllByUser(1L));
        assertEquals("Невозможно найти запросы пользователя - не найден пользователь с id 1",
                thrown.getMessage());
    }

    @Test
    void getAll() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(itemRequestDto, user.getId());
        assertEquals(0, itemRequestController.getAll(0, 10, user.getId()).size());
        UserDto user2 = userController.create(userDto.builder().name("name1").email("user1@email.com").build());
        assertEquals(1, itemRequestController.getAll(0, 10, user2.getId()).size());
    }

    @Test
    void getAllByWrongUser() {
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> itemRequestController.getAll(0, 10, 1L));
        assertEquals("Невозможно найти запросы - не найден пользователь с id 1",
                thrown.getMessage());
    }

    @Test
    void getAllWithWrongFrom() {
        BadDataException thrown = assertThrows(BadDataException.class,
                () -> itemRequestController.getAll(-1, 10, 1L));
        assertEquals("Невозможно найти запросы - неккоректно переданы параметры поиска " +
                        "- индекс первого элемента не может быть меньше нуля, " +
                        "а размер страницы должен быть больше нуля",
                thrown.getMessage());
    }
}
