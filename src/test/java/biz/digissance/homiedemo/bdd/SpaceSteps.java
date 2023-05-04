package biz.digissance.homiedemo.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class SpaceSteps {

    private final TestRestTemplate restTemplate;

    public SpaceSteps(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @When("{user} creates a space with details:")
    public void creates_a_space_with_details(UserDto user, SpaceDto space) {
        assertThat(user).isNotNull();
        assertThat(space).isNotNull();
        final var actual =
                restTemplate.postForObject("/spaces",
                        CreateSpaceRequest.builder()
                                .name(space.getName())
                                .description(space.getDescription())
                                .owner(user.getIdentifier())
                                .build()
                        , SpaceDto.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringFields("path")
                .isEqualTo(space);
    }

    @Then("{user} list spaces and sees:")
    public void list_spaces_and_sees(UserDto user, List<SpaceDto> spaces) {

        final var result =
                restTemplate.getForObject("/users/{id}/spaces", SpaceDto[].class, Map.of("id", user.getIdentifier()));

        assertThat(result)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("path")
                .containsExactlyInAnyOrderElementsOf(spaces);
    }
}
