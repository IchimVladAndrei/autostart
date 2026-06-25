package com.autodrive.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InstanceIdentityFilter extends OncePerRequestFilter {

    private static final String INSTANCE_HEADER = "X-AutoStart-Instance";

    private final Environment environment;

    public InstanceIdentityFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.setHeader(INSTANCE_HEADER, instanceId());
        filterChain.doFilter(request, response);
    }

    private String instanceId() {
        String configuredInstanceId = environment.getProperty("app.instance-id");
        if (StringUtils.hasText(configuredInstanceId)) {
            return configuredInstanceId;
        }

        String applicationName = environment.getProperty("spring.application.name", "autostart-service");
        String port = environment.getProperty("local.server.port",
                environment.getProperty("server.port", "unknown"));
        return applicationName + ":" + port;
    }
}
