package biz.digissance.homiedemo.bdd.steps;

import biz.digissance.homiedemo.http.dto.ElementDto;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;
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
        final var response = restTemplate.exchange("/elements/{id}/path",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ElementDto>>() {
                }, element.getId());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        actual = response.getBody();
    }

    @Then("the full path of the element is returned")
    public void the_full_path_of_the_element_is_returned(List<String> names) {
        final var expected = names.stream().map(cache::findElementByName).map(Optional::get)
                .collect(Collectors.toList());
        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
