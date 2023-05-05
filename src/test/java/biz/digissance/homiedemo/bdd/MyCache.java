package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.service.UserService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MyCache {

    private final UserService userService;
    private final Map<String, UserDto> userCache = new HashMap<>();
    private final Map<String, SpaceDto> spaceCache = new HashMap<>();

    public MyCache(final UserService userService) {
        this.userService = userService;
    }

    public UserDto findUserByNameOrCreate(final String name) {
        return userService.findByName(name).orElseGet(() -> userService.create(name));
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
        spaceCache.forEach((s, spaceDto) -> spaceDto.visit(elementDto -> {
            if (elementDto.getName().equals(roomName) && elementDto instanceof RoomOrStorageDto) {
                room.set((RoomOrStorageDto) elementDto);
            }
        }));
        return room.get();
    }
}
