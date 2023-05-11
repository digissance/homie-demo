package biz.digissance.homiedemo.bdd.steps;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.digissance.homiedemo.bdd.requests.ItemRequest;
import biz.digissance.homiedemo.bdd.requests.RoomRequest;
import biz.digissance.homiedemo.bdd.requests.SpaceRequest;
import biz.digissance.homiedemo.bdd.requests.StorageRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SomethingHoldingElements;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;

public class DeleteSteps {

    private final MyCache cache;
    private final TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DeleteSteps(final MyCache cache,
                       final TestRestTemplate restTemplate,
                       final ObjectMapper objectMapper) {
        this.cache = cache;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @And("space home with rooms and items are created")
    public void spaceHomeWithRoomsAndItems() throws IOException {
        final var currentUser = cache.getCurrentUser();
        final var jsonResource = new ClassPathResource("/json/my_home.json");
        final var spaceDto = objectMapper.readValue(jsonResource.getInputStream(), SpaceDto.class);
        spaceDto.visit(new Consumer<>() {
            private ElementDto currentParent;
            private final Deque<ElementDto> parentStack = new ArrayDeque<>();

            @Override
            public void accept(final ElementDto elementDto) {
                if (elementDto instanceof SpaceDto space) {
                    final var spaceDto = new SpaceRequest(restTemplate, space.getName(), space.getDescription())
                            .createSpace(currentUser);
                    cache.setSpaces(spaceDto);
                }
                if (elementDto instanceof RoomDto room) {
                    final var space = cache.getSpace(currentParent.getName());
                    final var roomDto = new RoomRequest(restTemplate, room.getName(), room.getDescription())
                            .create(space);
                    space.getRooms().add(roomDto);
                }
                if (elementDto instanceof StorageDto storage) {
                    final var roomByName = cache.findRoomByName(currentParent.getName());
                    final var storageDto = new StorageRequest(restTemplate, currentParent.getName(), storage.getName(),
                            storage.getDescription())
                            .create(roomByName);
                    roomByName.getStuff().add(storageDto);
                }
                if (elementDto instanceof ItemDto item) {
                    final var roomByName = cache.findRoomByName(currentParent.getName());
                    final var itemDto = new ItemRequest(restTemplate, currentParent.getName(), item.getName(),
                            item.getDescription())
                            .create(roomByName);
                    roomByName.getStuff().add(itemDto);
                }
                if (elementDto instanceof SomethingHoldingElements parent) {
                    currentParent = elementDto;
                    parentStack.push(elementDto);
                    parent.getElements().forEach(child -> child.visit(this));
                    parentStack.pop();
                    currentParent = parentStack.peek();
                }
            }
        });
    }

    @When("user deletes room {string}")
    public void user_deletes_room(String roomName) {
        final var response = restTemplate.exchange("/rooms/{id}", HttpMethod.DELETE, null, Void.class,
                Map.of("id", cache.findRoomByName(roomName).getId()));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @And("user deletes storage {string}")
    public void userDeletesStorage(String storageName) {
        final var response = restTemplate.exchange("/storage/{id}", HttpMethod.DELETE, null, Void.class,
                Map.of("id", cache.findRoomByName(storageName).getId()));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @And("user deletes item {string}")
    public void userDeletesItem(String itemName) {
        final var response = restTemplate.exchange("/items/{id}", HttpMethod.DELETE, null, Void.class,
                Map.of("id", cache.findItemByName(itemName).getId()));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}