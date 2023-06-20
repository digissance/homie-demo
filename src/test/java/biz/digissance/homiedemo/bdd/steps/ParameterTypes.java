package biz.digissance.homiedemo.bdd.steps;

import biz.digissance.homiedemo.bdd.requests.RoomRequest;
import biz.digissance.homiedemo.bdd.requests.SpaceRequest;
import biz.digissance.homiedemo.bdd.requests.StorageRequest;
import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ParameterTypes {

    private final MyCache cache;
    private final TestRestTemplate restTemplate;

    public ParameterTypes(final MyCache cache,
                          final TestRestTemplate restTemplate) {
        this.cache = cache;
        this.restTemplate = restTemplate;
        this.restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @ParameterType(".*")
    public UserDto user(String name) {
        return cache.findUserByName(name);
    }

    @DataTableType
    public SpaceRequest space(Map<String, String> map) {
        return new SpaceRequest(restTemplate, cache, map.get("name"), map.get("description"));
    }

    @DataTableType
    public RoomRequest room(Map<String, String> map) {
        return new RoomRequest(restTemplate, cache, map.get("name"), map.get("description"), null);
    }

    @DataTableType
    public StorageRequest storageRequest(Map<String, String> map) {
        final var room = Optional.ofNullable(map.get("room"))
                .map(p -> RoomDto.builder().name(p).build()).orElse(null);
        final var storage = Optional.ofNullable(map.get("storage"))
                .map(p -> StorageDto.builder().name(p).build()).orElse(null);
        var parent = room != null ? room : storage;
        return new StorageRequest(restTemplate, cache, map.get("name"), map.get("description"), parent);
    }

    @DataTableType
    public ElementDto elementRequest(Map<String, String> map) {

        final var type = map.get("type");
        ElementRequest request = null;
        switch (type) {
            case "space" -> request = new SpaceRequest(restTemplate, cache, map.get("name"), map.get("description"));
            case "room" -> {
                final var space = (SpaceDto) cache.findElementByName(map.get("parent")).orElseThrow();
                request = new RoomRequest(restTemplate, cache, map.get("name"), map.get("description"), space);
            }
            case "storage" -> {
                final var parent = (RoomOrStorageDto) cache.findElementByName(map.get("parent")).orElseThrow();
                request = new StorageRequest(restTemplate, cache, map.get("name"), map.get("description"), parent);
            }
        }

        return Objects.requireNonNull(request).create();
    }

    @ParameterType(".*")
    public ElementRequest elementRequest(String name) {
        final var elementDto = cache.findElementByName(name).orElseThrow();
        return ElementType.getType(elementDto).getRequest(elementDto);
    }

    @ParameterType(".*")
    public ElementDto element(String name) {
        return cache.findElementByName(name).orElseThrow();
    }
}
