package com.xwplay.crm.common.resilience4j.service;

import java.util.concurrent.CompletableFuture;

public interface IBackendService {
    String failure();

    String failureWithFallback();

    String success();

    String successException();

    String ignoreException();

    CompletableFuture<String> futureSuccess();

    CompletableFuture<String> futureFailure();

    CompletableFuture<String> futureTimeout();

}
