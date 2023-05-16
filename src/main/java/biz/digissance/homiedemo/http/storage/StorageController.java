package biz.digissance.homiedemo.http.storage;

import biz.digissance.homiedemo.domain.StuffEntity;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.service.StorageService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(final StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/{id}/items")
    public final ResponseEntity<ItemDto> createItem(final @PathVariable long id,
                                                    final @RequestBody CreateElementRequest request,
                                                    final UriComponentsBuilder uri) {
        final var item = storageService.createItem(id, request);
        return ResponseEntity.created(uri
                .path("/items/{id}")
                .buildAndExpand(Map.of("id", item.getId()))
                .toUri()).body(item);
    }

    @PostMapping("/{id}/storage")
    public final ResponseEntity<StorageDto> createStorage(final @PathVariable long id,
                                                          final @RequestBody CreateElementRequest request,
                                                          final UriComponentsBuilder uri) {
        final var storage = storageService.createStorage(id, request);
        return ResponseEntity.created(uri
                .path("/storage/{id}")
                .buildAndExpand(Map.of("id", storage.getId()))
                .toUri()).body(storage);
    }

    @PatchMapping("/{id}")
    public final ResponseEntity<StorageDto> editStorage(final @PathVariable long id,
                                                        final @RequestBody CreateElementRequest request) {
        final var storage = storageService.editStorage(id, request);
        return ResponseEntity.ok(storage);
    }

    @GetMapping("/{id}/elements")
    public final ResponseEntity<List<StuffEntity>> getElements(final @PathVariable long id,
                                                               final UriComponentsBuilder uri) {
//        return ResponseEntity.ok(stuffEntityRepository.findByParentId(id));
        throw new RuntimeException("not yet implemented");
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<Void> deleteStorage(final @PathVariable long id) {
        storageService.deleteStorage(id);
        return ResponseEntity.noContent().build();
    }
}
