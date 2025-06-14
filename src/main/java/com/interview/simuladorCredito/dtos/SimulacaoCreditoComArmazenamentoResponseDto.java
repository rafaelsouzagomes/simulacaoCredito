package com.interview.simuladorCredito.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimulacaoCreditoComArmazenamentoResponseDto {

    private Long id;
    private BigDecimal valorSolicitado;
    private BigDecimal valorTotal;
    private BigDecimal parcelaMensal;
    private BigDecimal totalJuros;
    private BigDecimal taxaAnualAplicada;
}
