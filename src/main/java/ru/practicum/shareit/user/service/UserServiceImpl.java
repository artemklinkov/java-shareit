package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadDataException;
import ru.practicum.shareit.exception.ConflictDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream().map(user -> toUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %s не найден", id)));

        return Optional.of(toUserDto(user));
    }

    @Override
    public UserDto create(User user) {
        checkEmail(user, false);

        return toUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(User user, Long id) {
        checkEmail(user, true);

        return toUserDto(userRepository.update(user, id));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    void checkEmail(User user, boolean isUpdate) {
        if (!isUpdate && user.getEmail() == null) {
            throw new BadDataException("Не указан email пользователя");
        }
        List<User> users = userRepository.getAll();
        users.forEach(checkedUser -> {
            if (checkedUser.getEmail().equals(user.getEmail())) {
                throw new ConflictDataException(
                        String.format("Пользователь с e-mail %s уже зарегистрирован", user.getEmail()));
            }
        });
    }
}
