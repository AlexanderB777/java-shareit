package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        user.setId(getNewId());
        users.add(user);
        return user;
    }

    private Long getNewId() {
        long maxId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L);
        return maxId + 1;
    }

    @Override
    public Optional<User> get(long id) {
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public void delete(long id) {
        users.removeIf(user -> user.getId() == id);
    }

    @Override
    public boolean existsEmail(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
