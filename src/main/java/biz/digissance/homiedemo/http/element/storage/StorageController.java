package biz.digissance.homiedemo.http.element.storage;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.StorageDto;
import biz.digissance.homiedemo.service.element.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(final StorageService storageService) {
        this.storageService = storageService;
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

    @DeleteMapping("/{id}")
    public final ResponseEntity<Void> deleteStorage(final @PathVariable long id) {
        storageService.deleteStorage(id);
        return ResponseEntity.noContent().build();
    }
}
