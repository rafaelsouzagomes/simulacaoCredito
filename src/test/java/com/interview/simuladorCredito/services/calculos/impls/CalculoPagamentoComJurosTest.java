package com.interview.simuladorCredito.services.calculos.impls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interview.simuladorCredito.dtos.ResultadoCalculoPagamentoComJuros;

class CalculoPagamentoComJurosTest {

    private final CalculoPagamentoComJuros calculoPagamentoComJuros = new CalculoPagamentoComJuros();

    @Test
    @DisplayName("Deve calcular corretamente a parcela mensal para empréstimo de R$ 1.000,00 em 10 meses com taxa de 4% ao ano")
    void deveCalcularParcelaMensalCorretamente() {
        BigDecimal taxaMensal = new BigDecimal("0.003333"); // 4% ao ano / 12 meses
        BigDecimal valorPresenteEmprestimo = new BigDecimal("1000.00");
        int numeroParcelas = 10;

        BigDecimal parcelaMensal = calculoPagamentoComJuros.calcularParcelaMensal(taxaMensal, valorPresenteEmprestimo, numeroParcelas);

        assertEquals(new BigDecimal("101.84"), parcelaMensal, "O valor da parcela mensal deve ser R$ 101,84");
    }

    @Test
    @DisplayName("Deve calcular corretamente todos os valores da simulação para empréstimo de R$ 1.000,00 em 10 meses com taxa de 4% ao ano")
    void deveCalcularValoresSimulacaoCorretamente() {
        BigDecimal taxaMensal = new BigDecimal("0.003333"); // 4% ao ano / 12 meses
        BigDecimal valorPresenteEmprestimo = new BigDecimal("1000.00");
        int numeroParcelas = 10;

        ResultadoCalculoPagamentoComJuros resultado = calculoPagamentoComJuros.calcularValoresSimulacaoCredito(taxaMensal, valorPresenteEmprestimo, numeroParcelas);

        assertNotNull(resultado);
        
        assertEquals(new BigDecimal("101.84"), resultado.parcelaMensal(), "O valor da parcela mensal deve ser R$ 101,84");
        assertEquals(new BigDecimal("1018.40"), resultado.valorTotal(), "O valor total deve ser R$ 1.018,40");
        assertEquals(new BigDecimal("18.40"), resultado.totalJuros(), "O total de juros deve ser R$ 18,40");

        assertEquals(resultado.valorTotal(), resultado.parcelaMensal().multiply(BigDecimal.valueOf(numeroParcelas)), 
            "O valor total deve ser igual ao valor das parcelas multiplicado pelo número de parcelas");

        assertEquals(resultado.totalJuros(), resultado.valorTotal().subtract(valorPresenteEmprestimo),
            "O total de juros deve ser a diferença entre o valor total e o valor solicitado");
    }

    @Test
    @DisplayName("Deve calcular corretamente a parcela mensal para empréstimo de R$ 80.000,00 em 48 meses com taxa de 5% ao ano")
    void deveCalcularParcelaMensalEmprestimoGrande() {
        BigDecimal taxaMensal = new BigDecimal("0.004167"); // 5% ao ano / 12 meses
        BigDecimal valorPresenteEmprestimo = new BigDecimal("80000.00");
        int numeroParcelas = 48;

        BigDecimal parcelaMensal = calculoPagamentoComJuros.calcularParcelaMensal(taxaMensal, valorPresenteEmprestimo, numeroParcelas);

        assertEquals(new BigDecimal("1842.36"), parcelaMensal, "O valor da parcela mensal deve ser R$ 1.842,36");
    }

    @Test
    @DisplayName("Deve calcular corretamente todos os valores da simulação para empréstimo de R$ 80.000,00 em 48 meses com taxa de 5% ao ano")
    void deveCalcularValoresSimulacaoEmprestimoGrande() {
        BigDecimal taxaMensal = new BigDecimal("0.004167"); // 5% ao ano / 12 meses
        BigDecimal valorPresenteEmprestimo = new BigDecimal("80000.00");
        int numeroParcelas = 48;

        ResultadoCalculoPagamentoComJuros resultado = calculoPagamentoComJuros.calcularValoresSimulacaoCredito(taxaMensal, valorPresenteEmprestimo, numeroParcelas);

        assertNotNull(resultado);
        
        assertEquals(new BigDecimal("1842.36"), resultado.parcelaMensal(), "O valor da parcela mensal deve ser R$ 1.842,36");
        assertEquals(new BigDecimal("88433.28"), resultado.valorTotal(), "O valor total deve ser R$ 88.433,28");
        assertEquals(new BigDecimal("8433.28"), resultado.totalJuros(), "O total de juros deve ser R$ 8.433,28");

        assertEquals(resultado.valorTotal(), resultado.parcelaMensal().multiply(BigDecimal.valueOf(numeroParcelas)), 
            "O valor total deve ser igual ao valor das parcelas multiplicado pelo número de parcelas");

        assertEquals(resultado.totalJuros(), resultado.valorTotal().subtract(valorPresenteEmprestimo),
            "O total de juros deve ser a diferença entre o valor total e o valor solicitado");
    }
} 