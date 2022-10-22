package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Setter
@Data
public class ItemRequest {
    private final long id;

    @NotBlank
    private final String description;

    @NotNull
    private final User requestor;

    private final LocalDateTime created;
}
