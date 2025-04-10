package com.xwplay.crm.common.resilience4j.controller;

import com.xwplay.crm.common.resilience4j.service.BackendCService;
import com.xwplay.crm.common.resilience4j.service.IBackendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/backendC")
public class BackendCController {

    private final IBackendService businessCService;

    public BackendCController(BackendCService businessCService) {
        this.businessCService = businessCService;
    }

    @GetMapping("failure")
    public String failure() {
        return businessCService.failure();
    }

    @GetMapping("success")
    public String success() {
        return businessCService.success();
    }

    @GetMapping("successException")
    public String successException() {
        return businessCService.successException();
    }

    @GetMapping("ignore")
    public String ignore() {
        return businessCService.ignoreException();
    }

    @GetMapping("futureFailure")
    public CompletableFuture<String> futureFailure() {
        return businessCService.futureFailure();
    }

    @GetMapping("futureSuccess")
    public CompletableFuture<String> futureSuccess() {
        return businessCService.futureSuccess();
    }

    @GetMapping("futureTimeout")
    public CompletableFuture<String> futureTimeout() {
        return businessCService.futureTimeout();
    }

    @GetMapping("fallback")
    public String failureWithFallback() {
        return businessCService.failureWithFallback();
    }
}
