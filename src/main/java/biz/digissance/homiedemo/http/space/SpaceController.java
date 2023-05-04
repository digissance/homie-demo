package biz.digissance.homiedemo.http.space;

import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import biz.digissance.homiedemo.service.SpaceService;
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
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService service;

    public SpaceController(final SpaceService service) {
        this.service = service;
    }

    @PostMapping
    public final ResponseEntity<SpaceDto> createSpace(final @RequestBody CreateSpaceRequest request,
                                                      final UriComponentsBuilder uri) {
        final var space = service.createSpace(request);
        return ResponseEntity.created(uri
                .path("/spaces/{id}")
                .buildAndExpand(Map.of("id", space.getName()))
                .toUri()).body(space);
    }

    @PostMapping("/{id}/rooms")
    public final ResponseEntity<RoomEntity> createRoom(final @PathVariable long id,
                                                       final @RequestBody CreateElementRequest request,
                                                       final UriComponentsBuilder uri) {
        final var element = service.createRoom(id, request);
        return ResponseEntity.created(uri.build().toUri())
                .body(element);
    }

    @GetMapping("/{id}/rooms")
    public final ResponseEntity<List<RoomEntity>> getRooms(final @PathVariable long id,
                                                           final UriComponentsBuilder uri) {
        return ResponseEntity.ok(service.getRooms(id));
    }

    @GetMapping("/whole-tree/{id}")
    public final ResponseEntity<SpaceDto> getWholeSpace(final @PathVariable long id,
                                                        final UriComponentsBuilder uri) {
        return ResponseEntity.ok(service.getSpaceTree(id));
    }
}
