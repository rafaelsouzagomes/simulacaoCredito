package com.interview.simuladorCredito.validadores;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;

@Service
public class SimulacaoCreditoValidador {

	public void validarConfiguracaoFaixaEtariaExistente(int idade, TaxaPorFaixaEtariaConfiguracao faixa) {
		if (faixa == null) 
            throw new IllegalStateException("Não há configuração de taxa para idade " + idade);
	}

	public void validarTaxa(BigDecimal taxa) {
		if (taxa.compareTo(BigDecimal.ZERO) == 0) 
            throw new IllegalStateException("O valor da taxa não pode ser 0 ");
	}

}
