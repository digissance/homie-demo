package biz.digissance.homiedemo.bdd.steps;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import biz.digissance.homiedemo.bdd.requests.ItemRequest;
import biz.digissance.homiedemo.bdd.requests.RoomRequest;
import biz.digissance.homiedemo.bdd.requests.StorageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

public class PermissionSteps {

    private final MyCache cache;
    private final TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PermissionSteps(final MyCache cache,
                           final TestRestTemplate restTemplate,
                           final ObjectMapper objectMapper) {
        this.cache = cache;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @And("user creates following rooms in space {string} and permission is denied:")
    public void userCreatesFollowingRoomsInSpaceAndPermissionIsDenied(String spaceName, RoomRequest room) {
        final var space = cache.getSpace(spaceName);
        final var actual = room.createWithResponse(space);
        assertThat(actual.getStatusCode().is4xxClientError()).isTrue();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(403));
    }

    @And("user creates following storage in room {string} and permission is denied:")
    public void userCreatesFollowingStorageInAndPermissionIsDenied(String roomName, StorageRequest request) {
        final var room = cache.findRoomByName(roomName);
        final var actual = request.createWithResponse(room);
        assertThat(actual.getStatusCode().is4xxClientError()).isTrue();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(403));
    }

    @And("user creates following item in {string} and permission is denied:")
    public void userCreatesFollowingItemInAndPermissionIsDenied(String roomName, ItemRequest request) {
        final var room = cache.findRoomByName(roomName);
        final var actual = request.createWithResponse(room);
        assertThat(actual.getStatusCode().is4xxClientError()).isTrue();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(403));
    }

    @When("user deletes room {string} and permission is denied")
    public void user_deletes_room(String roomName) {
        final var actual = restTemplate.exchange("/rooms/{id}", HttpMethod.DELETE, null, Void.class,
                Map.of("id", cache.findRoomByName(roomName).getId()));
        assertThat(actual.getStatusCode().is4xxClientError()).isTrue();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(403));
    }

    @And("user deletes storage {string} and permission is denied")
    public void userDeletesStorage(String storageName) {
        final var actual = restTemplate.exchange("/storage/{id}", HttpMethod.DELETE, null, Void.class,
                Map.of("id", cache.findRoomByName(storageName).getId()));
        assertThat(actual.getStatusCode().is4xxClientError()).isTrue();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(403));
    }

    @And("user deletes item {string} and permission is denied")
    public void userDeletesItem(String itemName) {
        final var actual = restTemplate.exchange("/items/{id}", HttpMethod.DELETE, null, Void.class,
                Map.of("id", cache.findItemByName(itemName).getId()));
        assertThat(actual.getStatusCode().is4xxClientError()).isTrue();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(403));
    }
}