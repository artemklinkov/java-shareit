package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTests {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    private ItemDto itemDto = ItemDto.builder()
            .name("name")
            .description("description")
            .available(true)
            .build();

    private UserDto userDto = UserDto.builder()
            .name("name")
            .email("user@email.com")
            .build();

    @Test
    void createTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(itemDto, 1L);
        assertEquals(item.getId(), itemController.getById(item.getId(), user.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.create(userDto);
        itemController.create(itemDto, 1L);
        ItemDto item = ItemDto.builder().description("updateDescription").build();
        itemController.update(item, 1L, 1L);
        assertEquals(item.getDescription(), itemController.getById(1L, 1L).getDescription());
    }

    @Test
    void deleteTest() {
        userController.create(userDto);
        itemController.create(itemDto, 1L);
        assertEquals(1, itemController.getAll(1L, 0, 1).size());
        itemController.delete(1L);
        assertEquals(0, itemController.getAll(1L, 0, 1).size());
    }

    @Test
    void searchTest() {
        userController.create(userDto);
        itemController.create(itemDto, 1L);
        assertEquals(1, itemController.search("Desc", 0, 1).size());
    }

    @Test
    void searchEmptyTest() {
        userController.create(userDto);
        itemController.create(itemDto, 1L);
        assertEquals(0, itemController.search("", 0, 1).size());
    }

    @Test
    void createCommentTest() {
        CommentDto comment = CommentDto.builder().text("first comment").build();
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(itemDto, user.getId());
        UserDto user2 = userController.create(UserDto.builder().name("name2").email("email2@mail.com").build());
        bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 20, 12, 15))
                .end(LocalDateTime.of(2022, 10, 27, 12, 15))
                .itemId(item.getId()).build(), user2.getId());
        bookingController.approve(1L, 1L, true);
        itemController.createComment(item.getId(), user2.getId(), comment);
        assertEquals(1, itemController.getById(1L, 1L).getComments().size());
    }
}