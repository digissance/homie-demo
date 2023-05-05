package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.UserDto;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import java.util.Map;
import org.springframework.boot.test.web.client.TestRestTemplate;

public record ParameterTypes(MyCache myCache, TestRestTemplate restTemplate) {

    @ParameterType(".*")
    public UserDto user(String name) {
        return myCache.findUserByName(name);
    }

    @DataTableType
    public SpaceRequest space(Map<String, String> map) {
        return new SpaceRequest(restTemplate, map.get("name"), map.get("description"));
    }

    @DataTableType
    public RoomRequest room(Map<String, String> map) {
        return new RoomRequest(restTemplate, map.get("name"), map.get("description"));
    }

    @DataTableType
    public StorageRequest storageRequest(Map<String, String> map) {
        return new StorageRequest(restTemplate, map.get("room"), map.get("name"), map.get("description"));
    }

    @DataTableType
    public ItemRequest itemRequest(Map<String, String> map) {
        return new ItemRequest(restTemplate, map.get("storage"), map.get("name"), map.get("description"));
    }
}
