package biz.digissance.homiedemo.cloudinary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import java.util.List;

import static org.springframework.aot.hint.MemberCategory.*;

@Slf4j
public class CloudinaryImportBeanDefinitionRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(final RuntimeHints hints, final ClassLoader classLoader) {

        hints.reflection().registerTypes(List.of(
                        TypeReference.of(com.cloudinary.http44.UploaderStrategy.class),
                        TypeReference.of(com.cloudinary.http44.ApiStrategy.class)
                ),
                hint -> hint
                        .withMembers(
                                PUBLIC_FIELDS,
                                DECLARED_FIELDS,
                                INTROSPECT_PUBLIC_CONSTRUCTORS,
                                INTROSPECT_DECLARED_CONSTRUCTORS,
                                INVOKE_PUBLIC_CONSTRUCTORS,
                                INVOKE_DECLARED_CONSTRUCTORS,
                                INTROSPECT_PUBLIC_METHODS,
                                INTROSPECT_DECLARED_METHODS,
                                INVOKE_PUBLIC_METHODS,
                                INVOKE_DECLARED_METHODS,
                                PUBLIC_CLASSES,
                                DECLARED_CLASSES));

        log.info("Registering Cloudinary runtime hints");
    }
}
