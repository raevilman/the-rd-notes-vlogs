package com.therdnotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

public class AsyncFunctions {
    private final static Logger log = LoggerFactory.getLogger(AsyncFunctions.class);

    public static final Function<String, Mono<String>> asyncLoggingTask = input -> getUserEntryLoggerMono();

    public static final Consumer<? super String> logUserEntered = _input -> getUserEntryLoggerMono().subscribe();

    private static Mono<String> getUserEntryLoggerMono() {
        return AuthUtils
                .getCurrentUser()
                .delayElement(Duration.ofSeconds(2))
                .switchIfEmpty(Mono.error(new IllegalStateException("User not found")))
                .doOnNext(u -> log.info(u + " entered the chat room at " + System.currentTimeMillis() + " ms"))
                .onErrorResume(throwable -> {
                    log.error("Error: " + throwable.getMessage());
                    return Mono.empty();
                });
    }

    public static final Function<String, Mono<String>> sendEmailAsync = input -> AuthUtils.getCurrentUser()
            .delayElement(Duration.ofSeconds(6))
            .switchIfEmpty(Mono.error(new IllegalStateException("User not found")))
            .doOnNext(u -> log.info("Email sent to " + u))
            .onErrorResume(throwable -> {
                log.error("Error: " + throwable.getMessage());
                return Mono.empty();
            });
    public static final Function<String, Mono<String>> billChatAsync = input -> AuthUtils.getCurrentUser()
            .delayElement(Duration.ofSeconds(4))
            .switchIfEmpty(Mono.error(new IllegalStateException("User not found")))
            .doOnNext(u -> log.info("Chat session billed to " + u))
            .onErrorResume(throwable -> {
                log.error("Error: " + throwable.getMessage());
                return Mono.empty();
            });



}
