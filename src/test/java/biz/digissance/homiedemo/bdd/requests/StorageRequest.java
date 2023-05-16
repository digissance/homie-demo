package biz.digissance.homiedemo.bdd.requests;

import biz.digissance.homiedemo.bdd.steps.ElementRequest;
import biz.digissance.homiedemo.bdd.steps.MyCache;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public record StorageRequest(TestRestTemplate restTemplate,
                             MyCache cache,
                             String name,
                             String description,
                             RoomOrStorageDto parent) implements ElementRequest {

    public StorageDto create(RoomOrStorageDto parent) {
        final var storageDto =
                restTemplate.postForObject(getUrl(parent), getBuild(), StorageDto.class, Map.of("id", parent.getId()));
        cache.putElement(storageDto);
        parent.getStuff().add(storageDto);
        return storageDto;
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

    @Override
    public ElementDto create() {
        return create(parent);
    }

    @Override
    public ElementDto editName(final String newName) {
        final var oldElement = cache.findElementByName(name).orElseThrow();
        final var newElement = restTemplate.patchForObject("/storage/{id}",
                CreateSpaceRequest.builder()
                        .name(newName)
                        .build()
                , StorageDto.class, Map.of("id", oldElement.getId()));
        return newElement;
    }
}
