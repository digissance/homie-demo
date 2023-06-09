package biz.digissance.homiedemo.bdd.steps;

import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.http.dto.RoomOrStorageDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.http.dto.UserDto;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import biz.digissance.homiedemo.service.user.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Transactional
public class MyCache {

    private final UserService userService;
    private final Map<String, UserDto> userCache = new HashMap<>();
    private final Map<String, SpaceDto> spaceCache = new HashMap<>();
    private final Map<Long, ElementDto> elementsCache = new HashMap<>();
    private final ElementEntityRepository elementEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public MyCache(final UserService userService,
                   final ElementEntityRepository elementEntityRepository,
                   final UserEntityRepository userEntityRepository) {
        this.userService = userService;
        this.elementEntityRepository = elementEntityRepository;
        this.userEntityRepository = userEntityRepository;
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

    public void setSpaces(final SpaceDto... result) {
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

    public StorageDto findItemByName(final String itemName) {
        final var item = new AtomicReference<StorageDto>();
        final var visitor = getTraverser(elementDto -> {
            if (elementDto.getName().equals(itemName) && elementDto instanceof StorageDto rs) {
                item.set(rs);
            }
        });
        spaceCache.forEach((s, spaceDto) -> spaceDto.visit(visitor));
        return item.get();
    }

    private Consumer<ElementDto> getTraverser(Consumer<ElementDto> doYourThing) {
        return new ElementDtoVisitor(doYourThing);
    }

    public <T extends ElementDto> void putElement(final T element) {
        elementsCache.put(element.getId(), element);
    }

    public <T extends ElementDto> T getElementById(final long elementId) {
        return (T) elementsCache.get(elementId);
    }

    public void cleanAll() {
        elementEntityRepository.deleteAll();
        userEntityRepository.deleteAll();
        userCache.clear();
        spaceCache.clear();
        elementsCache.clear();
    }

    public Optional<ElementDto> findElementByName(final String name) {
        return elementsCache.values().stream()
                .filter(e -> name.equalsIgnoreCase(e.getName())).findFirst();
    }
}
