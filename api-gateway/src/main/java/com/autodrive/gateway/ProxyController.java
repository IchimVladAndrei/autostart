package com.autodrive.gateway;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Enumeration;
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
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public ProxyController(GatewayRoutesProperties routes) {
        this.routes = routes;
    }

    @RequestMapping("/api/v1/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request) throws IOException, InterruptedException {
        URI target = targetUri(request);
        byte[] body = request.getInputStream().readAllBytes();

        HttpRequest.Builder builder = HttpRequest.newBuilder(target)
                .timeout(Duration.ofSeconds(30))
                .method(request.getMethod(), body.length == 0
                        ? HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofByteArray(body));

        copyRequestHeaders(request, builder);

        HttpResponse<byte[]> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
        HttpHeaders headers = new HttpHeaders();
        response.headers().map().forEach((name, values) -> {
            if (!HOP_BY_HOP_HEADERS.contains(name.toLowerCase())) {
                headers.put(name, values);
            }
        });

        return new ResponseEntity<>(response.body(), headers, HttpStatusCode.valueOf(response.statusCode()));
    }

    private URI targetUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String baseUrl = routeBaseUrl(requestUri);
        String query = request.getQueryString();
        return URI.create(baseUrl + requestUri + (query == null ? "" : "?" + query));
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

    private void copyRequestHeaders(HttpServletRequest request, HttpRequest.Builder builder) {
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
