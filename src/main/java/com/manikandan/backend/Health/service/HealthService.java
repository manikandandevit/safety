package com.manikandan.backend.Health.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {
    public String getPingPong() {
        return "Pong";
    }
}