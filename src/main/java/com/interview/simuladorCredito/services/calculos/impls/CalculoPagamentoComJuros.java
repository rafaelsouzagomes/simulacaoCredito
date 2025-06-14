package com.interview.simuladorCredito.services.calculos.impls;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.simuladorCredito.dtos.CalculoParamsDTO;
import com.interview.simuladorCredito.dtos.ResultadoCalculoPagamentoComJuros;
import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;
import com.interview.simuladorCredito.services.calculos.TipoCalculoJurosSistema;
import com.interview.simuladorCredito.validadores.SimulacaoCreditoValidador;

@Service
public class CalculoPagamentoComJuros implements TipoCalculoJurosSistema {

	private SimulacaoCreditoValidador validador;
	
	//half up
	private static final MathContext MC = MathContext.DECIMAL128;
	
	@Override
	public ResultadoCalculoPagamentoComJuros efetuarCalculos(CalculoParamsDTO dto, TaxaPorFaixaEtariaConfiguracao faixa) {
		
		validador.validarTaxa(faixa.getTaxaAnual());
		
		BigDecimal taxaAnual  				= faixa.getTaxaAnual();                     // ex.: 0.050 = 5 %
        BigDecimal taxaMensal 				= taxaAnual.divide(BigDecimal.valueOf(12), MC); // r
        BigDecimal valorPresenteEmprestimo 	= dto.valorSolicitado(); // -> pv
        int numeroParcelas          		= dto.prazoMeses(); // -> n                  

        
        return calcularValoresSimulacaoCredito(taxaMensal, valorPresenteEmprestimo, numeroParcelas);

	}


	protected ResultadoCalculoPagamentoComJuros calcularValoresSimulacaoCredito(BigDecimal taxaMensal, BigDecimal valorPresenteEmprestimo, int numeroParcelas) {
		BigDecimal parcelaMensal = calcularParcelaMensal(taxaMensal, valorPresenteEmprestimo, numeroParcelas);

        
        BigDecimal valorTotal = parcelaMensal.multiply(BigDecimal.valueOf(numeroParcelas))
                                             .setScale(2, RoundingMode.HALF_EVEN);
        
        BigDecimal totalJuros = valorTotal.subtract(valorPresenteEmprestimo)
                                          .setScale(2, RoundingMode.HALF_EVEN);
        
        
        return new ResultadoCalculoPagamentoComJuros(parcelaMensal, valorTotal, totalJuros );
	}


	protected BigDecimal calcularParcelaMensal(BigDecimal taxaMensal, BigDecimal valorPresenteEmprestimo, int numeroParcelas) {
		// PV * r
        BigDecimal numerador = valorPresenteEmprestimo.multiply(taxaMensal, MC);

        // (1 + r)
        BigDecimal umMaisR = BigDecimal.ONE.add(taxaMensal, MC);
        
        //(1 + r)^n
        BigDecimal umMaisRPotenciaN = umMaisR.pow(numeroParcelas, MC);
        
        // (1 + r)^-n   
        BigDecimal fracaoPotenciaNegativa = BigDecimal.ONE.divide(umMaisRPotenciaN, MC);
        
        //  1 - 1 / (1 + r)^n
        BigDecimal denominador = BigDecimal.ONE.subtract(fracaoPotenciaNegativa, MC);

        // PV * R / 1 - (1 + r)^-n
        BigDecimal parcelaMensal = numerador.divide(denominador, 2, RoundingMode.HALF_EVEN);
		return parcelaMensal;
	}
	
	
	@Autowired
	public void setValidador(SimulacaoCreditoValidador validador) {
		this.validador = validador;
	}

}
