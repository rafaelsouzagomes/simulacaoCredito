package com.interview.simuladorCredito.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoRequestDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoResponseDto;
import com.interview.simuladorCredito.services.SimulacaoCreditoService;
import com.interview.simuladorCredito.services.calculos.TipoCalculo;

@WebMvcTest(SimulacaoCreditoController.class)
class SimulacaoCreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SimulacaoCreditoService simulacaoCreditoService;

    @Test
    void deveCriarSimulacaoComSucesso() throws Exception {
        // Arrange
        SimulacaoCreditoComArmazenamentoRequestDto request = new SimulacaoCreditoComArmazenamentoRequestDto(
            1L,
            new BigDecimal("10000.00"),
            LocalDate.now().minusYears(30),
            12,
            TipoCalculo.PADRAO.getCodigo()
        );

        SimulacaoCreditoComArmazenamentoResponseDto response = new SimulacaoCreditoComArmazenamentoResponseDto(
            1L,
            new BigDecimal("10000.00"),
            new BigDecimal("11000.00"),
            new BigDecimal("916.67"),
            new BigDecimal("1000.00"),
            new BigDecimal("0.050")
        );

        when(simulacaoCreditoService.criarSimulacao(any(SimulacaoCreditoComArmazenamentoRequestDto.class)))
            .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/simulacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valorSolicitado").value(10000.00))
                .andExpect(jsonPath("$.valorTotal").value(11000.00))
                .andExpect(jsonPath("$.parcelaMensal").value(916.67))
                .andExpect(jsonPath("$.totalJuros").value(1000.00))
                .andExpect(jsonPath("$.taxaAnualAplicada").value(0.050));
    }

    @Test
    void deveRetornarErroQuandoRequestInvalido() throws Exception {
        // Arrange
        SimulacaoCreditoComArmazenamentoRequestDto request = new SimulacaoCreditoComArmazenamentoRequestDto(
            1L,
            new BigDecimal("500.00"), // Valor abaixo do mínimo
            LocalDate.now().minusYears(30),
            12,
            TipoCalculo.PADRAO.getCodigo()
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/simulacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valorSolicitado").value("Valor mínimo é R$1000,00"));
    }

    @Test
    void deveRetornarErroQuandoDataNascimentoFutura() throws Exception {
        // Arrange
        SimulacaoCreditoComArmazenamentoRequestDto request = new SimulacaoCreditoComArmazenamentoRequestDto(
            1L,
            new BigDecimal("10000.00"),
            LocalDate.now().plusYears(1), // Data futura
            12,
            TipoCalculo.PADRAO.getCodigo()
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/simulacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.dataNascimento").exists());
    }

    @Test
    void deveRetornarErroQuandoPrazoMesesNegativo() throws Exception {
        // Arrange
        SimulacaoCreditoComArmazenamentoRequestDto request = new SimulacaoCreditoComArmazenamentoRequestDto(
            1L,
            new BigDecimal("10000.00"),
            LocalDate.now().minusYears(30),
            -12, // Prazo negativo
            TipoCalculo.PADRAO.getCodigo()
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/simulacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.prazoMeses").exists());
    }
} 