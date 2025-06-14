package com.interview.simuladorCredito.validadores;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;

class SimulacaoCreditoValidadorTest {

    private final SimulacaoCreditoValidador validador = new SimulacaoCreditoValidador();

    @Test
    @DisplayName("Deve validar corretamente uma configuração de faixa etária existente")
    void deveValidarConfiguracaoFaixaEtariaExistente() {
        int idade = 30;
        TaxaPorFaixaEtariaConfiguracao faixa = TaxaPorFaixaEtariaConfiguracao.builder()
                .idadeMinima(18)
                .idadeMaxima(60)
                .taxaAnual(new BigDecimal("0.050"))
                .build();

        assertDoesNotThrow(() -> validador.validarConfiguracaoFaixaEtariaExistente(idade, faixa),
                "Não deve lançar exceção quando a faixa etária existe");
    }

    @Test
    @DisplayName("Deve lançar exceção quando não existe configuração de faixa etária")
    void deveLancarExcecaoQuandoNaoExisteConfiguracaoFaixaEtaria() {
        int idade = 30;
        TaxaPorFaixaEtariaConfiguracao faixa = null;

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> validador.validarConfiguracaoFaixaEtariaExistente(idade, faixa),
                "Deveria lançar IllegalStateException quando a faixa etária não existe");

        assertEquals("Não há configuração de taxa para idade 30", exception.getMessage(),
                "A mensagem de erro deve indicar a idade que não possui configuração");
    }

    @Test
    @DisplayName("Deve validar corretamente uma taxa de juros válida")
    void deveValidarTaxaValida() {
        BigDecimal taxa = new BigDecimal("0.050"); // 5% ao ano

        assertDoesNotThrow(() -> validador.validarTaxa(taxa),
                "Não deve lançar exceção quando a taxa é válida");
    }

    @Test
    @DisplayName("Deve lançar exceção quando a taxa de juros é zero")
    void deveLancarExcecaoQuandoTaxaEhZero() {
        BigDecimal taxa = BigDecimal.ZERO;

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> validador.validarTaxa(taxa),
                "Deveria lançar IllegalStateException quando a taxa é zero");

        assertEquals("O valor da taxa não pode ser 0 ", exception.getMessage(),
                "A mensagem de erro deve indicar que a taxa não pode ser zero");
    }
} 