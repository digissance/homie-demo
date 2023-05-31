package biz.digissance.homiedemo.http.element.room;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.service.element.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/{id}/items")
    public final ResponseEntity<ItemDto> createItem(final @PathVariable long id,
                                                    final @RequestBody CreateElementRequest request,
                                                    final UriComponentsBuilder uri) {
        final var item = roomService.createItem(id, request);
        return ResponseEntity.created(uri
                .path("/items/{id}")
                .buildAndExpand(Map.of("id", item.getId()))
                .toUri()).body(item);
    }

    @PostMapping("/{id}/storage")
    public final ResponseEntity<StorageDto> createStorage(final @PathVariable long id,
                                                          final @RequestBody CreateElementRequest request,
                                                          final UriComponentsBuilder uri) {
        final var storage = roomService.createStorage(id, request);
        return ResponseEntity.created(uri
                .path("/storage/{id}")
                .buildAndExpand(Map.of("id", storage.getId()))
                .toUri()).body(storage);
    }

    @PatchMapping("/{id}")
    public final ResponseEntity<RoomDto> editRoom(final @PathVariable long id,
                                                  final @RequestBody CreateElementRequest request) {
        final var room = roomService.editRoom(id, request);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<Void> deleteRoom(final @PathVariable long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
