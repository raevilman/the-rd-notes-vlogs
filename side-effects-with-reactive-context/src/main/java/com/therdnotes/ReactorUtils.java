package com.therdnotes;

import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ReactorUtils {
    public static <T>BiFunction<Mono<T>, ContextView, Mono<T>> runAsyncTaskWithContext(Function<T, Mono<T>> asyncTask) {
        return (originalMono, context) -> originalMono
                .doOnNext(input -> {
                    Mono.just(input)
                            .flatMap(asyncTask)
                            .contextWrite(context)
                            .subscribe();
                });
    }
}
