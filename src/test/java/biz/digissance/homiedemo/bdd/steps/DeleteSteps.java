package biz.digissance.homiedemo.bdd.steps;

import biz.digissance.homiedemo.bdd.requests.RoomRequest;
import biz.digissance.homiedemo.bdd.requests.StorageRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.SomethingHoldingElements;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        final var jsonResource = new ClassPathResource("/json/my_home.json");
        final var spaceDto = objectMapper.readValue(jsonResource.getInputStream(), SpaceDto.class);
        spaceDto.visit(new Consumer<>() {
            private ElementDto currentParent;
            private final Deque<ElementDto> parentStack = new ArrayDeque<>();

            @Override
            public void accept(final ElementDto elementDto) {

                if (elementDto instanceof SpaceDto space) {
                    final var spaceDto = ElementType.SPACE.getRequest(space).create();
                    cache.setSpaces((SpaceDto) spaceDto);
                }
                if (elementDto instanceof RoomDto room) {
                    final var space = (SpaceDto) cache.findElementByName(currentParent.getName()).orElseThrow();
                    ((RoomRequest) ElementType.ROOM.getRequest(room)).create(space);
                }
                if (elementDto instanceof StorageDto storage) {
                    final var parent =
                            (RoomOrStorageDto) cache.findElementByName(currentParent.getName()).orElseThrow();
                    ((StorageRequest) ElementType.STORAGE.getRequest(storage)).create(parent);
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
        final var response = restTemplate.exchange("/storage/{id}", HttpMethod.DELETE, null, Void.class,
                Map.of("id", cache.findItemByName(itemName).getId()));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @When("user deletes space {string}")
    public void user_deletes_space(String spaceName) {
        final var response = restTemplate.exchange("/spaces/{id}", HttpMethod.DELETE, null, Void.class,
                Map.of("id", cache.findElementByName(spaceName).orElseThrow().getId()));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}