package biz.digissance.homiedemo.bdd.requests;

import biz.digissance.homiedemo.bdd.steps.ElementRequest;
import biz.digissance.homiedemo.bdd.steps.MyCache;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public record RoomRequest(TestRestTemplate restTemplate, MyCache cache, String name, String description, SpaceDto space)
        implements ElementRequest {

    public static final String SPACES_ID_ROOMS = "/spaces/{id}/rooms";

    @Override
    public ElementDto create() {
        return create(space);
    }

    @Override
    public ElementDto editName(final String newName) {
        final var oldElement = cache.findElementByName(name).orElseThrow();
        final var newElement = restTemplate.patchForObject("/rooms/{id}",
                CreateSpaceRequest.builder()
                        .name(newName)
                        .build()
                , RoomDto.class, Map.of("id", oldElement.getId()));
        return newElement;
    }

    public RoomDto create(final SpaceDto space) {
        final var roomDto = restTemplate.postForObject(SPACES_ID_ROOMS,
                getBuild()
                , RoomDto.class, Map.of("id", space.getId()));
        cache.putElement(roomDto);
        space.getRooms().add(roomDto);
        return roomDto;
    }

    public ResponseEntity<RoomDto> createWithResponse(final SpaceDto space) {
        return restTemplate.exchange(SPACES_ID_ROOMS, HttpMethod.POST, new HttpEntity<>(getBuild()), RoomDto.class,
                Map.of("id", space.getId()));
    }

    private CreateElementRequest getBuild() {
        return CreateElementRequest.builder()
                .name(name)
                .description(description)
                .build();
    }
}
