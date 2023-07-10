package biz.digissance.homiedemo.http.element.space;

import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.http.dto.*;
import biz.digissance.homiedemo.service.element.SpaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService service;

    public SpaceController(final SpaceService service) {
        this.service = service;
    }


    @GetMapping
    public final ResponseEntity<List<SpaceDto>> getUserSpaces(Authentication auth) {
        return ResponseEntity.ok(service.getSpaces(auth));
    }


    @PostMapping
    public final ResponseEntity<SpaceDto> createSpace(
            final @RequestBody CreateSpaceRequest request,
            final Authentication auth,
            final UriComponentsBuilder uri) {
        final var space = service.createSpace(request, auth.getName());
        return ResponseEntity.created(uri
                .path("/spaces/{id}")
                .buildAndExpand(Map.of("id", space.getName()))
                .toUri()).body(space);
    }

    @PostMapping("/{id}/rooms")
    public final ResponseEntity<RoomDto> createRoom(final @PathVariable long id,
                                                    final @RequestBody CreateElementRequest request,
                                                    final UriComponentsBuilder uri) {
        final var room = service.createRoom(id, request);
        return ResponseEntity.created(uri
                .path("/rooms/{id}")
                .buildAndExpand(Map.of("id", room.getId()))
                .toUri()).body(room);
    }

    @PatchMapping("/{id}")
    public final ResponseEntity<SpaceDto> editSpace(final @PathVariable long id,
                                                    final @RequestBody CreateElementRequest request) {
        final var space = service.editSpace(id, request);
        return ResponseEntity.ok(space);
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

    @GetMapping("/{id}/elements/{elementId}/path")
    public ResponseEntity<Collection<ElementDto>> getElementPath(final @PathVariable long id,
                                                                 final @PathVariable long elementId) {
        return ResponseEntity.ok(service.getElementPath(id, elementId));
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<Void> deleteSpace(final @PathVariable long id) {
        service.deleteSpace(id);
        return ResponseEntity.noContent().build();
    }
}
