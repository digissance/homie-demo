package biz.digissance.homiedemo.bdd.config;

import biz.digissance.homiedemo.bdd.steps.MyCache;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import biz.digissance.homiedemo.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public MyCache appCache(final UserService userService,
                            final ElementEntityRepository elementEntityRepository,
                            final UserEntityRepository userEntityRepository) {
        return new MyCache(userService, elementEntityRepository, userEntityRepository);
    }
}
