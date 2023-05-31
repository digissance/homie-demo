package biz.digissance.homiedemo.security;

import biz.digissance.homiedemo.repository.ElementEntityRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class MyPermissionEvaluator implements PermissionEvaluator {
    private final ElementEntityRepository elementEntityRepository;

    public MyPermissionEvaluator(
            final ElementEntityRepository elementEntityRepository) {
        this.elementEntityRepository = elementEntityRepository;
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Object targetDomainObject,
                                 final Object permission) {

        final var realOwner = elementEntityRepository.findByIdFetchAllProperties((Long) targetDomainObject)
                .orElseThrow().getOwner();

        return realOwner.getIdentifier().equals(authentication.getName());
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Serializable targetId,
                                 final String targetType,
                                 final Object permission) {
        return false;
    }
}
