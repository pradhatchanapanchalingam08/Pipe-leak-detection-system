package com.example.pipedetection.service;

import com.example.pipedetection.dto.BalanceSheetResponse;
import com.example.pipedetection.dto.BudgetReportRow;
import com.example.pipedetection.dto.ProfitLossResponse;
import com.example.pipedetection.entity.Account;
import com.example.pipedetection.entity.AnalyticAccount;
import com.example.pipedetection.entity.Budget;
import com.example.pipedetection.entity.JournalEntry;
import com.example.pipedetection.entity.Product;
import com.example.pipedetection.repository.AccountRepository;
import com.example.pipedetection.repository.AnalyticAccountRepository;
import com.example.pipedetection.repository.BudgetRepository;
import com.example.pipedetection.repository.JournalEntryRepository;
import com.example.pipedetection.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Produces the three simple reports required by the project:
 * Balance Sheet, Profit & Loss and the Pipeline Sector Budget Report.
 * Account balances are derived from the journal entries recorded by the
 * other services (JournalService).
 */
@Service
public class ReportService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private AnalyticAccountRepository analyticAccountRepository;

    /** Sum of all debit entries for an account, by account name. Returns ZERO if the account has no entries. */
    private BigDecimal sumDebit(String accountName) {
        Optional<Account> account = accountRepository.findByAccountName(accountName);
        if (account.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return journalEntryRepository.findByAccountId(account.get().getId()).stream()
                .map(JournalEntry::getDebit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Sum of all credit entries for an account, by account name. Returns ZERO if the account has no entries. */
    private BigDecimal sumCredit(String accountName) {
        Optional<Account> account = accountRepository.findByAccountName(accountName);
        if (account.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return journalEntryRepository.findByAccountId(account.get().getId()).stream()
                .map(JournalEntry::getCredit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Asset / Expense accounts grow with debit, so balance = debit - credit. */
    private BigDecimal debitBalance(String accountName) {
        BigDecimal balance = sumDebit(accountName).subtract(sumCredit(accountName));
        return balance.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : balance;
    }

    /** Liability / Income accounts grow with credit, so balance = credit - debit. */
    private BigDecimal creditBalance(String accountName) {
        BigDecimal balance = sumCredit(accountName).subtract(sumDebit(accountName));
        return balance.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : balance;
    }

    public BalanceSheetResponse getBalanceSheet() {
        BigDecimal droneFleet = debitBalance("Pipe Crawler Drone Fleet");

        BigDecimal sealantStock = productRepository.findAll().stream()
                .filter(p -> p.getType() == Product.ProductType.GOODS)
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(
                        p.getStockQuantity() != null ? p.getStockQuantity() : 0)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cashBank = debitBalance("Cash/Bank");
        BigDecimal sealantPayables = creditBalance("Sealant Supplier Payables");
        BigDecimal droneCreditors = creditBalance("Drone Component Creditors");

        return new BalanceSheetResponse(droneFleet, sealantStock, cashBank, sealantPayables, droneCreditors);
    }

    public ProfitLossResponse getProfitLoss() {
        BigDecimal repairRevenue = creditBalance("Pipe Repair Revenues");
        BigDecimal inspectionFees = creditBalance("Preventive Inspection Fees");
        BigDecimal sealantCosts = debitBalance("Sealant Material Costs");
        BigDecimal droneCharging = debitBalance("Drone Battery Charging Expenses");

        return new ProfitLossResponse(repairRevenue, inspectionFees, sealantCosts, droneCharging);
    }

    public List<BudgetReportRow> getBudgetReport() {
        List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream().map(budget -> {
            String sectorName = analyticAccountRepository.findById(budget.getAnalyticAccountId())
                    .map(AnalyticAccount::getName)
                    .orElse("Unknown Sector");
            return new BudgetReportRow(
                    budget.getId(),
                    sectorName,
                    budget.getBudgetYear(),
                    budget.getPlannedAmount(),
                    budget.getSealantPurchaseAmount(),
                    budget.getDroneChargingAmount(),
                    budget.getRepairRevenueAmount()
            );
        }).collect(Collectors.toList());
    }
}
