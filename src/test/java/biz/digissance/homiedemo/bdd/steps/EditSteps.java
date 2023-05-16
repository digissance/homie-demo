package biz.digissance.homiedemo.bdd.steps;


import static org.assertj.core.api.Assertions.assertThat;

import biz.digissance.homiedemo.http.dto.ElementDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.util.List;

public class EditSteps {

    @Given("user creates following elements:")
    public void user_creates_following_elements(List<ElementDto> elements) {
        assertThat(elements).isNotEmpty();
    }

    @When("user edits name of element {elementRequest} to {string} is a success")
    public void user_edits_name_of_element_to_is_a_success(ElementRequest elementRequest, String newName) {
        ElementDto updated = elementRequest.editName(newName);
        assertThat(updated.getName()).isEqualTo(newName);
    }
}
