
package com.manikandan.backend.Health.controller;
import com.manikandan.backend.Health.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {
   private final HealthService healthService;

   public HealthController(HealthService healthService){
    this.healthService = healthService;
   }

   @GetMapping("/ping")
   public String getPingPong(){
    return healthService.getPingPong();
   }
}