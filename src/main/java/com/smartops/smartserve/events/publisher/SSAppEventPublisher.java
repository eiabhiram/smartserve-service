package com.smartops.smartserve.events.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SSAppEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publish(Object event) {
        // If you want it async, you can wrap into CompletableFuture.runAsync(...) or use @Async
        publisher.publishEvent(event);
    }
}
