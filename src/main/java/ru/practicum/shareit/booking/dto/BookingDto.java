package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BookingDto {
    private Long id;

    @Future
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private BookingStatus status;

    private User booker;

    private Item item;
}