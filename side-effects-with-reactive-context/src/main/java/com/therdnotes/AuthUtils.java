package com.therdnotes;

import reactor.core.publisher.Mono;

public class AuthUtils {
    public static Mono<String> getCurrentUser() {
        return Mono.deferContextual(Mono::just)
                .filter(ctx -> ctx.hasKey("user"))
                .map(ctx -> ctx.get("user"));
    }
}
