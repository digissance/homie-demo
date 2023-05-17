package biz.digissance.homiedemo.service;

import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.http.dto.CreateElementRequest;
import biz.digissance.homiedemo.http.dto.CreateSpaceRequest;
import biz.digissance.homiedemo.http.dto.RoomDto;
import biz.digissance.homiedemo.http.dto.SpaceDto;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

public interface SpaceService {

    SpaceDto createSpace(final CreateSpaceRequest request, final String owner);

    @PreAuthorize("hasPermission(#spaceId,'EDIT')")
    RoomDto createRoom(final long spaceId, final CreateElementRequest request);

    List<RoomEntity> getRooms(final long spaceId);

    SpaceDto getSpaceTree(final long spaceId);

    @PreAuthorize("hasPermission(#spaceId,'EDIT')")
    SpaceDto editSpace(long spaceId, CreateElementRequest request);

    List<SpaceDto> getSpaces(Authentication auth);
}
