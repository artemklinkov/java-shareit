package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.BadDataException;
import ru.practicum.shareit.exception.ConflictDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(user -> toUserDto(user))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %s не найден", id)));

        return toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        checkEmail(userDto, false);
        User user = toUser(userDto);

        return toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, Long id) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Невозможно обновить данные пользователя. " +
                        "Не найден пользователь с id: " + id));
        Optional.ofNullable(userDto.getEmail()).ifPresent(updatedUser::setEmail);
        Optional.ofNullable(userDto.getName()).ifPresent(updatedUser::setName);

        return toUserDto(userRepository.save(updatedUser));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    void checkEmail(UserDto userDto, boolean isUpdate) {
        if (!isUpdate && userDto.getEmail() == null) {
            throw new BadDataException("Не указан email пользователя");
        }
        List<User> users = userRepository.findAll();
        users.forEach(checkedUser -> {
            if (checkedUser.getEmail().equals(userDto.getEmail())) {
                throw new ConflictDataException(
                        String.format("Пользователь с e-mail %s уже зарегистрирован", userDto.getEmail()));
            }
        });
    }
}
