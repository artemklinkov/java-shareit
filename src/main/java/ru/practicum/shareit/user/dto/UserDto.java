package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
public class UserDto {
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}