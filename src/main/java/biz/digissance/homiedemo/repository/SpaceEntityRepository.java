package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.SpaceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceEntityRepository extends JpaRepository<SpaceEntity, Long> {
    List<SpaceEntity> findByOwnerId(long ownerId);

    Optional<SpaceEntity> findByIdAndOwner_Identifier(long id, final String ownerIdentifier);

    List<SpaceEntity> findByOwnerIdentifier(String ownerId);
}
