package com.example.orchestrator.model;
public record RetryPolicy(int maxAttempts, long delayMs) { }
