package biz.digissance.homiedemo.http.element;

import biz.digissance.homiedemo.http.dto.ElementDto;
import biz.digissance.homiedemo.service.element.ElementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/elements")
public class ElementController {

    private final ElementService service;

    public ElementController(ElementService service) {
        this.service = service;
    }

    @GetMapping("/{id}/path")
    public ResponseEntity<Collection<ElementDto>> getElementPath(@PathVariable long id) {
        return ResponseEntity.ok(service.getElementPath(id));
    }
}
