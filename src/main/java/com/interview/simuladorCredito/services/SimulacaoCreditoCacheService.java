package com.interview.simuladorCredito.services;

import java.math.BigDecimal;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.interview.simuladorCredito.dtos.CalculoParamsDTO;
import com.interview.simuladorCredito.dtos.ResultadoCalculoPagamentoComJuros;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoResponseDto;
import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;
import com.interview.simuladorCredito.services.calculos.TipoCalculoJurosSistema;
import com.interview.simuladorCredito.services.calculos.TipoCalculoJurosSistemaLocator;

@Service
public class SimulacaoCreditoCacheService {

    @Cacheable(
        value = "simulacoesCache",
        key = "T(java.util.Objects).hash(#valorSolicitado, #prazoMeses, #taxaAnual, #tipoCalculo)"
    )
    public SimulacaoCreditoResponseDto realizarSimulacaoCacheada(
        BigDecimal valorSolicitado,
        int prazoMeses,
        BigDecimal taxaAnual,
        String tipoCalculo,
        TipoCalculoJurosSistemaLocator calculoLocator
    ) {
        System.out.println(">>> Executando cálculo REAL (NÃO usando a memória cache)");
        CalculoParamsDTO calculosParams = new CalculoParamsDTO(valorSolicitado, prazoMeses);
        TaxaPorFaixaEtariaConfiguracao faixa = new TaxaPorFaixaEtariaConfiguracao();
        faixa.setTaxaAnual(taxaAnual);

        TipoCalculoJurosSistema calculoStrategy = calculoLocator.getStrategy(tipoCalculo);
        ResultadoCalculoPagamentoComJuros resultado = calculoStrategy.efetuarCalculos(calculosParams, faixa);

        return new SimulacaoCreditoResponseDto(
            resultado.valorTotal(), resultado.parcelaMensal(), resultado.totalJuros()
        );
    }
}
