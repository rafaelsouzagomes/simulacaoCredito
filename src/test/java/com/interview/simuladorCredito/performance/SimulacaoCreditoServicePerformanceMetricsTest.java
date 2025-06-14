package com.interview.simuladorCredito.performance;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoRequestDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoResponseDto;
import com.interview.simuladorCredito.services.SimulacaoCreditoService;

@SpringBootTest
class SimulacaoCreditoServicePerformanceMetricsTest {

    @Autowired
    private SimulacaoCreditoService simulacaoCreditoService;

    // Configurações de carga
    private static final int NUM_THREADS = 20;
    private static final int REQUESTS_PER_THREAD = 50;
    private static final long TIMEOUT_SECONDS = 60;

    // Metas de performance
    private static final double MIN_THROUGHPUT_RPS = 100.0; // Requisições por segundo mínimas
    private static final long MAX_LATENCY_MS = 500; // Latência máxima em milissegundos
    private static final double MAX_LATENCY_P95_MS = 800; // Latência P95 em milissegundos
    private static final double MAX_ERROR_RATE = 0.01; // Taxa máxima de erros (1%)

    @Test
    @Sql(scripts = {
        "classpath:db/test/V1__criar_tabelas_teste.sql",
        "classpath:db/migration/V4__insert_test_user.sql",
        "classpath:db/migration/V5__corrigir_taxas_padrao.sql"
    })
    void deveAtenderMetasDePerformance() throws Exception {
        // given
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<CompletableFuture<ResponseMetrics>> futures = new ArrayList<>();
        AtomicLong totalErrors = new AtomicLong(0);
        List<Long> latencies = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        // when
        for (int i = 0; i < NUM_THREADS; i++) {
            CompletableFuture<ResponseMetrics> future = CompletableFuture.supplyAsync(() -> {
                ResponseMetrics metrics = new ResponseMetrics();
                for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
                    try {
                        long requestStart = System.currentTimeMillis();
                        SimulacaoCreditoComArmazenamentoRequestDto request = criarRequestSimulacao();
                        SimulacaoCreditoComArmazenamentoResponseDto response = simulacaoCreditoService.criarSimulacao(request);
                        long latency = System.currentTimeMillis() - requestStart;
                        
                        metrics.totalLatency += latency;
                        metrics.successCount++;
                        latencies.add(latency);
                    } catch (Exception e) {
                        metrics.errorCount++;
                        totalErrors.incrementAndGet();
                    }
                }
                return metrics;
            }, executor);
            futures.add(future);
        }

        // then
        List<ResponseMetrics> results = futures.stream()
            .map(future -> {
                try {
                    return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        int totalRequests = NUM_THREADS * REQUESTS_PER_THREAD;
        long totalSuccess = results.stream().mapToLong(r -> r.successCount).sum();
        long totalErrors1 = results.stream().mapToLong(r -> r.errorCount).sum();

        // Calcular métricas
        double throughput = (totalSuccess * 1000.0) / totalTime;
        double errorRate = (double) totalErrors1 / totalRequests;
        double avgLatency = latencies.stream().mapToLong(Long::longValue).average().orElse(0);
        double p95Latency = calculateP95Latency(latencies);

        // Imprimir resultados
        System.out.println("\n=== Resultados do Teste de Performance ===");
        System.out.println("Total de Requisições: " + totalRequests);
        System.out.println("Requisições com Sucesso: " + totalSuccess);
        System.out.println("Requisições com Erro: " + totalErrors1);
        System.out.println("Tempo Total: " + totalTime + "ms");
        System.out.println("Throughput: " + String.format("%.2f", throughput) + " req/s");
        System.out.println("Taxa de Erro: " + String.format("%.2f", errorRate * 100) + "%");
        System.out.println("Latência Média: " + String.format("%.2f", avgLatency) + "ms");
        System.out.println("Latência P95: " + String.format("%.2f", p95Latency) + "ms");

        // Validar métricas
        assertTrue(throughput >= MIN_THROUGHPUT_RPS,
            String.format("Throughput abaixo do esperado. Esperado: %.2f req/s, Atual: %.2f req/s",
                MIN_THROUGHPUT_RPS, throughput));
        
        assertTrue(avgLatency <= MAX_LATENCY_MS,
            String.format("Latência média acima do esperado. Esperado: %d ms, Atual: %.2f ms",
                MAX_LATENCY_MS, avgLatency));
        
        assertTrue(p95Latency <= MAX_LATENCY_P95_MS,
            String.format("Latência P95 acima do esperado. Esperado: %.2f ms, Atual: %.2f ms",
                MAX_LATENCY_P95_MS, p95Latency));
        
        assertTrue(errorRate <= MAX_ERROR_RATE,
            String.format("Taxa de erro acima do esperado. Esperado: %.2f%%, Atual: %.2f%%",
                MAX_ERROR_RATE * 100, errorRate * 100));

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }

    private double calculateP95Latency(List<Long> latencies) {
        if (latencies.isEmpty()) return 0;
        
        List<Long> sortedLatencies = new ArrayList<>(latencies);
        sortedLatencies.sort(Long::compareTo);
        
        int index = (int) Math.ceil(0.95 * sortedLatencies.size()) - 1;
        return sortedLatencies.get(index);
    }

    private SimulacaoCreditoComArmazenamentoRequestDto criarRequestSimulacao() {
        return new SimulacaoCreditoComArmazenamentoRequestDto(
            1L,
            new BigDecimal("10000.00"),
            LocalDate.now().minusYears(35),
            12,
            "PAGAMENTO_COM_JUROS"
        );
    }

    private static class ResponseMetrics {
        long successCount = 0;
        long errorCount = 0;
        long totalLatency = 0;
    }
} 