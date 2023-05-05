package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;

public record StorageRequest(TestRestTemplate restTemplate, String room, String name, String description) {

    public StorageDto create(RoomOrStorageDto parent) {
        return restTemplate.postForObject(getUrl(parent),
                CreateElementRequest.builder()
                        .name(name)
                        .description(description)
                        .build()
                , StorageDto.class, Map.of("id", parent.getId()));
    }

    private String getUrl(final RoomOrStorageDto parent) {

        if(parent instanceof RoomDto room){
            return "/rooms/{id}/storage";
        }
        if(parent instanceof StorageDto storage){
            return "/storage/{id}/storage";
        }

        return null;
    }
}
