package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.SpaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpaceEntityRepository extends JpaRepository<SpaceEntity, Long> {

    @Query("from SpaceEntity e left join fetch e.owner where e.id = ?1")
    Optional<SpaceEntity> findByIdFetchOwner(final long id);

    @Query("""
            from SpaceEntity e
            left join fetch e.owner
            left join fetch e.parent
            left join fetch e.space
            left join fetch e.photo
            where e.owner.identifier = ?1
            """)
    List<SpaceEntity> findByOwnerIdentifier(String ownerId);
}
