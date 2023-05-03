package biz.digissance.homiedemo.repository;

import biz.digissance.homiedemo.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}
