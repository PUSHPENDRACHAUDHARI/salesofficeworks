
package com.sales.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("http://31.97.203.68:9092/")
    public String health() {
        return "Backend is running successfully";
    }
}

