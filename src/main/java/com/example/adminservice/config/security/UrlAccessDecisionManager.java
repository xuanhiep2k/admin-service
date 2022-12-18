package com.example.adminservice.config.security;

import com.example.adminservice.utils.Constants;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> needRoles = collection.stream().map(ConfigAttribute::getAttribute).collect(Collectors.toList());
        for (GrantedAuthority grantedAuthority : authorities) {
            if (needRoles.contains(grantedAuthority.getAuthority()) || grantedAuthority.getAuthority().equals(Constants.ROLE.ADMIN)) {
                return;
            }
        }
        throw new AccessDeniedException("Vui lòng liên hệ với ADMIN!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
