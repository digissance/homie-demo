package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.RoomEntity;
import biz.digissance.homiedemo.domain.SpaceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomEntityRepository extends JpaRepository<RoomEntity, Long> {

    List<RoomEntity> findBySpaceId(long spaceId);
}
