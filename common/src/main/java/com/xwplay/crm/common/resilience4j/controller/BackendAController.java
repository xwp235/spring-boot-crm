package com.xwplay.crm.common.resilience4j.controller;

import com.xwplay.crm.common.resilience4j.service.BackendAService;
import com.xwplay.crm.common.resilience4j.service.IBackendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/backendA")
public class BackendAController {

    private final IBackendService businessAService;

    public BackendAController(BackendAService businessAService) {
        this.businessAService = businessAService;
    }

    @GetMapping("failure")
    public String failure() {
        return businessAService.failure();
    }

    @GetMapping("success")
    public String success() {
        return businessAService.success();
    }

    @GetMapping("successException")
    public String successException() {
        return businessAService.successException();
    }

    @GetMapping("ignore")
    public String ignore() {
        return businessAService.ignoreException();
    }

    @GetMapping("futureFailure")
    public CompletableFuture<String> futureFailure() {
        return businessAService.futureFailure();
    }

    @GetMapping("futureSuccess")
    public CompletableFuture<String> futureSuccess() {
        return businessAService.futureSuccess();
    }

    @GetMapping("futureTimeout")
    public CompletableFuture<String> futureTimeout() {
        return businessAService.futureTimeout();
    }

    @GetMapping("fallback")
    public String failureWithFallback() {
        return businessAService.failureWithFallback();
    }
    
}
