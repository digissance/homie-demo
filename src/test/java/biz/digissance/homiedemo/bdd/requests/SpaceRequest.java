package biz.digissance.homiedemo.bdd.requests;

import biz.digissance.homiedemo.bdd.steps.ElementRequest;
import biz.digissance.homiedemo.bdd.steps.MyCache;
import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;

public record SpaceRequest(TestRestTemplate restTemplate, MyCache cache, String name, String description)
        implements ElementRequest {
    public SpaceDto createSpace() {
        final var spaceDto = restTemplate.postForObject("/spaces",
                CreateSpaceRequest.builder()
                        .name(name)
                        .description(description)
                        .build()
                , SpaceDto.class);
        cache.putElement(spaceDto);
        return spaceDto;
    }

    public SpaceDto getExpected() {
        return SpaceDto.builder()
                .name(name)
                .description(description)
                .build();
    }

    @Override
    public ElementDto create() {
        return createSpace();
    }

    @Override
    public ElementDto editName(final String newName) {
        final var oldElement = cache.findElementByName(name).orElseThrow();
        final var newElement = restTemplate.patchForObject("/spaces/{id}",
                CreateSpaceRequest.builder()
                        .name(newName)
                        .build()
                , SpaceDto.class, Map.of("id", oldElement.getId()));
        return newElement;
    }
}
