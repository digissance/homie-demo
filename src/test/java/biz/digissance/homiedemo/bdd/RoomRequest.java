package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;

public record RoomRequest(TestRestTemplate restTemplate, String name, String description) {
    public RoomDto create(final SpaceDto space) {
        return restTemplate.postForObject("/spaces/{id}/rooms",
                CreateElementRequest.builder()
                        .name(name)
                        .description(name)
                        .build()
                , RoomDto.class, Map.of("id", space.getId()));
    }
}
