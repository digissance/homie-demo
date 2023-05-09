package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import org.springframework.boot.test.web.client.TestRestTemplate;

public record SpaceRequest(TestRestTemplate restTemplate, String name, String description) {
    public SpaceDto createSpace(final UserDto user) {
        return restTemplate.postForObject("/spaces",
                CreateSpaceRequest.builder()
                        .name(name)
                        .description(description)
                        .build()
                , SpaceDto.class);
    }

    public SpaceDto getExpected() {
        return SpaceDto.builder()
                .name(name)
                .description(description)
                .build();
    }
}
