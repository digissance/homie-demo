package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public record RoomRequest(TestRestTemplate restTemplate, String name, String description) {

    public static final String SPACES_ID_ROOMS = "/spaces/{id}/rooms";

    public RoomDto create(final SpaceDto space) {
        return restTemplate.postForObject(SPACES_ID_ROOMS,
                getBuild()
                , RoomDto.class, Map.of("id", space.getId()));
    }

    private CreateElementRequest getBuild() {
        return CreateElementRequest.builder()
                .name(name)
                .description(description)
                .build();
    }

    public ResponseEntity<RoomDto> createWithResponse(final SpaceDto space) {
        return restTemplate.exchange(SPACES_ID_ROOMS, HttpMethod.POST, new HttpEntity<>(getBuild()), RoomDto.class,
                Map.of("id", space.getId()));
    }
}
