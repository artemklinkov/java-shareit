package ru.practicum.shareit.generator;

public class IdGenerator {
    private Long id = 0L;

    public Long getNewId() {
        return ++id;
    }
}
