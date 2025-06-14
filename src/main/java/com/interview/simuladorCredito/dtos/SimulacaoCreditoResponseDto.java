package com.interview.simuladorCredito.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimulacaoCreditoResponseDto {

    private BigDecimal valorTotal;
    private BigDecimal parcelaMensal;
    private BigDecimal totalJuros;
}
