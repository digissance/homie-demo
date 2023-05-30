package biz.digissance.homiedemo.bdd.config;

import biz.digissance.homiedemo.bdd.steps.ElementType;
import biz.digissance.homiedemo.bdd.steps.ElementTypeFactory;
import biz.digissance.homiedemo.bdd.steps.MyCache;
import biz.digissance.homiedemo.repository.ElementEntityRepository;
import biz.digissance.homiedemo.repository.UserEntityRepository;
import biz.digissance.homiedemo.service.user.UserService;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public MyCache appCache(final UserService userService,
                            final ElementEntityRepository elementEntityRepository,
                            final UserEntityRepository userEntityRepository) {
        return new MyCache(userService, elementEntityRepository, userEntityRepository);
    }

    @Bean
    public List<ElementTypeFactory> elementTypeFactoryList(
            final TestRestTemplate restTemplate, final MyCache cache) {
        Arrays.stream(ElementType.values()).forEach(elementType -> {
            elementType.setRestTemplate(restTemplate);
            elementType.setCache(cache);
        });
        return List.of(ElementType.values());
    }
}
