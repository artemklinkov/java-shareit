package ru.practicum.shareit.generator;

public class IdGenerator {
    private Long id = 0l;

    public Long getNewId() {
        return ++id;
    }
}
