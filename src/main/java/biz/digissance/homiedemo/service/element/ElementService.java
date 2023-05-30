package biz.digissance.homiedemo.service.element;

import biz.digissance.homiedemo.http.dto.ElementDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;

public interface ElementService {
    @PreAuthorize("hasPermission(#id,'READ')")
    Collection<ElementDto> getElementPath(long id);
}
