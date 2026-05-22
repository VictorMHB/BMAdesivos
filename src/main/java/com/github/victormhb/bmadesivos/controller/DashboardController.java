package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.DashboardResponseDTO;
import com.github.victormhb.bmadesivos.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponseDTO getDados() {
        return dashboardService.getDados();
    }
}