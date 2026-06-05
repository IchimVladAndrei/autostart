package com.autodrive.backend.security;

import com.autodrive.backend.entity.user.User;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final JwtProperties jwtProperties;
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private final Base64.Decoder decoder = Base64.getUrlDecoder();

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(jwtProperties.expirationMs());

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", user.getEmail());
        claims.put("role", user.getRole().name());
        claims.put("iat", now.getEpochSecond());
        claims.put("exp", expiresAt.getEpochSecond());

        String unsignedToken = encodeJson(header) + "." + encodeJson(claims);
        return unsignedToken + "." + sign(unsignedToken);
    }

    public String extractUsername(String token) {
        return parseClaims(token).subject();
    }

    public boolean isTokenValid(String token, User user) {
        JwtClaims claims = parseClaims(token);

        return claims.subject().equals(user.getEmail())
                && !isExpired(claims);
    }

    public JwtClaims parseClaims(String token) {
        String[] parts = token.split("\\.", -1);
        if (parts.length != 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        String unsignedToken = parts[0] + "." + parts[1];
        String expectedSignature = sign(unsignedToken);
        if (!MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
            throw new IllegalArgumentException("Invalid JWT signature");
        }

        Map<String, String> claims = parseFlatJson(new String(decoder.decode(parts[1]), StandardCharsets.UTF_8));
        JwtClaims jwtClaims = new JwtClaims(
                requiredClaim(claims, "sub"),
                requiredClaim(claims, "role"),
                parseLong(requiredClaim(claims, "exp"))
        );
        if (isExpired(jwtClaims)) {
            throw new IllegalArgumentException("Expired JWT");
        }
        return jwtClaims;
    }

    private boolean isExpired(JwtClaims claims) {
        return Instant.now().getEpochSecond() >= claims.expiresAt();
    }

    private String encodeJson(Map<String, Object> value) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(escape(entry.getKey())).append("\":");
            if (entry.getValue() instanceof Number) {
                json.append(entry.getValue());
            } else {
                json.append("\"").append(escape(entry.getValue().toString())).append("\"");
            }
            first = false;
        }
        json.append("}");
        return encoder.encodeToString(json.toString().getBytes(StandardCharsets.UTF_8));
    }

    private Map<String, String> parseFlatJson(String json) {
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JWT payload");
        }

        Map<String, String> claims = new LinkedHashMap<>();
        String body = json.substring(1, json.length() - 1);
        if (body.isBlank()) {
            return claims;
        }

        for (String field : body.split(",")) {
            String[] parts = field.split(":", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid JWT payload");
            }
            claims.put(unquote(parts[0].trim()), unquote(parts[1].trim()));
        }
        return claims;
    }

    private String requiredClaim(Map<String, String> claims, String name) {
        String value = claims.get(name);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing JWT claim: " + name);
        }
        return value;
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid JWT numeric claim", ex);
        }
    }

    private String unquote(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
        }
        return value;
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    private String sign(String unsignedToken) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(jwtProperties.secret().getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return encoder.encodeToString(mac.doFinal(unsignedToken.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot sign JWT", ex);
        }
    }

    public record JwtClaims(String subject, String role, long expiresAt) {
    }
}
