package biz.digissance.homiedemo.bdd.requests;

import biz.digissance.homiedemo.bdd.steps.ElementRequest;
import biz.digissance.homiedemo.bdd.steps.MyCache;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public record ItemRequest(TestRestTemplate restTemplate, MyCache cache, String name, String description,
                          RoomOrStorageDto parent)
        implements
        ElementRequest {

    public ItemDto create(final RoomOrStorageDto parent) {
        final var itemDto =
                restTemplate.postForObject(getUrl(parent), getBuild(), ItemDto.class, Map.of("id", parent.getId()));
        cache.putElement(itemDto);
        parent.getStuff().add(itemDto);
        return itemDto;
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

    @Override
    public ElementDto create() {
        return create(parent);
    }

    @Override
    public ElementDto editName(final String newName) {
        final var oldElement = cache.findElementByName(name).orElseThrow();
        final var newElement = restTemplate.patchForObject("/items/{id}",
                CreateSpaceRequest.builder()
                        .name(newName)
                        .build()
                , ItemDto.class, Map.of("id", oldElement.getId()));
        return newElement;
    }
}
