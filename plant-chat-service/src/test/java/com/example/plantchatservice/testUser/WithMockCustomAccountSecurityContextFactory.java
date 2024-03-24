package com.example.plantchatservice.testUser;

import com.example.plantchatservice.dto.member.MemberDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockCustomAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomAccount> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomAccount customOAuth2Account) {

        final SecurityContext context = SecurityContextHolder.createEmptyContext();


        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", customOAuth2Account.username());
        attributes.put("name", customOAuth2Account.name());
        attributes.put("email", customOAuth2Account.email());


        final MemberDto principal = MemberDto.builder()
                .id(1L)
                .nickname(customOAuth2Account.name())
                .username(customOAuth2Account.username())
                .email(customOAuth2Account.email())
                .build();


        final Authentication token = new UsernamePasswordAuthenticationToken(principal, "dd",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));


        context.setAuthentication(token);
        return context;
    }
}
