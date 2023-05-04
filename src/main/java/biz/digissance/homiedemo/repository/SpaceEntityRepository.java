package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.SpaceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceEntityRepository extends JpaRepository<SpaceEntity, Long> {
    List<SpaceEntity> findByOwnerId(long ownerId);

    Optional<SpaceEntity> findByIdAndPathStartingWith(long ownerId, final String startOfPath);

    List<SpaceEntity> findByOwnerIdentifier(String ownerId);
}
