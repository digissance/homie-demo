package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public MyCache appCache(final UserService userService) {
        return new MyCache(userService);
    }
}
