package com.example.adminservice.config.security;

import com.example.adminservice.repository.RoleFunctionRepository;
import com.example.adminservice.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UrlFilterInvocationSecurity implements FilterInvocationSecurityMetadataSource {
    private final RoleFunctionRepository roleFunctionRepository;

    /***
     * Return the required user permission information of the url
     *
     * @param object: Save request url information
     * @return: null: Identity does not need any permission to access
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        String requestUrl = ((FilterInvocation) object).getHttpRequest().getRequestURI();
        if (requestUrl.startsWith("/auth")) {
            return SecurityConfig.createList();
        }
        List<String> roles = roleFunctionRepository.getRoleByPath(requestUrl, Constants.STATUS.ACTIVE);
        if (roles != null && roles.size() > 0) {
            return SecurityConfig.createList(roles.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
