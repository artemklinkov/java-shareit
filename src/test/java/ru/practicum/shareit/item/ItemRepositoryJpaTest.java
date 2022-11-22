package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.TypedQuery;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class ItemRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void searchTest() throws Exception {
        User owner = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRepository.save(Item.builder().name("name").description("description").available(true).owner(owner).build());
        Page<Item> items = itemRepository.search("desc", PageRequest.of(0, 1));

        TypedQuery<Item> query = em.getEntityManager().createQuery("select i from Item i " +
                "where i.available = true and upper(i.name) like upper(concat('%', :searchStr, '%')) " +
                "or  i.available = true and upper(i.description) like upper(concat('%', :searchStr, '%')) " +
                "order by i.id asc", Item.class);
        Item foundItem = query.setParameter("searchStr", "desc").getSingleResult();
        assertThat(items.stream().collect(Collectors.toList()).get(0), equalTo(foundItem));
    }
}
