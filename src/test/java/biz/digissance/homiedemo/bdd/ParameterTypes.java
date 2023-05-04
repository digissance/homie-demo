package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.service.UserService;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import java.util.Map;

public record ParameterTypes(UserService userService) {

    @ParameterType(".*")
    public UserDto user(String name) {
        return userService.findByName(name).orElseGet(() -> userService.create(name));
    }

    @DataTableType
    public SpaceDto space(Map<String, String> map) {
        return SpaceDto.builder()
                .name(map.get("name"))
                .description(map.get("description"))
                .build();
    }
}
