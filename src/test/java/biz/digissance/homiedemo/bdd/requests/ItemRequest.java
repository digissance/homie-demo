package biz.digissance.homiedemo.bdd.requests;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public record ItemRequest(TestRestTemplate restTemplate, String parent, String name, String description) {

    public ItemDto create(final RoomOrStorageDto parent) {
        return restTemplate.postForObject(getUrl(parent), getBuild(), ItemDto.class, Map.of("id", parent.getId()));
    }

    private String getUrl(final RoomOrStorageDto parent) {
        if (parent instanceof RoomDto room) {
            return "/rooms/{id}/items";
        }
        if (parent instanceof StorageDto storage) {
            return "/storage/{id}/items";
        }
        return null;
    }

    public ResponseEntity<ItemDto> createWithResponse(final RoomOrStorageDto parent) {
        return restTemplate.exchange(getUrl(parent), HttpMethod.POST,
                new HttpEntity<>(getBuild()), ItemDto.class, Map.of("id", parent.getId()));
    }

    private CreateElementRequest getBuild() {
        return CreateElementRequest.builder()
                .name(name)
                .description(description)
                .build();
    }
}
