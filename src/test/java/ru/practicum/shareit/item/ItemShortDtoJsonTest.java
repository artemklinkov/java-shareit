package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemShortDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemShortDtoJsonTest {

    @Autowired
    JacksonTester<ItemShortDto> json;

    @Test
    void testUserDto() throws Exception {
        ItemShortDto itemShortDto = ItemShortDto
                .builder()
                .id(1L)
                .name("item")
                .available(true)
                .description("description")
                .requestId(1L)
                .build();

        JsonContent<ItemShortDto> result = json.write(itemShortDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}
