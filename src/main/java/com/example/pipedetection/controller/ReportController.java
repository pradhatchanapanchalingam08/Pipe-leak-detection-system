package com.example.pipedetection.controller;

import com.example.pipedetection.dto.BalanceSheetResponse;
import com.example.pipedetection.dto.BudgetReportRow;
import com.example.pipedetection.dto.ProfitLossResponse;
import com.example.pipedetection.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/balance-sheet")
    public BalanceSheetResponse getBalanceSheet() {
        return reportService.getBalanceSheet();
    }

    @GetMapping("/profit-loss")
    public ProfitLossResponse getProfitLoss() {
        return reportService.getProfitLoss();
    }

    @GetMapping("/budget")
    public List<BudgetReportRow> getBudgetReport() {
        return reportService.getBudgetReport();
    }
}
