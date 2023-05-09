package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.service.UserService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class MyCache {

    private final UserService userService;
    private final Map<String, UserDto> userCache = new HashMap<>();
    private final Map<String, SpaceDto> spaceCache = new HashMap<>();

    public MyCache(final UserService userService) {
        this.userService = userService;
    }

    public UserDto findUserByNameOrCreate(final String name) {
        final var password = "123456";
        return userService.findByName(name).orElseGet(() -> userService.create(name, password))
                .toBuilder()
                .password(password)
                .build();
    }

    public UserDto getCurrentUser() {
        return userCache.get("currentUser");
    }

    public void setCurrenUser(final UserDto user) {
        userCache.put("currentUser", user);
    }

    public UserDto findUserByName(final String name) {
        return userCache.get(name);
    }

    public void setSpaces(final SpaceDto[] result) {
        Arrays.stream(result).forEach(p -> spaceCache.put(p.getName(), p));
    }

    public SpaceDto getSpace(final String spaceName) {
        return spaceCache.get(spaceName);
    }

    public RoomOrStorageDto findRoomByName(final String roomName) {
        final var room = new AtomicReference<RoomOrStorageDto>();
        final var visitor = getTraverser(elementDto -> {
            if (elementDto.getName().equals(roomName) && elementDto instanceof RoomOrStorageDto rs) {
                room.set(rs);
            }
        });
        spaceCache.forEach((s, spaceDto) -> spaceDto.visit(visitor));
        return room.get();
    }

    private Consumer<ElementDto> getTraverser(Consumer<ElementDto> doYourThing) {
        return new ElementDtoVisitor(doYourThing);
    }
}
