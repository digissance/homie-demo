package biz.digissance.homiedemo.bdd.steps;

import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StuffDto;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ElementPathSteps {

    private final MyCache cache;
    private final TestRestTemplate restTemplate;
    private List<ElementDto> actual;

    public ElementPathSteps(MyCache cache, TestRestTemplate restTemplate) {
        this.cache = cache;
        this.restTemplate = restTemplate;
    }

    @When("user requests the full path of the element {element}")
    public void user_requests_the_full_path_of_the_element(ElementDto element) {
        assertThat(element).isNotNull();
        final var response = restTemplate.exchange("/spaces/{id}/elements/{elementId}/path",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ElementDto>>() {
                }, Map.of("id", getSpaceId(element), "elementId", element.getId()));
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        actual = response.getBody();
    }

    private long getSpaceId(ElementDto element) {
        if (element instanceof StuffDto) {
            return ((StuffDto) element).getSpaceId();
        }
        if (element instanceof RoomDto) {
            return ((RoomDto) element).getSpaceId();
        }
        if (element instanceof SpaceDto) {
            return element.getId();
        }
        throw new IllegalArgumentException("Unknown element type: " + element.getClass());
    }

    @Then("the full path of the element is returned")
    public void the_full_path_of_the_element_is_returned(List<String> names) {
        final var expected = names.stream().map(cache::findElementByName).map(Optional::get)
                .collect(Collectors.toList());
        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
