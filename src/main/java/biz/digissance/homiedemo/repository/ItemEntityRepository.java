package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemEntityRepository extends JpaRepository<ItemEntity, Long> {
}
