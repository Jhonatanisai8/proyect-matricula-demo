package com.isai.demowebregistrationsystem.controllers.admin;

import com.isai.demowebregistrationsystem.model.dtos.DashboardSummaryDTO;
import com.isai.demowebregistrationsystem.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
        model.addAttribute("summary", summary);
        return "admin/dashboard-admin";
    }

}