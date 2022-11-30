package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTests {
    @Autowired
    private UserController userController;

    private UserDto user = UserDto.builder()
            .id(1L)
            .name("name")
            .email("user@email.com")
            .build();

    @Test
    void createTest() {
        UserDto userDto = userController.create(user);
        assertEquals(userDto.getId(), userController.getById(userDto.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.create(user);
        UserDto userDto = user.builder()
                .id(1L)
                .email("update@email.com")
                .build();
        userController.update(userDto, 1L);
        assertEquals(userDto.getEmail(), userController.getById(1L).getEmail());
    }

    @Test
    void updateFailUserIdTest() {
        userController.create(user);
        UserDto userDto = user.builder()
                .id(1L)
                .email("update@email.com")
                .build();
        assertThrows(NotFoundException.class, () -> userController.update(userDto, 2L));
    }

    @Test
    void deleteTest() {
        UserDto userDto = userController.create(user);
        assertEquals(userController.getAll().size(), 1);
        userController.delete(userDto.getId());
        assertEquals(0, userController.getAll().size());
    }
}