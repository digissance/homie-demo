package biz.digissance.homiedemo;

import biz.digissance.homiedemo.cloudinary.CloudinaryImportBeanDefinitionRegistrar;
import java.security.Principal;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(proxyBeanMethods = false)
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@ImportRuntimeHints(CloudinaryImportBeanDefinitionRegistrar.class)
public class HomieDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomieDemoApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication).map(Principal::getName);
    }
}
