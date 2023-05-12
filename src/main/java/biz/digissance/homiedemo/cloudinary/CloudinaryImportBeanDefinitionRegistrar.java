package biz.digissance.homiedemo.cloudinary;

import static org.springframework.aot.hint.MemberCategory.DECLARED_CLASSES;
import static org.springframework.aot.hint.MemberCategory.DECLARED_FIELDS;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_DECLARED_METHODS;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_PUBLIC_METHODS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_METHODS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS;
import static org.springframework.aot.hint.MemberCategory.PUBLIC_CLASSES;
import static org.springframework.aot.hint.MemberCategory.PUBLIC_FIELDS;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

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
