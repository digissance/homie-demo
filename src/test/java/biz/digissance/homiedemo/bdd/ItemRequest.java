package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;

public record ItemRequest(TestRestTemplate restTemplate, String parent, String name, String description) {

    public ItemDto create(final RoomOrStorageDto parent) {
        return restTemplate.postForObject(getUrl(parent),
                CreateElementRequest.builder()
                        .name(name)
                        .description(description)
                        .build()
                , ItemDto.class, Map.of("id", parent.getId()));
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
}
