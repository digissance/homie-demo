package biz.digissance.homiedemo.service.element;

import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.http.dto.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.List;

public interface SpaceService {

    SpaceDto createSpace(final CreateSpaceRequest request, final String owner);

    @PreAuthorize("hasPermission(#spaceId,'EDIT')")
    RoomDto createRoom(final long spaceId, final CreateElementRequest request);

    List<RoomEntity> getRooms(final long spaceId);

    SpaceDto getSpaceTree(final long spaceId);

    @PreAuthorize("hasPermission(#spaceId,'EDIT')")
    SpaceDto editSpace(long spaceId, CreateElementRequest request);

    List<SpaceDto> getSpaces(Authentication auth);

    @PreAuthorize("hasPermission(#elementId,'READ')")
    Collection<ElementDto> getElementPath(long spaceId, long elementId);
}
