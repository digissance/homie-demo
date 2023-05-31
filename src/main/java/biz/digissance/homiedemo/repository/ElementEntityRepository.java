package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.ElementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ElementEntityRepository extends JpaRepository<ElementEntity, Long> {

    @Query("""
            select e from ElementEntity e
            left join fetch e.owner
            left join fetch e.parent
            left join fetch e.space
            left join fetch e.photo
            where e.space.id = ?1
            """)
    List<ElementEntity> findBySpaceId(final long spaceId);

    @Query("""
            select e from ElementEntity e
            left join fetch e.owner
            left join fetch e.parent
            left join fetch e.space
            left join fetch e.photo
            where e.id = ?1
            """)
    Optional<ElementEntity> findByIdFetchAllProperties(final long id);
}
