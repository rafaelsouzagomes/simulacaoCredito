package com.interview.simuladorCredito.dtos;

import java.math.BigDecimal;

public record ResultadoCalculoPagamentoComJuros(
		
		BigDecimal parcelaMensal, 
		BigDecimal valorTotal,
		BigDecimal totalJuros
) {}



