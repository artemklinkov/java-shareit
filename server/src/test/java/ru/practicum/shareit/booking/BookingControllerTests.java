package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadDataException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTests {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    private ItemDto itemDto = ItemDto.builder()
            .name("name")
            .description("description")
            .available(true)
            .build();

    private UserDto userDto = UserDto.builder()
            .name("name")
            .email("user@email.com")
            .build();

    private UserDto userDto1 = UserDto.builder()
            .name("name")
            .email("user1@email.com")
            .build();

    private BookingShortDto bookingShortDto = BookingShortDto.builder()
            .start(LocalDateTime.of(2022, 10, 24, 12, 30))
            .end(LocalDateTime.of(2023, 11, 10, 13, 0))
            .itemId(1L).build();

    @Test
    void createTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(itemDto, user.getId());
        UserDto user1 = userController.create(userDto1);
        BookingDto booking = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(1L, bookingController.getById(booking.getId(), user1.getId()).getId());
    }

    @Test
    void approveTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(itemDto, user.getId());
        UserDto user1 = userController.create(userDto1);
        BookingDto booking = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        assertEquals(WAITING, bookingController.getById(booking.getId(), user1.getId()).getStatus());
        bookingController.approve(booking.getId(), user.getId(), true);
        assertEquals(APPROVED, bookingController.getById(booking.getId(), user1.getId()).getStatus());
    }

    @Test
    void getAllByUserTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(itemDto, user.getId());
        UserDto user1 = userController.create(userDto1);
        BookingDto booking = bookingController.create(bookingShortDto, user1.getId());
        assertEquals(1, bookingController.getAllByUser(user1.getId(), "WAITING", 0, 10).size());
        assertEquals(1, bookingController.getAllByUser(user1.getId(), "ALL", 0, 10).size());
        assertEquals(0, bookingController.getAllByUser(user1.getId(), "PAST", 0, 10).size());
        assertEquals(1, bookingController.getAllByUser(user1.getId(), "CURRENT", 0, 10).size());
        assertEquals(0, bookingController.getAllByUser(user1.getId(), "FUTURE", 0, 10).size());
        assertEquals(0, bookingController.getAllByUser(user1.getId(), "REJECTED", 0, 10).size());
        bookingController.approve(booking.getId(), user.getId(), true);
        assertEquals(1, bookingController.getAllByOwner(user.getId(), "CURRENT", 0, 10).size());
        assertEquals(1, bookingController.getAllByOwner(user.getId(), "ALL", 0, 10).size());
        assertEquals(0, bookingController.getAllByOwner(user.getId(), "WAITING", 0, 10).size());
        assertEquals(0, bookingController.getAllByOwner(user.getId(), "FUTURE", 0, 10).size());
        assertEquals(0, bookingController.getAllByOwner(user.getId(), "REJECTED", 0, 10).size());
        assertEquals(0, bookingController.getAllByOwner(user.getId(), "PAST", 0, 10).size());
    }

    @Test
    void getAllByOwnerFailIncorrectParametersTest() {
        UserDto user = userController.create(userDto);
        assertThrows(BadDataException.class, () -> bookingController.getAllByOwner(user.getId(),
                "UNKNOWN", -1, 0));
    }

    @Test
    void getAllByOwnerFailTest() {
        UserDto user = userController.create(userDto);
        assertThrows(BadDataException.class, () -> bookingController.getAllByOwner(user.getId(),
                "UNKNOWN", 0, 1));
    }

    @Test
    void getAllByUserFailTest() {
        UserDto user = userController.create(userDto);
        assertThrows(BadDataException.class, () -> bookingController.getAllByUser(user.getId(),
                "UNKNOWN", 0, 1));
    }

    @Test
    void toBookingTest() {
        UserDto user = userController.create(userDto);
        ItemDto item = itemController.create(itemDto, user.getId());
        UserDto user1 = userController.create(userDto1);
        BookingDto bookingDto = bookingController.create(BookingShortDto.builder()
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .itemId(item.getId()).build(), user1.getId());
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 10, 24, 12, 30))
                .end(LocalDateTime.of(2022, 11, 10, 13, 0))
                .item(ItemMapper.toItem(item))
                .booker(UserMapper.toUser(user1))
                .status(WAITING)
                .build();
        assertEquals(booking, BookingMapper.toBooking(bookingDto));
    }
}