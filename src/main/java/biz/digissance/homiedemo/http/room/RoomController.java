package biz.digissance.homiedemo.http.room;

import biz.digissance.homiedemo.domain.StuffEntity;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.repository.StuffEntityRepository;
import biz.digissance.homiedemo.service.RoomService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final StuffEntityRepository stuffEntityRepository;
    private final RoomService roomService;

    public RoomController(final StuffEntityRepository stuffEntityRepository,
                          final RoomService roomService) {
        this.stuffEntityRepository = stuffEntityRepository;
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

    @GetMapping("/{id}/elements")
    public final ResponseEntity<List<StuffEntity>> getElements(final @PathVariable long id,
                                                               final UriComponentsBuilder uri) {
        return ResponseEntity.ok(stuffEntityRepository.findByParentId(id));
    }
}
