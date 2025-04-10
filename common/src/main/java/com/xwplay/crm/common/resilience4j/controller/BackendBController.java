package com.xwplay.crm.common.resilience4j.controller;

import com.xwplay.crm.common.resilience4j.service.BackendBService;
import com.xwplay.crm.common.resilience4j.service.IBackendService;
import io.github.resilience4j.bulkhead.*;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.vavr.control.Try;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

@RestController
@RequestMapping(value = "/backendB")
public class BackendBController {

    private static final String BACKEND_B = "backendB";
    private final IBackendService businessBService;
    private final CircuitBreaker circuitBreaker;
    private final Bulkhead bulkhead;
    private final ThreadPoolBulkhead threadPoolBulkhead;
    private final Retry retry;
    private final RateLimiter rateLimiter;
    private final TimeLimiter timeLimiter;
    private final ScheduledExecutorService scheduledExecutorService;

    public BackendBController(
            BackendBService businessBService,
            CircuitBreakerRegistry circuitBreakerRegistry,
            ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry,
            BulkheadRegistry bulkheadRegistry,
            RetryRegistry retryRegistry,
            RateLimiterRegistry rateLimiterRegistry,
            TimeLimiterRegistry timeLimiterRegistry) {
        this.businessBService = businessBService;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(BACKEND_B);
        this.bulkhead = bulkheadRegistry.bulkhead(BACKEND_B);
        this.threadPoolBulkhead = threadPoolBulkheadRegistry.bulkhead(BACKEND_B);
        this.retry = retryRegistry.retry(BACKEND_B);
        this.rateLimiter = rateLimiterRegistry.rateLimiter(BACKEND_B);
        this.timeLimiter = timeLimiterRegistry.timeLimiter(BACKEND_B);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(3);
    }

    @GetMapping("failure")
    public String failure() {
        return execute(businessBService::failure);
    }

    @GetMapping("success")
    public String success() {
        return execute(businessBService::success);
    }

    @GetMapping("successException")
    public String successException() {
        return execute(businessBService::successException);
    }

    @GetMapping("ignore")
    public String ignore() {
        return Decorators.ofSupplier(businessBService::ignoreException)
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead).get();
    }


    @GetMapping("futureFailure")
    public CompletableFuture<String> futureFailure() {
        return executeAsync(businessBService::failure);
    }

    @GetMapping("futureSuccess")
    public CompletableFuture<String> futureSuccess() {
        return executeAsync(businessBService::success);
    }

    @GetMapping("futureTimeout")
    public CompletableFuture<String> futureTimeout() {
        return executeAsyncWithFallback(this::timeout, this::fallback);
    }

    @GetMapping("fallback")
    public String failureWithFallback() {
        return businessBService.failureWithFallback();
    }

    private String timeout() {
        Try.run(() -> Thread.sleep(10000));
        return "";
    }


    private <T> T execute(Supplier<T> supplier) {
        return Decorators.ofSupplier(supplier)
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .withRetry(retry)
                .get();
    }

    private <T> CompletableFuture<T> executeAsync(Supplier<T> supplier) {
        return Decorators.ofSupplier(supplier)
                .withThreadPoolBulkhead(threadPoolBulkhead)
                .withTimeLimiter(timeLimiter, scheduledExecutorService)
                .withCircuitBreaker(circuitBreaker)
                .withRetry(retry, scheduledExecutorService)
                .get().toCompletableFuture();
    }

    private <T> CompletableFuture<T> executeAsyncWithFallback(Supplier<T> supplier, Function<Throwable, T> fallback) {
        return Decorators.ofSupplier(supplier)
                .withThreadPoolBulkhead(threadPoolBulkhead)
                .withTimeLimiter(timeLimiter, scheduledExecutorService)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(asList(TimeoutException.class, CallNotPermittedException.class, BulkheadFullException.class),
                        fallback)
                .get().toCompletableFuture();
    }

    private String fallback(Throwable ex) {
        return "Recovered: " + ex.toString();
    }

}
