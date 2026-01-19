package com.upb.agripos.service;

import com.upb.agripos.model.Transaction;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service untuk generate laporan penjualan (FR-4)
 */
public class ReportService {
    private final TransactionService transactionService;
    private final NumberFormat currencyFormat;
    private final DateTimeFormatter dateFormatter;

    public ReportService(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    /**
     * Generate laporan penjualan harian
     */
    public String generateDailyReport(LocalDate date) throws Exception {
        List<Transaction> transactions = transactionService.getTransactionsByDate(date);
        double totalSales = transactionService.getDailySalesTotal(date);
        int transactionCount = transactionService.getDailyTransactionCount(date);

        StringBuilder sb = new StringBuilder();
        sb.append("====================================\n");
        sb.append("     LAPORAN PENJUALAN HARIAN\n");
        sb.append("====================================\n\n");
        sb.append("Tanggal: ").append(date.format(dateFormatter)).append("\n\n");
        
        sb.append("RINGKASAN:\n");
        sb.append("--------------------------------\n");
        sb.append(String.format("Jumlah Transaksi : %d\n", transactionCount));
        sb.append(String.format("Total Penjualan  : %s\n", formatCurrency(totalSales)));
        sb.append("\n");

        if (!transactions.isEmpty()) {
            sb.append("DETAIL TRANSAKSI:\n");
            sb.append("--------------------------------\n");
            
            for (Transaction t : transactions) {
                sb.append(String.format("[%s] %s - %s (%s)\n",
                    t.getTransactionCode(),
                    t.getTransactionDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    formatCurrency(t.getTotal()),
                    t.getPaymentMethod()));
            }
        } else {
            sb.append("Tidak ada transaksi pada tanggal ini.\n");
        }

        sb.append("\n====================================\n");
        return sb.toString();
    }

    /**
     * Generate laporan penjualan periodik
     */
    public String generatePeriodReport(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        
        double totalSales = transactions.stream()
            .mapToDouble(Transaction::getTotal)
            .sum();
        
        double totalTax = transactions.stream()
            .mapToDouble(Transaction::getTax)
            .sum();

        StringBuilder sb = new StringBuilder();
        sb.append("====================================\n");
        sb.append("    LAPORAN PENJUALAN PERIODIK\n");
        sb.append("====================================\n\n");
        sb.append("Periode: ").append(startDate.format(dateFormatter))
          .append(" s/d ").append(endDate.format(dateFormatter)).append("\n\n");

        sb.append("RINGKASAN:\n");
        sb.append("--------------------------------\n");
        sb.append(String.format("Jumlah Transaksi : %d\n", transactions.size()));
        sb.append(String.format("Total Pajak      : %s\n", formatCurrency(totalTax)));
        sb.append(String.format("Total Penjualan  : %s\n", formatCurrency(totalSales)));
        sb.append("\n");

        // Group by date
        Map<LocalDate, List<Transaction>> byDate = transactions.stream()
            .collect(Collectors.groupingBy(t -> t.getTransactionDate().toLocalDate()));

        sb.append("PENJUALAN PER HARI:\n");
        sb.append("--------------------------------\n");
        
        byDate.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                LocalDate date = entry.getKey();
                List<Transaction> dayTransactions = entry.getValue();
                double dayTotal = dayTransactions.stream()
                    .mapToDouble(Transaction::getTotal)
                    .sum();
                sb.append(String.format("%s : %d transaksi - %s\n",
                    date.format(dateFormatter),
                    dayTransactions.size(),
                    formatCurrency(dayTotal)));
            });

        // Group by payment method
        Map<String, Double> byPaymentMethod = transactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::getPaymentMethod,
                Collectors.summingDouble(Transaction::getTotal)));

        sb.append("\nPENJUALAN PER METODE PEMBAYARAN:\n");
        sb.append("--------------------------------\n");
        
        byPaymentMethod.forEach((method, total) -> {
            sb.append(String.format("%-15s : %s\n", method, formatCurrency(total)));
        });

        sb.append("\n====================================\n");
        return sb.toString();
    }

    /**
     * Generate laporan ringkas untuk dashboard
     */
    public DashboardSummary getDashboardSummary(LocalDate date) throws Exception {
        double todaySales = transactionService.getDailySalesTotal(date);
        int todayTransactions = transactionService.getDailyTransactionCount(date);

        // Week summary
        LocalDate weekStart = date.minusDays(6);
        List<Transaction> weekTransactions = transactionService.getTransactionsByDateRange(weekStart, date);
        double weekSales = weekTransactions.stream()
            .mapToDouble(Transaction::getTotal)
            .sum();

        return new DashboardSummary(todaySales, todayTransactions, weekSales, weekTransactions.size());
    }

    private String formatCurrency(double value) {
        return currencyFormat.format(value);
    }

    /**
     * Get total sales by specific date
     */
    public double getSalesByDate(LocalDate date) throws Exception {
        return transactionService.getDailySalesTotal(date);
    }

    /**
     * Get total sales by period
     */
    public double getSalesByPeriod(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return transactions.stream()
            .mapToDouble(Transaction::getTotal)
            .sum();
    }

    /**
     * Get transaction count by date
     */
    public int getTransactionCountByDate(LocalDate date) throws Exception {
        return transactionService.getDailyTransactionCount(date);
    }

    /**
     * DTO untuk dashboard summary
     */
    public static class DashboardSummary {
        private final double todaySales;
        private final int todayTransactions;
        private final double weekSales;
        private final int weekTransactions;

        public DashboardSummary(double todaySales, int todayTransactions, 
                               double weekSales, int weekTransactions) {
            this.todaySales = todaySales;
            this.todayTransactions = todayTransactions;
            this.weekSales = weekSales;
            this.weekTransactions = weekTransactions;
        }

        public double getTodaySales() { return todaySales; }
        public int getTodayTransactions() { return todayTransactions; }
        public double getWeekSales() { return weekSales; }
        public int getWeekTransactions() { return weekTransactions; }
    }
}
