package com.interview.simuladorCredito.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.interview.simuladorCredito.services.calculos.TipoCalculo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public record SimulacaoCreditoRequestDto (
   
        @NotNull
        @DecimalMin(value = "1000.00", message = "Valor mínimo é R$1000,00")
        BigDecimal valorSolicitado,
        
        @NotNull @Past
        LocalDate dataNascimento,
        
        @Positive
        int prazoMeses,
        
        String tipoCalculo
	) {
	    public TipoCalculo getTipoCalculo() {
	        return TipoCalculo.fromCodigo(tipoCalculo);
	    }
	}