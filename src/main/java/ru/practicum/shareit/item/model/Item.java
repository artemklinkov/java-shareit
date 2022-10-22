package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Data
@Builder
public class Item {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    @NotNull
    private User owner;

    private ItemRequest request;
}