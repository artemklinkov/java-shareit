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
        assertThrows(NotFoundException.class, () -> itemRequestController.create(itemRequestDto, 1L));
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.create(userDto);
        ItemRequestDto itemRequest = itemRequestController.create(itemRequestDto, user.getId());
        assertEquals(1, itemRequestController.getAllByUser(user.getId()).size());
    }

    @Test
    void getAllByUserWithWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemRequestController.getAllByUser(1L));
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
        assertThrows(NotFoundException.class, () -> itemRequestController.getAll(0, 10, 1L));
    }

    @Test
    void getAllWithWrongFrom() {
        assertThrows(BadDataException.class, () -> itemRequestController.getAll(-1, 10, 1L));
    }
}
