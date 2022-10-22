package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.generator.IdGenerator;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public User create(User user) {
        user.setId(idGenerator.getNewId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User user, Long id) {
        User updatedUser = getById(id).get();
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        users.put(id, updatedUser);

        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

}
