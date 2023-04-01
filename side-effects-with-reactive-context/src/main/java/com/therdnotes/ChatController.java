package com.therdnotes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.therdnotes.AsyncFunctions.logUserEntered;

@RestController
public class ChatController {

    private static final String WELCOME_MSG = "Welcome to the chat room.";


    //region Basic chat entry
    @GetMapping("/entry1")
    public static Mono<String> entry1() {
        return AuthUtils.getCurrentUser()
                .map(user -> "Hello " + user + "! " + WELCOME_MSG);
    }
    //endregion


    //region Chat entry with logging
        @GetMapping("/entry2")
        public static Mono<String> entry2() {
            return AuthUtils.getCurrentUser()
                    .map(user -> "Hello " + user + "! " + WELCOME_MSG)
                    .doOnNext(logUserEntered);
        }
    //endregion






    //region Chat entry with logging with context
    @GetMapping("/entry3")
    public static Mono<String> entry3() {
        return AuthUtils.getCurrentUser()
                .map(user -> "Hello " + user + "! " + WELCOME_MSG)
                .transformDeferredContextual(ReactorUtils.runAsyncTaskWithContext(AsyncFunctions.asyncLoggingTask));
    }
    //endregion


    //region Chat entry with multiple background tasks
    @GetMapping("/entry4")
    public static Mono<String> entry4() {
        return AuthUtils.getCurrentUser()
                .map(user -> "Hello " + user + "! " + WELCOME_MSG)
                .transformDeferredContextual(ReactorUtils.runAsyncTaskWithContext(AsyncFunctions.asyncLoggingTask))
                .transformDeferredContextual(ReactorUtils.runAsyncTaskWithContext(AsyncFunctions.billChatAsync))
                .transformDeferredContextual(ReactorUtils.runAsyncTaskWithContext(AsyncFunctions.sendEmailAsync))
                ;
    }
    //endregion
}