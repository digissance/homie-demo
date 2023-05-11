package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public record StorageRequest(TestRestTemplate restTemplate, String room, String name, String description) {

    public StorageDto create(RoomOrStorageDto parent) {
        return restTemplate.postForObject(getUrl(parent), getBuild(), StorageDto.class, Map.of("id", parent.getId()));
    }

    private CreateElementRequest getBuild() {
        return CreateElementRequest.builder()
                .name(name)
                .description(description)
                .build();
    }

    private String getUrl(final RoomOrStorageDto parent) {

        if (parent instanceof RoomDto room) {
            return "/rooms/{id}/storage";
        }
        if (parent instanceof StorageDto storage) {
            return "/storage/{id}/storage";
        }

        return null;
    }

    public ResponseEntity<StorageDto> createWithResponse(final RoomOrStorageDto parent) {
        return restTemplate.exchange(getUrl(parent), HttpMethod.POST, new HttpEntity<>(getBuild()), StorageDto.class,
                Map.of("id", parent.getId()));
    }
}
