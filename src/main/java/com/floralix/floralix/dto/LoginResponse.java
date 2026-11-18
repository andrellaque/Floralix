
package com.floralix.floralix.dto;

public record LoginResponse(
        String token,
        String tipo,
        String username
) {
}
