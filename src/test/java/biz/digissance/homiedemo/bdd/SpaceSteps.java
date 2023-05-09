package biz.digissance.homiedemo.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

public class SpaceSteps {

    private final TestRestTemplate restTemplate;
    private final MyCache myCache;
    private List<RoomRequest> expectedRooms;
    private List<StorageRequest> expectedStorage;
    private List<ItemRequest> expectedItems;

    public SpaceSteps(final TestRestTemplate restTemplate, final MyCache myCache) {
        this.restTemplate = restTemplate;
        this.myCache = myCache;
    }

    @Given("user with name {string}")
    public void userWithNameGus(String name) {
        myCache.setCurrenUser(myCache.findUserByNameOrCreate(name));
    }

    @When("user creates a space with details:")
    public void creates_a_space_with_details(SpaceRequest spaceRequest) {
        final var user = myCache.getCurrentUser();
        assertThat(user).isNotNull();
        assertThat(spaceRequest).isNotNull();
        final var actual = spaceRequest.createSpace(user);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringFields("path", "id")
                .isEqualTo(spaceRequest.getExpected());
    }

    @Then("user list spaces and sees:")
    public void list_spaces_and_sees(List<SpaceRequest> spaces) {
        final var user = myCache.getCurrentUser();
        final var result =
                restTemplate.getForObject("/users/{id}/spaces", SpaceDto[].class, Map.of("id", user.getIdentifier()));
        myCache.setSpaces(result);
        assertThat(result)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("path", "id")
                .containsExactlyInAnyOrderElementsOf(spaces.stream()
                        .map(SpaceRequest::getExpected).collect(Collectors.toList()));
    }

    @When("user creates following rooms in space {string}:")
    public void user_creates_following_rooms_in_space(String spaceName, List<RoomRequest> rooms) {
        expectedRooms = rooms;
        final var space = myCache.getSpace(spaceName);
        rooms.forEach(room -> {
            final var actual = room.create(space);
            space.getRooms().add(actual);
        });
    }

    @Then("user list rooms of space {string} and sees the same rooms as described above")
    public void user_list_rooms_of_space_and_sees_the_same_rooms_as_described_above(String spaceName) {
        final var space = myCache.getSpace(spaceName);
        assertThat(space.getRooms().stream().map(ElementDto::getName).collect(Collectors.toList()))
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("path", "id")
                .containsExactlyInAnyOrderElementsOf(
                        expectedRooms.stream().map(RoomRequest::name).collect(Collectors.toList()));
    }

    @When("user creates following storage units in different rooms:")
    public void userCreatesFollowingStorageUnitsInDifferentRooms(List<StorageRequest> storageRequests) {
        expectedStorage = storageRequests;
        storageRequests.forEach(request -> {
            final var parent = myCache.findRoomByName(request.room());
            final var actual = request.create(parent);
            parent.getStuff().add(actual);
        });
    }

    @Then("all the storage units listed above exist in the space {string}")
    public void allTheStorageUnitsListedAboveExistInTheSpaceHome(String spaceName) {
        final var actual = new ArrayList<String>();
        myCache.getSpace(spaceName)
                .visit(getTraverser(elementDto -> {
                    if (elementDto instanceof StorageDto rs) {
                        actual.add(elementDto.getName());
                    }
                }));
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("room")
                .containsExactlyInAnyOrderElementsOf(expectedStorage.stream()
                        .map(StorageRequest::name).collect(Collectors.toList()));
    }

    @When("user creates following items in different storage units:")
    public void user_creates_following_items_in_different_storage_units(List<ItemRequest> itemRequests) {
        expectedItems = itemRequests;
        itemRequests.forEach(s -> {
            final var parent = myCache.findRoomByName(s.parent());
            final var actual = s.create(parent);
            parent.getStuff().add(actual);
        });
    }

    @Then("all the items listed above exist in the space {string}")
    public void allTheItemsListedAboveExistInTheSpaceHome(String spaceName) {
        final var actual = new ArrayList<String>();
        myCache.getSpace(spaceName)
                .visit(getTraverser(elementDto -> {
                    if (elementDto instanceof ItemDto rs) {
                        actual.add(elementDto.getName());
                    }
                }));
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("parent")
                .containsExactlyInAnyOrderElementsOf(expectedItems.stream()
                        .map(ItemRequest::name)
                        .collect(Collectors.toList()));
    }

    @When("user logs in")
    public void userLogsIn() {
        final var user = myCache.getCurrentUser();
        restTemplate.getRestTemplate().getInterceptors()
                .add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        final var response = restTemplate.exchange("/api/auth/token", HttpMethod.POST, null, Void.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        restTemplate.getRestTemplate().getInterceptors()
                .add(new ClientHttpRequestInterceptor() {
                    @Override
                    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
                                                        final ClientHttpRequestExecution execution) throws IOException {
                        final var res = response;
                        HttpHeaders headers = request.getHeaders();
                        res.getHeaders().get("Set-Cookie").forEach(c -> {
//                            final var cookieParts = c.split("=");
                            headers.add(HttpHeaders.COOKIE, c);
                        });
                        return execution.execute(request, body);
                    }
                });
        restTemplate.getRestTemplate().getInterceptors().removeIf(p -> p instanceof BasicAuthenticationInterceptor);
    }

    private Consumer<ElementDto> getTraverser(Consumer<ElementDto> doYourThing) {
        return new ElementDtoVisitor(doYourThing);
    }
}
