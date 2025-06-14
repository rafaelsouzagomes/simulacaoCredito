package com.interview.simuladorCredito.dtos;

import java.math.BigDecimal;

public record CalculoParamsDTO (
		
	BigDecimal valorSolicitado,
	int prazoMeses
) {}