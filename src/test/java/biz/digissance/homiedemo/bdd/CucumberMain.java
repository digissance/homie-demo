package biz.digissance.homiedemo.bdd;

import biz.digissance.homiedemo.bdd.config.PostgresTestContainerInitializer;
import biz.digissance.homiedemo.bdd.config.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("dev")
@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfig.class, initializers = PostgresTestContainerInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberMain {
}
