package com.xwplay.crm.platform;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractCircuitBreakerTest extends AbstractIntegrationTest {

    protected void checkHealthStatus(String circuitBreakerName, CircuitBreaker.State state) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        assertThat(circuitBreaker.getState()).isEqualTo(state);
    }

}
