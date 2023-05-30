package biz.digissance.homiedemo.http.element.item;

import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.ItemDto;
import biz.digissance.homiedemo.service.element.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<Void> deleteStorage(final @PathVariable long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public final ResponseEntity<ItemDto> editItem(final @PathVariable long id,
                                                  final @RequestBody CreateElementRequest request) {
        final var item = itemService.editItem(id, request);
        return ResponseEntity.ok(item);
    }
}
