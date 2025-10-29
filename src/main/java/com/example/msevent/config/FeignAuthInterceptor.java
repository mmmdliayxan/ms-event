package com.example.msevent.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null && attributes.getRequest() != null) {
            String token = attributes.getRequest().getHeader("Authorization");

            if (token != null && !token.isEmpty()) {
                if (!token.startsWith("Bearer ")) {
                    token = "Bearer " + token;
                }
                template.header("Authorization", token);
            }
        }
    }
}