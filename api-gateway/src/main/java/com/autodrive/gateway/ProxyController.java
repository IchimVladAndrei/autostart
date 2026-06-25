package com.autodrive.gateway;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;

@RestController
public class ProxyController {

    private static final Set<String> HOP_BY_HOP_HEADERS = Set.of(
            "connection",
            "content-length",
            "expect",
            "host",
            "keep-alive",
            "proxy-authenticate",
            "proxy-authorization",
            "te",
            "trailer",
            "transfer-encoding",
            "upgrade"
    );

    private final GatewayRoutesProperties routes;
    private final WebClient webClient;

    public ProxyController(GatewayRoutesProperties routes, WebClient.Builder loadBalancedWebClientBuilder) {
        this.routes = routes;
        this.webClient = loadBalancedWebClientBuilder.build();
    }

    @RequestMapping("/api/v1/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request) throws IOException {
        byte[] body = request.getInputStream().readAllBytes();
        try {
            return forward(request, body);
        } catch (WebClientRequestException firstFailure) {
            return forward(request, body);
        }
    }

    private ResponseEntity<byte[]> forward(HttpServletRequest request, byte[] body) {
        String target = targetUri(request);

        WebClient.RequestBodySpec requestSpec = webClient
                .method(Objects.requireNonNull(HttpMethod.valueOf(request.getMethod())))
                .uri(target);

        copyRequestHeaders(request, requestSpec);

        WebClient.RequestHeadersSpec<?> headersSpec = body.length == 0
                ? requestSpec
                : requestSpec.bodyValue(body);

        ResponseEntity<byte[]> response = headersSpec
                .exchangeToMono(clientResponse -> clientResponse.toEntity(byte[].class))
                .block();

        HttpHeaders headers = new HttpHeaders();
        Objects.requireNonNull(response).getHeaders().forEach((name, values) -> {
            if (!HOP_BY_HOP_HEADERS.contains(name.toLowerCase())) {
                headers.put(name, values);
            }
        });

        return new ResponseEntity<>(response.getBody(), headers, HttpStatusCode.valueOf(response.getStatusCode().value()));
    }

    private String targetUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String baseUrl = routeBaseUrl(requestUri);
        String query = request.getQueryString();
        return baseUrl + requestUri + (query == null ? "" : "?" + query);
    }

    private String routeBaseUrl(String requestUri) {
        if (requestUri.startsWith("/api/v1/auth/")
                || requestUri.startsWith("/api/v1/users")
                || requestUri.startsWith("/api/v1/customers")
                || requestUri.startsWith("/api/v1/employees")) {
            return routes.authUserUrl();
        }
        if (requestUri.startsWith("/api/v1/vehicles")
                || requestUri.startsWith("/api/v1/brands")
                || requestUri.startsWith("/api/v1/extra-options")) {
            return routes.vehicleUrl();
        }
        if (requestUri.startsWith("/api/v1/sale-contracts")
                || requestUri.startsWith("/api/v1/payments")) {
            return routes.salesUrl();
        }
        throw new IllegalArgumentException("No gateway route configured for " + requestUri);
    }

    private void copyRequestHeaders(HttpServletRequest request, WebClient.RequestBodySpec builder) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (HOP_BY_HOP_HEADERS.contains(name.toLowerCase())) {
                continue;
            }
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                builder.header(name, values.nextElement());
            }
        }
    }
}
