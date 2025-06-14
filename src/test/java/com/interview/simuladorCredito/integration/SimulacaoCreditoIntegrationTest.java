package com.interview.simuladorCredito.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoRequestDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoResponseDto;
import com.interview.simuladorCredito.services.SimulacaoCreditoService;
import com.interview.simuladorCredito.services.calculos.TipoCalculo;

@SpringBootTest
@ActiveProfiles("test")
public class SimulacaoCreditoIntegrationTest {

    @Autowired
    private SimulacaoCreditoService simulacaoCreditoService;

    @Test
    public void testSimulacaoCreditoCompleta() {
        SimulacaoCreditoComArmazenamentoRequestDto request = new SimulacaoCreditoComArmazenamentoRequestDto(
            1L, // idUsuario
            new BigDecimal("10000.00"),
            LocalDate.now().minusYears(30),
            12,
            TipoCalculo.PADRAO.getCodigo()
        );

        SimulacaoCreditoComArmazenamentoResponseDto response = simulacaoCreditoService.criarSimulacao(request);

        assertNotNull(response);
        assertNotNull(response.getValorTotal());
        assertNotNull(response.getParcelaMensal());
        assertNotNull(response.getTotalJuros());
        assertNotNull(response.getTaxaAnualAplicada());
        
        assertTrue(response.getValorTotal().compareTo(request.valorSolicitado()) > 0);
        
        assertEquals(
            response.getValorTotal(),
            response.getParcelaMensal().multiply(BigDecimal.valueOf(request.prazoMeses())),
            "O valor total deve ser igual ao valor das parcelas multiplicado pelo número de parcelas"
        );
        
        assertEquals(
            response.getTotalJuros(),
            response.getValorTotal().subtract(request.valorSolicitado()),
            "O total de juros deve ser a diferença entre o valor total e o valor solicitado"
        );
    }

    @Test
    public void testSimulacaoCreditoComValoresDiferentes() {
        SimulacaoCreditoComArmazenamentoRequestDto request = new SimulacaoCreditoComArmazenamentoRequestDto(
            1L, // idUsuario
            new BigDecimal("5000.00"),
            LocalDate.now().minusYears(30),
            6,
            TipoCalculo.PADRAO.getCodigo()
        );

        SimulacaoCreditoComArmazenamentoResponseDto response = simulacaoCreditoService.criarSimulacao(request);

        assertNotNull(response);
        assertTrue(response.getValorTotal().compareTo(new BigDecimal("5000.00")) > 0);
        assertEquals(6, request.prazoMeses());
    }

    @Test
    public void testSimulacaoCreditoComParcelasDiferentes() {
        SimulacaoCreditoComArmazenamentoRequestDto request = new SimulacaoCreditoComArmazenamentoRequestDto(
            1L, // idUsuario
            new BigDecimal("15000.00"),
            LocalDate.now().minusYears(30),
            24,
            TipoCalculo.PADRAO.getCodigo()
        );

        SimulacaoCreditoComArmazenamentoResponseDto response = simulacaoCreditoService.criarSimulacao(request);

        assertNotNull(response);
        assertEquals(24, request.prazoMeses());
        assertTrue(response.getValorTotal().compareTo(new BigDecimal("15000.00")) > 0);
    }
} 